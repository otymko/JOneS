/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.compiler.MethodDescriptor;
import com.github.otymko.jos.compiler.SymbolAddress;
import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.exception.WrappedJavaException;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.Arithmetic;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.VariableReference;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ExceptionInfoContext;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.TypeFactory;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.TypeValue;
import com.github.otymko.jos.runtime.format.ValueFormatter;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import com.github.otymko.jos.util.Common;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.github.otymko.jos.util.StringUtils.toTitleCase;

/**
 * Экземпляр стековой машины
 */
public class MachineInstance {
    private static final String VARIABLE_STACK_NAME = "$stackvar";

    private final ScriptEngine engine;
    @Getter
    private final List<Scope> scopes = new ArrayList<>();
    private final Deque<IValue> operationStack = new ArrayDeque<>();
    private final Deque<ExecutionFrame> callStack = new ArrayDeque<>();
    private final Deque<ExceptionJumpInfo> exceptionsStack = new ArrayDeque<>();
    private ExecutionFrame currentFrame;
    private ModuleImage currentImage;

    public MachineInstance(ScriptEngine engine) {
        this.engine = engine;
    }

    public void implementContext(AttachableContext context) {
        var methods = context.getMethods();
        var variables = context.getVariables();
        var scope = new Scope(context, variables, methods);
        scopes.add(scope);
    }

    public void executeModuleBody(ScriptDrivenObject sdo) {
        currentImage = sdo.getModuleImage();
        if (currentImage.getEntry() >= 0) {
            var methodDescriptor = currentImage.getMethods().get(currentImage.getEntry());
            prepareReentrantMethodExecution(sdo, methodDescriptor);
            executeCode();
        }
    }

    private void prepareReentrantMethodExecution(ScriptDrivenObject sdo, MethodDescriptor methodDescriptor) {
        var image = sdo.getModuleImage();
        var frame = new ExecutionFrame();
        frame.setImage(image);
        frame.setInstructionPointer(methodDescriptor.getEntry());
        var variables = createVariables(methodDescriptor.getVariables());
        frame.setLocalVariables(variables);
        frame.setModuleLoadIndex(scopes.size() - 1);
        frame.setModuleScope(createModuleScope(sdo));

        pushFrame(frame);
    }

    public IValue executeMethod(ScriptDrivenObject sdo, int methodId, IValue[] parameters) {
        // FIXME: нельзя повторно добавлять модульскоуп
        createModuleScope(sdo);

        currentImage = sdo.getModuleImage();

        var methodDescriptor = currentImage.getMethods().get(methodId);
        prepareReentrantMethodExecution(sdo, methodDescriptor);
        currentFrame.setOneTimeCall(true);

        // TODO: подготовить параметры
        setMethodParameters(currentFrame, methodDescriptor, parameters);

        executeCode();

        IValue methodResult = null;
        if (methodDescriptor.getSignature().isFunction()) {
            methodResult = operationStack.pop();
        }

        if (callStack.size() > 1) {
            popFrame();
        }

        return methodResult;
    }

    private Scope createModuleScope(ScriptDrivenObject sdo) {
        var image = sdo.getModuleImage();

        IVariable[] variables = sdo.getState();
        var methodSize = image.getMethods().size() + sdo.getContextInfo().getMethods().length;
        MethodInfo[] methods = new MethodInfo[methodSize];
        var position = 0;
        for (var method : sdo.getContextInfo().getMethods()) {
            methods[position] = method;
            position++;
        }
        for (var method : image.getMethods()) {
            methods[position] = method.getSignature();
            position++;
        }

        return new Scope(sdo, variables, methods);
    }

    private IVariable[] createVariables(List<VariableInfo> variableInfos) {
        IVariable[] variables = new IVariable[variableInfos.size()];
        int index = 0;
        for (var variableInfo : variableInfos) {
            var variable = new Variable();
            variable.setName(variableInfo.getName());
            variable.setValue(ValueFactory.create());
            variables[index] = variable;
            index++;
        }
        return variables;
    }

    private void executeCode() {

        while (true) {
            try {

                mainCommandLoop();
                break;

            } catch (MachineException exception) {

                var errorInfo = exception.getErrorInfo();
                if (errorInfo.getLine() < 0) {
                    errorInfo.setLine(currentFrame.getLineNumber());
                    errorInfo.setSource(Common.getAbsolutPath(currentImage.getSource().getPath()));
                    Common.fillCodePositionInErrorInfo(errorInfo, currentImage, currentFrame.getLineNumber());
                }

                if (shouldRethrowException(exception))
                    throw exception;
            }
        }
    }

    private boolean shouldRethrowException(MachineException exception) {
        if (exceptionsStack.isEmpty()) {
            return true;
        }

        var callStackFrames = exception.getBslStackTrace();

        if (callStackFrames.isEmpty()) {
            callStackFrames = createCallstack();
            exception.setBslStackTrace(callStackFrames);
        }

        var handler = exceptionsStack.pop();

        // Раскрутка стека вызовов
        while (currentFrame != handler.getHandlerFrame()) {
            if (currentFrame.isOneTimeCall()) {
                exceptionsStack.push(handler);
                popFrame();
                return true;
            }

            popFrame();
        }

        currentFrame.setInstructionPointer(handler.getHandlerAddress());
        currentFrame.setLastException(exception);

        // При возникновении исключения посредине выражения
        // некому почистить стек операндов.
        // Сделаем это
        while (operationStack.size() > handler.getStackSize()) {
            operationStack.pop();
        }

        return false;
    }

    private List<StackTraceRecord> createCallstack() {
        return callStack.stream()
                .map(StackTraceRecord::new)
                .collect(Collectors.toList());
    }

    private void mainCommandLoop() {

        try {
            while (currentFrame.getInstructionPointer() >= 0
                    && currentFrame.getInstructionPointer() < currentImage.getCode().size()) {

                var command = currentImage.getCode().get(currentFrame.getInstructionPointer());
                runCommand(command.getCode(), command.getArgument());
            }
        } catch (EngineException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new WrappedJavaException(exception);
        }
    }

    private void runCommand(OperationCode opCode, int argument) {
        switch (opCode) {
            case LINE_NUM:
                lineNum(argument);
                break;
            case PUSH_CONST:
                pushConst(argument);
                break;
            case ARG_NUN:
                argNum(argument);
                break;
            case PUSH_DEFAULT_ARG:
                pushDefaultArg(argument);
                break;
            case CALL_PROC:
                callProc(argument);
                break;
            case LOAD_LOC:
                loadLoc(argument);
                break;
            case PUSH_LOC:
                pushLoc(argument);
                break;
            case PUSH_REF:
                pushRef(argument);
                break;
            case PUSH_VAR:
                pushVar(argument);
                break;
            case LOAD_VAR:
                loadVar(argument);
                break;
            case RETURN:
                toReturn(argument);
                break;
            case ADD:
                add(argument);
                break;
            case SUB:
                sub(argument);
                break;
            case MUL:
                mul(argument);
                break;
            case DIV:
                div(argument);
                break;
            case MOD:
                mod(argument);
                break;
            case NOT:
                not(argument);
                break;
            case NEG:
                neg(argument);
                break;

            case AND:
                and(argument);
                break;
            case OR:
                or(argument);
                break;
            case MAKE_BOOL:
            case BOOL:
                makeBool(argument);
                break;
            case JMP:
                jmp(argument);
                break;
            case JMP_FALSE:
                jmpFalse(argument);
                break;

            case GREATER:
                greater(argument);
                break;
            case GREATER_OR_EQUAL:
                greaterOrEqual(argument);
                break;
            case LESS:
                less(argument);
                break;
            case LESS_OR_EQUAL:
                lessOrEqual(argument);
                break;
            case EQUALS:
                toEquals(argument);
                break;
            case NOT_EQUAL:
                notEqual(argument);
                break;

            case MAKE_RAW_VALUE:
                makeRawValue(argument);
                break;
            case CALL_FUNC:
                callFunc(argument);
                break;

            case NEW_INSTANCE:
                newInstance(argument);
                break;

            case RESOLVE_METHOD_PROC:
                resolveMethodProc(argument);
                break;
            case RESOLVE_METHOD_FUNC:
                resolveMethodFunc(argument);
                break;

            case PUSH_INDEXED:
                pushIndexed(argument);
                break;
            case BEGIN_TRY:
                beginTry(argument);
                break;
            case END_TRY:
                endTry(argument);
                break;
            case RAISE_EXCEPTION:
                raiseException(argument);
                break;

            case ASSIGN_REF:
                assignReference(argument);
                break;
            case RESOLVE_PROP:
                resolveProp(argument);
                break;

            // Функции работы с типами
            case TYPE:
                callType(argument);
                break;
            case VAL_TYPE:
                callTypeOf(argument);
                break;

            case EXCEPTION_DESCR:
                exceptionDescr(argument);
                break;
            case EXCEPTION_INFO:
                exceptionInfo(argument);
                break;

            case PUSH_ITERATOR:
                pushIterator(argument);
                break;
            case ITERATOR_NEXT:
                iteratorNext(argument);
                break;
            case STOP_ITERATOR:
                stopIterator(argument);
                break;

            case PUSH_TMP:
                pushTmp(argument);
                break;
            case INC:
                increment(argument);
                break;
            case JMP_COUNTER:
                jmpCounter(argument);
                break;
            case POP_TMP:
                popTmp(argument);
                break;

            case NOP:
                nop(argument);
                break;

            case STR_LEN:
                stringLength(argument);
                break;
            case U_CASE:
                upperCase(argument);
                break;
            case L_CASE:
                lowerCase(argument);
                break;
            case T_CASE:
                titleCase(argument);
                break;
            case TRIM_L:
                trimL(argument);
                break;
            case TRIM_R:
                trimR(argument);
                break;
            case TRIM_LR:
                trimLR(argument);
                break;
            case STR_POS:
                strPos();
                break;

            case LEFT:
                left(argument);
                break;
            case RIGHT:
                right(argument);
                break;
            case MID:
                middle(argument);
                break;

            case EMPTY_STR:
                emptyStr(argument);
                break;
            case CHR:
                chr(argument);
                break;
            case CHR_CODE:
                chrCode(argument);
                break;
            case STR_REPLACE:
                strReplace(argument);
                break;

            case CURRENT_DATE:
                currentDate(argument);
                break;
            case NUMBER:
                number(argument);
                break;
            case STR:
                str(argument);
                break;
            case DATE:
                date(argument);
                break;

            case FORMAT:
                format(argument);
                break;
            default:
                throw MachineException.wrongStackConditionException();
        }
    }

    private void format(int argument) {
        final var formatString = operationStack.pop().asString();
        final var value = operationStack.pop().getRawValue();
        operationStack.push(ValueFactory.create(ValueFormatter.format(value, formatString)));
        nextInstruction();
    }

    private void pushDefaultArg(int argument) {
        operationStack.push(ValueFactory.create());
        nextInstruction();
    }

    private void currentDate(int argument) {
        operationStack.push(ValueFactory.create(new Date()));
        nextInstruction();
    }

    private void number(int argument) {
        final var source = operationStack.pop();
        operationStack.push(ValueFactory.create(source.asNumber()));
        nextInstruction();
    }

    private void date(int arg) {
        if (arg == 1) {
            var strDate = operationStack.pop().asString();
            operationStack.push(ValueFactory.parse(strDate, DataType.DATE));
        } else if (arg >= 3 && arg <= 6) {
            int[] factArgs = new int[6];

            for (int i = arg - 1; i >= 0; i--) {
                factArgs[i] = operationStack.pop().asNumber().intValue();
            }

            @SuppressWarnings("MagicConstant")
            var date = new GregorianCalendar(
                    factArgs[0],
                    factArgs[1] - 1,
                    factArgs[2],
                    factArgs[3],
                    factArgs[4],
                    factArgs[5]);

            operationStack.push(ValueFactory.create(date.getTime()));

        } else {
            throw new MachineException("Неверное количество параметров");
        }

        nextInstruction();
    }

    private void str(int argument) {
        final var source = operationStack.pop();
        operationStack.push(ValueFactory.create(source.asString()));
        nextInstruction();
    }

    private void upperCase(int argument) {
        var value = operationStack.pop().asString().toUpperCase(Locale.ENGLISH);
        operationStack.push(ValueFactory.create(value));
        nextInstruction();
    }

    private void lowerCase(int argument) {
        var value = operationStack.pop().asString().toLowerCase(Locale.ENGLISH);
        operationStack.push(ValueFactory.create(value));
        nextInstruction();
    }

    private void titleCase(int argument) {
        var value = operationStack.pop().asString();
        value = toTitleCase(value);
        operationStack.push(ValueFactory.create(value));
        nextInstruction();
    }

    private void trimL(int argument) {
        var value = operationStack.pop().asString();
        operationStack.push(ValueFactory.create(value.stripLeading()));
        nextInstruction();
    }

    private void trimR(int argument) {
        var value = operationStack.pop().asString();
        operationStack.push(ValueFactory.create(value.stripTrailing()));
        nextInstruction();
    }

    private void trimLR(int argument) {
        var value = operationStack.pop().asString();
        operationStack.push(ValueFactory.create(value.strip()));
        nextInstruction();
    }

    private void strPos() {
        var needle = operationStack.pop().asString();
        var haystack = operationStack.pop().asString();

        var result = haystack.indexOf(needle) + 1;
        operationStack.push(ValueFactory.create(result));
        nextInstruction();
    }

    private void emptyStr(int argument) {
        var value = operationStack.pop().asString().isBlank();
        operationStack.push(ValueFactory.create(value));
        nextInstruction();
    }

    private void chr(int argument) {
        var value = operationStack.pop().asNumber().intValue();
        var stringFromChar = new String(new int[]{value}, 0, 0);

        operationStack.push(ValueFactory.create(stringFromChar));
        nextInstruction();
    }

    private void chrCode(int argCount) {

        String string;
        int position;

        if (argCount == 1) {
            string = operationStack.pop().asString();
            position = 0;
        } else if (argCount == 2) {
            position = operationStack.pop().asNumber().intValue() - 1;
            string = operationStack.pop().asString();
        } else {
            throw new IllegalStateException("argCount = " + argCount);
        }

        int result;
        if (string.length() == 0)
            result = 0;
        else if (position >= 0 && position < string.length())
            result = string.charAt(position);
        else
            throw MachineException.invalidArgumentValueException();

        operationStack.push(ValueFactory.create(result));

        nextInstruction();
    }

    private void strReplace(int argument) {
        var newVal = operationStack.pop().asString();
        var searchVal = operationStack.pop().asString();
        var sourceString = operationStack.pop().asString();

        var result = sourceString.replace(searchVal, newVal);
        operationStack.push(ValueFactory.create(result));

        nextInstruction();
    }

    private void stringLength(int argument) {
        var value = operationStack.pop().asString();
        operationStack.push(ValueFactory.create(value.length()));
        nextInstruction();
    }

    private void left(int argument) {
        var length = operationStack.pop().asNumber().intValue();
        var value = operationStack.pop().asString();

        if (length > value.length()) {
            length = value.length();
        }

        if (length < 0) {
            operationStack.push(ValueFactory.create(""));
        } else {
            var newValue = value.substring(0, length);
            operationStack.push(ValueFactory.create(newValue));
        }

        nextInstruction();
    }

    private void right(int argument) {
        var length = operationStack.pop().asNumber().intValue();
        var value = operationStack.pop().asString();

        if (length > value.length()) {
            length = value.length();
        }

        if (length < 0) {
            operationStack.push(ValueFactory.create(""));
        } else {
            var startPosition = value.length() - length;
            var newValue = value.substring(startPosition);
            operationStack.push(ValueFactory.create(newValue));
        }

        nextInstruction();
    }

    private void middle(int argument) {
        String value;
        int start;
        int length;
        if (argument == 2) {
            start = operationStack.pop().asNumber().intValue();
            value = operationStack.pop().asString();
            length = value.length() - start + 1;
        } else {
            length = operationStack.pop().asNumber().intValue();
            start = operationStack.pop().asNumber().intValue();
            value = operationStack.pop().asString();
        }

        if (start < 1) {
            start = 1;
        }

        int end = start - 1 + length;
        if (end > value.length()) {
            end = value.length();
        }

        String result;
        if (start > value.length() || length == 0) {
            result = "";
        } else {
            result = value.substring(start - 1, end);
        }

        operationStack.push(ValueFactory.create(result));
        nextInstruction();
    }

    private void nop(int argument) {
        nextInstruction();
    }

    private void pushTmp(int argument) {
        var value = operationStack.pop();
        currentFrame.getLocalFrameStack().push(value);
        nextInstruction();
    }

    private void increment(int argument) {
        var operand = operationStack.pop().asNumber().intValue();
        operationStack.push(ValueFactory.create(++operand));
        nextInstruction();
    }

    private void jmpCounter(int argument) {
        var counter = operationStack.pop().getRawValue();
        var limit = currentFrame.getLocalFrameStack().peek();
        if (counter.compareTo(limit) <= 0) {
            nextInstruction();
        } else {
            jmp(argument);
        }
    }

    private void popTmp(int argument) {
        var value = currentFrame.getLocalFrameStack().pop();
        if (argument == 0) {
            operationStack.push(value);
        }
        nextInstruction();
    }

    private void beginTry(int argument) {
        var info = new ExceptionJumpInfo();
        info.setHandlerAddress(argument);
        info.setHandlerFrame(currentFrame);
        info.setStackSize(operationStack.size());

        exceptionsStack.push(info);
        nextInstruction();
    }

    private void endTry(int argument) {
        if (exceptionsStack.size() > 0 && exceptionsStack.peek().getHandlerFrame() == currentFrame) {
            exceptionsStack.pop();
        }
        currentFrame.setLastException(null);
        nextInstruction();
    }

    private void raiseException(int argument) {
        if (argument < 0) {
            if (currentFrame.getLastException() == null) {
                // Если в блоке Исключение была еще одна Попытка, то она затерла lastException
                // 1С в этом случае бросает новое пустое исключение
                throw new MachineException("");
            }

            throw currentFrame.getLastException();
        } else {
            var exceptionValue = operationStack.pop().getRawValue();

            throw new MachineException(exceptionValue.asString());
        }
    }

    private void exceptionDescr(int integer) {
        if (currentFrame.getLastException() != null) {
            var exceptionInfo = currentFrame.getLastException();
            String message;
            if (exceptionInfo instanceof MachineException) {
                // TODO: варианты текстов исключений привести к иерархии классов 1Script
                var machineExc = (MachineException) exceptionInfo;
                message = machineExc.getMessageWithoutCodeFragment();
            } else {
                message = exceptionInfo.getMessage();
            }
            operationStack.push(ValueFactory.create(message));
        } else {
            operationStack.push(ValueFactory.create(""));
        }
        nextInstruction();
    }

    private void exceptionInfo(int integer) {
        if (currentFrame.getLastException() != null) {
            var excInfo = new ExceptionInfoContext(currentFrame.getLastException());
            operationStack.push(excInfo);
        } else {
            operationStack.push(ValueFactory.create());
        }
        nextInstruction();
    }

    private void iteratorNext(int argument) {
        if (currentFrame.getLocalFrameStack().isEmpty()) {
            throw MachineException.wrongStackConditionException();
        }

        var iteratorValue = currentFrame.getLocalFrameStack().peek().getRawValue();
        if (!(iteratorValue instanceof IteratorValue)) {
            throw MachineException.iteratorIsNotDefinedException();
        }

        var iterator = ((IteratorValue) iteratorValue).iterator();
        var hasNext = iterator.hasNext();
        if (hasNext) {
            operationStack.push(iterator.next());
        }
        operationStack.push(ValueFactory.create(hasNext));
        nextInstruction();
    }

    private void pushIterator(int argument) {
        var collection = operationStack.pop().getRawValue();
        if (!(collection instanceof CollectionIterable)) {
            throw MachineException.iteratorIsNotDefinedException();
        }

        var iterable = (CollectionIterable) collection;
        IValue iterator = iterable.iterator();
        currentFrame.getLocalFrameStack().push(iterator);
        nextInstruction();
    }

    private void stopIterator(int argument) {
        if (currentFrame.getLocalFrameStack().isEmpty()) {
            throw MachineException.wrongStackConditionException();
        }

        var iteratorValue = currentFrame.getLocalFrameStack().peek().getRawValue();
        if (!(iteratorValue instanceof IteratorValue)) {
            throw MachineException.iteratorIsNotDefinedException();
        }

        var iterator = ((IteratorValue) iteratorValue).iterator();
        nextInstruction();
    }

    private void neg(int argument) {
        var value = operationStack.pop();
        operationStack.push(Arithmetic.negative(value));
        nextInstruction();
    }

    private void callType(int argument) {
        var typeName = operationStack.pop().asString();
        var type = engine.getTypeManager().getContextInfoByName(typeName);
        if (type.isEmpty()) {
            throw MachineException.typeNotRegisteredException(typeName);
        }
        var value = new TypeValue(type.get());
        operationStack.push(value);
        nextInstruction();
    }

    private void callTypeOf(int argument) {
        final var value = operationStack.pop().getRawValue();
        if (!(value instanceof RuntimeContext)) {
            throw MachineException.typeNotSupportedException(value.getClass().getSimpleName());
        }
        var contextType = (RuntimeContext) value;
        var type = new TypeValue((contextType.getContextInfo()));
        operationStack.push(type);
        nextInstruction();
    }

    private void pushIndexed(int argument) {
        var index = operationStack.pop();
        var context = breakVariableLink(operationStack.pop()); // FIXME: ??

        if (!(context instanceof IndexAccessor)) {
            throw MachineException.objectNotSupportAccessByIndexException();
        }

        var variable = VariableReference.createIndexedPropertyReference((RuntimeContext) context,
                index, VARIABLE_STACK_NAME);

        operationStack.push(variable);
        nextInstruction();
    }

    private void assignReference(int argument) {
        var value = breakVariableLink(operationStack.pop());

        var variable = operationStack.pop();
        if (variable instanceof IVariable) {
            var reference = (IVariable) variable;
            reference.setValue(value);
        } else {
            throw MachineException.wrongStackConditionException();
        }

        nextInstruction();
    }

    private void resolveProp(int argument) {
        var runtimeContext = (RuntimeContext) operationStack.pop().getRawValue();
        var propertyName = currentImage.getConstants().get(argument).getValue();

        VariableReference reference = null;
        if (runtimeContext instanceof PropertyNameAccessor) {
            var propertyNameAccessor = (PropertyNameAccessor) runtimeContext;
            if (propertyNameAccessor.hasProperty(propertyName)) {
                reference = VariableReference.createDynamicPropertyNameReference(runtimeContext, propertyName, VARIABLE_STACK_NAME);
            }
        }

        if (reference == null) {
            var indexProperty = runtimeContext.findProperty(propertyName.asString());
            if (indexProperty >= 0) {
                reference = VariableReference.createContextPropertyReference(runtimeContext, indexProperty, VARIABLE_STACK_NAME);
            }
        }

        if (reference == null) {
            throw MachineException.getPropertyNotFoundException(propertyName.asString());
        }

        operationStack.push(reference);
        nextInstruction();
    }

    private void resolveMethodCall(int argument) {
        int argumentCount = operationStack.pop().asNumber().intValue();

        var factArgumentValues = new IValue[argumentCount];
        for (var index = argumentCount - 1; index >= 0; index--) {
            var value = operationStack.pop();
            // если по значению BreakVariableLink
            factArgumentValues[index] = value;
        }

        var instance = (operationStack.pop()).getRawValue();
        if (!(instance instanceof RuntimeContext)) {
            throw MachineException.objectIsNotContextTypeException();
        }

        var context = (RuntimeContext) instance;

        var methodName = currentImage.getConstants().get(argument).getValue().asString();
        var methodId = context.findMethodId(methodName);
        if (methodId < 0) {
            throw MachineException.methodNotFoundException(methodName);
        }

        var methodInfo = context.getMethodById(methodId);
        var argumentValues = new IValue[methodInfo.getParameters().length];
        fillArgumentValueFromFact(methodInfo, factArgumentValues, argumentValues);

        callContext(context, methodId, methodInfo, argumentValues);

        nextInstruction();
    }

    private void resolveMethodFunc(int argument) {

        resolveMethodCall(argument);

    }

    private void resolveMethodProc(int argument) {

        resolveMethodCall(argument);

    }

    private void newInstance(int argument) {
        var argumentValues = new IValue[argument];

        for (var position = argument; position > 0; position--) {
            argumentValues[position - 1] = breakVariableLink(operationStack.pop());
        }

        var typeName = operationStack.pop().asString();
        var contextInfoOptional = engine.getTypeManager().getContextInfoByName(typeName);
        if (contextInfoOptional.isEmpty()) {
            throw MachineException.typeNotRegisteredException(typeName);
        }

        var contextInfo = contextInfoOptional.get();
        if (contextInfo == ContextInfo.EMPTY) {
            throw MachineException.typeNotRegisteredException(typeName);
        }

        // TODO: MachineRuntimeException.constructorNotFoundException
        var typeInstance = TypeFactory.callConstructor(contextInfo, argumentValues);
        operationStack.push(typeInstance);

        nextInstruction();
    }

    private void makeRawValue(int argument) {
        var value = operationStack.pop().getRawValue();
        operationStack.push(value);
        nextInstruction();
    }

    private void notEqual(int notEqual) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        var newValue = ValueFactory.create(valueOne.compareTo(valueTwo) != 0);
        operationStack.push(newValue);
        nextInstruction();
    }

    private void toEquals(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        var newValue = ValueFactory.create(valueOne.compareTo(valueTwo) == 0);
        operationStack.push(newValue);
        nextInstruction();
    }

    private void greater(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        var newValue = ValueFactory.create(valueTwo.compareTo(valueOne) > 0);
        operationStack.push(newValue);
        nextInstruction();
    }

    private void greaterOrEqual(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        var newValue = ValueFactory.create(valueTwo.compareTo(valueOne) >= 0);
        operationStack.push(newValue);
        nextInstruction();
    }

    private void lessOrEqual(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        var newValue = ValueFactory.create(valueTwo.compareTo(valueOne) <= 0);
        operationStack.push(newValue);
        nextInstruction();
    }

    private void less(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        var newValue = ValueFactory.create(valueTwo.compareTo(valueOne) < 0);
        operationStack.push(newValue);
        nextInstruction();
    }

    private void add(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        operationStack.push(Arithmetic.add(valueTwo, valueOne));
        nextInstruction();
    }

    private void sub(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        operationStack.push(Arithmetic.sub(valueTwo, valueOne));
        nextInstruction();
    }

    private void mul(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        operationStack.push(Arithmetic.mul(valueTwo, valueOne));
        nextInstruction();
    }

    private void div(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        operationStack.push(Arithmetic.div(valueTwo, valueOne));
        nextInstruction();
    }

    private void mod(int argument) {
        var valueOne = operationStack.pop();
        var valueTwo = operationStack.pop();
        operationStack.push(Arithmetic.mod(valueTwo, valueOne));
        nextInstruction();
    }

    private void lineNum(int argument) {
        currentFrame.setLineNumber(argument);
        nextInstruction();
    }

    private void pushConst(int argument) {
        operationStack.push(currentImage.getConstants().get(argument).getValue());
        nextInstruction();
    }

    private void not(int argument) {
        var value = operationStack.pop();
        var newValue = ValueFactory.create(!value.asBoolean());
        operationStack.push(newValue);
        nextInstruction();
    }

    private void and(int argument) {
        var value = operationStack.peek().asBoolean();
        if (!value) {
            jmp(argument);
        } else {
            operationStack.pop();
            nextInstruction();
        }
    }

    private void or(int argument) {
        var value = operationStack.peek().asBoolean();
        if (value) {
            jmp(argument);
        } else {
            operationStack.pop();
            nextInstruction();
        }
    }

    private void jmp(int argument) {
        currentFrame.setInstructionPointer(argument);
    }

    private void jmpFalse(int argument) {
        var condition = operationStack.pop();
        if (condition.asBoolean()) {
            nextInstruction();
        } else {
            currentFrame.setInstructionPointer(argument);
        }
    }

    private void makeBool(int argument) {
        var value = operationStack.pop().asBoolean();
        var newValue = ValueFactory.create(value);
        operationStack.push(newValue);
        nextInstruction();
    }

    // FIXME: ??
    private void argNum(int argument) {
        operationStack.push(ValueFactory.create(argument));
        nextInstruction();
    }

    private void callProc(int argument) {
        methodCall(argument, false);
        currentFrame.setDiscardReturnValue(false);
    }

    private void callFunc(int argument) {
        var discarding = methodCall(argument, true);
        currentFrame.setDiscardReturnValue(discarding);
    }

    private boolean methodCall(int argument, boolean isFunction) {
        var address = currentImage.getMethodRefs().get(argument);
        if (address.getContextId() == scopes.size() - 1) {
            var scope = scopes.get(scopes.size() - 1);
            var sdo = (ScriptDrivenObject) scope.getInstance();
            var lengthSdoMethod = sdo.getContextInfo().getMethods().length;
            if (address.getSymbolId() <= lengthSdoMethod - 1) {
                methodSdoCall(scope, address);
            } else {
                methodScriptCall(address, lengthSdoMethod);
            }
        } else {
            var scope = getScopes().get(address.getContextId());
            methodSdoCall(scope, address);
        }
        // FIXME: учесть, что функция может быть вызвана не в присваивании
        return !isFunction;
    }

    private void setMethodParameters(ExecutionFrame frame, MethodDescriptor methodDescriptor, IValue[] argumentValues) {
        // здесь нужно учесть значения по умолчанию и т.п.
        var variables = createVariables(methodDescriptor.getVariables());
        frame.setLocalVariables(variables);
        var parameters = methodDescriptor.getSignature().getParameters();
        for (var position = 0; position < parameters.length; position++) {

            if (position >= argumentValues.length) {

                var defaultValue = getDefaultArgumentValue(parameters[position]);
                variables[position] = Variable.create(defaultValue, parameters[position].getName());

            } else if (argumentValues[position] instanceof IVariable) {

                if (parameters[position].isByValue()) {
                    variables[position] = Variable.create(argumentValues[position], parameters[position].getName());
                } else {
                    var value = argumentValues[position];
                    variables[position] = VariableReference.create((IVariable) value, parameters[position].getName());
                }

            } else if (argumentValues[position] == null) { // или DataType.NotAValidValue

                var defaultValue = getDefaultArgumentValue(parameters[position]);
                variables[position] = Variable.create(defaultValue, parameters[position].getName());

            } else {

                var variable = variables[position];
                variable.setValue(argumentValues[position]);

            }
        }
    }

    private IValue breakVariableLink(IValue value) {
        return value.getRawValue();
    }


    private void pushFrame(ExecutionFrame frame) {
        callStack.push(frame);
        setFrame(frame);
    }

    private void setFrame(ExecutionFrame frame) {
        currentImage = frame.getImage();
        scopes.set(frame.getModuleLoadIndex(), frame.getModuleScope());
        currentFrame = frame;
    }

    private void loadLoc(int argument) {
        var value = breakVariableLink(operationStack.pop());
        currentFrame.getLocalVariables()[argument].setValue(value);
        nextInstruction();
    }

    private void loadVar(int argument) {
        var address = currentImage.getVariableRefs().get(argument);
        var scope = scopes.get(address.getContextId());
        var value = breakVariableLink(operationStack.pop());
        scope.getVariables()[address.getSymbolId()].setValue(value);
        nextInstruction();
    }

    private void pushLoc(int argument) {
        operationStack.push(currentFrame.getLocalVariables()[argument]);
        nextInstruction();
    }

    private void pushRef(int argument) {
        var address = currentImage.getVariableRefs().get(argument);
        var scope = scopes.get(address.getContextId());
        var reference = VariableReference.createContextPropertyReference(scope.getInstance(),
                address.getSymbolId(), VARIABLE_STACK_NAME);
        operationStack.push(reference);
        nextInstruction();
    }

    private void pushVar(int argument) {
        var address = currentImage.getVariableRefs().get(argument);
        Scope scope;
        if (address.getContextId() == scopes.size() - 1) {
            scope = scopes.get(scopes.size() - 1);
        } else {
            scope = scopes.get(address.getContextId());
        }
        operationStack.push(scope.getVariables()[address.getSymbolId()].getValue());
        nextInstruction();
    }

    private void nextInstruction() {
        currentFrame.setInstructionPointer(currentFrame.getInstructionPointer() + 1);
    }

    private void callContext(RuntimeContext drivenObject, int methodId, MethodInfo methodInfo, IValue[] arguments) {
        if (methodInfo.isFunction()) {
            var result = drivenObject.callAsFunction(methodId, arguments);
            operationStack.push(result);
        } else {
            drivenObject.callAsProcedure(methodId, arguments);
        }
    }

    private void toReturn(int argument) {
        if (currentFrame.isDiscardReturnValue()) {
            operationStack.pop();
        }

        if (currentFrame.isOneTimeCall()) {
            currentFrame.setInstructionPointer(-1);
        } else {
            popFrame();
        }
    }

    private void popFrame() {
        callStack.pop();
        setFrame(callStack.peek());
    }

    private IValue getDefaultArgumentValue(ParameterInfo parameterInfo) {
        if (parameterInfo.hasDefaultValue() && parameterInfo.getDefaultValueIndex() >= 0) {
            return currentImage.getConstants().get(parameterInfo.getDefaultValueIndex()).getValue();
        }
        return ValueFactory.create();
    }

    private void methodSdoCall(Scope scope, SymbolAddress address) {
        var method = scope.getMethods()[address.getSymbolId()];
        int argumentCount = operationStack.pop().asNumber().intValue();

        var factArgumentValues = new IValue[argumentCount];
        for (var index = argumentCount - 1; index >= 0; index--) {
            var value = operationStack.pop();
            factArgumentValues[index] = value;
        }

        var argumentValues = new IValue[method.getParameters().length];
        fillArgumentValueFromFact(method, factArgumentValues, argumentValues);

        callContext(scope.getInstance(), address.getSymbolId(), method, argumentValues);
        nextInstruction();
    }

    private void methodScriptCall(SymbolAddress address, int methodIndexOffset) {
        // уход из зацикливания
        nextInstruction();

        // FIXME: под общую гребенку: хранить в sdo сколько методов из модели, сколько из кода
        var methodDescriptor = currentImage.getMethods().get(address.getSymbolId() - methodIndexOffset);

        int argumentCount = operationStack.pop().asNumber().intValue();
        var argumentValues = new IValue[argumentCount];
        for (var index = argumentCount - 1; index >= 0; index--) {
            var value = operationStack.pop();
            argumentValues[index] = value;
        }

        var frame = new ExecutionFrame();
        frame.setImage(currentImage);
        frame.setModuleLoadIndex(scopes.size() - 1);
        frame.setModuleScope(scopes.get(frame.getModuleLoadIndex()));
        frame.setMethodName(methodDescriptor.getSignature().getName());

        setMethodParameters(frame, methodDescriptor, argumentValues);

        frame.setInstructionPointer(methodDescriptor.getEntry());
        pushFrame(frame);
    }

    private void fillArgumentValueFromFact(MethodInfo methodInfo, IValue[] factArguments, IValue[] arguments) {
        // FIXME: проверять сигнатуру
        for (var index = 0; index < factArguments.length; index++) {
            var argumentValue = factArguments[index];
            var parameter = methodInfo.getParameters()[index];
            if (parameter.isByValue()) {
                arguments[index] = breakVariableLink(argumentValue);
            } else {
                arguments[index] = argumentValue;
            }
        }
    }
}
