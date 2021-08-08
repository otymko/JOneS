package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.compiler.MethodDescriptor;
import com.github.otymko.jos.compiler.SymbolAddress;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.Arithmetic;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.ContextInitializer;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.type.TypeFactory;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Экземпляр стековой машины
 */
public class MachineInstance {
  private final Map<OperationCode, Consumer<Integer>> commands = createMachineCommands();

  private final ScriptEngine engine;

  @Getter
  private final List<Scope> scopes = new ArrayList<>();

  private final Stack<IValue> operationStack = new Stack<>();

  private final Stack<ExecutionFrame> callStack = new Stack<>();
  private ExecutionFrame currentFrame;

  private ModuleImage currentImage;

  public MachineInstance(ScriptEngine engine) {
    this.engine = engine;
  }

  public void implementContext(RuntimeContext context) {
    var methods = ContextInitializer.getContextMethods(context.getClass());
    // FIXME: this
    var variables = new Variable[0];
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

    Variable[] variables = createVariables(image.getVariables());
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

  private Variable[] createVariables(List<VariableInfo> variableInfos) {
    Variable[] variables = new Variable[variableInfos.size()];
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
    startExecuting();
  }

  private void startExecuting() {

    while (currentFrame.getInstructionPointer() >= 0
      && currentFrame.getInstructionPointer() < currentImage.getCode().size()) {

      var command = currentImage.getCode().get(currentFrame.getInstructionPointer());
      commands.get(command.getCode()).accept(command.getArgument());

    }

  }

  private Map<OperationCode, Consumer<Integer>> createMachineCommands() {
    Map<OperationCode, Consumer<Integer>> map = new EnumMap<>(OperationCode.class);
    map.put(OperationCode.LineNum, this::lineNum);
    map.put(OperationCode.PushConst, this::pushConst);
    map.put(OperationCode.ArgNum, this::argNum);
    map.put(OperationCode.CallProc, this::callProc);
    map.put(OperationCode.LoadLoc, this::loadLoc);
    map.put(OperationCode.PushLoc, this::pushLoc);
    map.put(OperationCode.PushVar, this::pushVar);
    map.put(OperationCode.LoadVar, this::loadVar);
    map.put(OperationCode.Return, this::toReturn);
    map.put(OperationCode.Add, this::add);
    map.put(OperationCode.Sub, this::sub);
    map.put(OperationCode.Mul, this::mul);
    map.put(OperationCode.Div, this::div);
    map.put(OperationCode.Not, this::not);

    map.put(OperationCode.And, this::and);
    map.put(OperationCode.Or, this::or);
    map.put(OperationCode.MakeBool, this::makeBool);
    map.put(OperationCode.Jmp, this::jmp);

    map.put(OperationCode.Greater, this::greater);
    map.put(OperationCode.GreaterOrEqual, this::greaterOrEqual);
    map.put(OperationCode.Less, this::less);
    map.put(OperationCode.LessOrEqual, this::lessOrEqual);
    map.put(OperationCode.Equals, this::toEquals);
    map.put(OperationCode.NotEqual, this::notEqual);


    map.put(OperationCode.MakeRawValue, this::makeRawValue);
    map.put(OperationCode.CallFunc, this::callFunc);

    map.put(OperationCode.NewInstance, this::newInstance);

    map.put(OperationCode.ResolveMethodProc, this::resolveMethodProc);
    map.put(OperationCode.ResolveMethodFunc, this::resolveMethodFunc);

    map.put(OperationCode.PushIndexed, this::pushIndexed);

    return map;
  }

  private void pushIndexed(int argument) {
    var index = operationStack.pop();
    var context = breakVariableLink(operationStack.pop()); // ???

    if (!(context instanceof IndexAccessor)) {
      throw new RuntimeException("Индексный доступ не доступен");
    }

    var indexAccessor = (IndexAccessor) context;
    var valueRef = indexAccessor.get((int) index.asNumber());
    operationStack.push(valueRef);
    nextInstruction();

  }

  private void resolveMethodCall(int argument) {
    int argumentCount = (int) operationStack.pop().asNumber();
    var argumentValues = new IValue[argumentCount];

    for (var index = argumentCount - 1; index >= 0; index--) {
      var value = operationStack.pop();
      // если по значению BreakVariableLink
      argumentValues[index] = value;
    }

    var instance = (operationStack.pop()).getRawValue();
    if (!(instance instanceof RuntimeContext)) {
      throw new RuntimeException("Это не contextType");
    }

    var context = (RuntimeContext) instance;

    var methodName = currentImage.getConstants().get(argument).getValue().asString();
    var methodId = context.findMethodId(methodName);
    var methodInfo = context.getMethodById(methodId);

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
    // TODO: заполнить аргументы для передачи

    // получим описание типа
    // определим конструктор
    // создадим новый экземпляр
    // закинем в стопку

    var typeName = operationStack.pop().asString();
    var contextInfoOptional = engine.getTypeManager().getContextInfoByName(typeName);
    if (contextInfoOptional.isEmpty()) {
      throw new RuntimeException("Тип не найден");
    }

    var contextInfo = contextInfoOptional.get();
    if (contextInfo == ContextInfo.EMPTY) {
      throw new RuntimeException("Пустой контекст типа");
    }

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

      } else if (argumentValues[position] instanceof Variable) {

        if (parameters[position].isByValue()) {
          variables[position] = Variable.create(argumentValues[position], parameters[position].getName());
        } else {
          var value = argumentValues[position];
          variables[position] = (Variable) value;
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
    int argumentCount = (int) operationStack.pop().asNumber();
    var argumentValues = new IValue[argumentCount];
    for (var index = argumentCount - 1; index >= 0; index--) {
      var value = operationStack.pop();
      argumentValues[index] = value;
    }
    callContext(scope.getInstance(), address.getSymbolId(), method, argumentValues);
    nextInstruction();
  }

  private void methodScriptCall(SymbolAddress address, int methodIndexOffset) {
    // уход из зацикливания
    nextInstruction();

    // FIXME: под общую гребенку: хранить в sdo сколько методов из модели, сколько из кода
    var methodDescriptor = currentImage.getMethods().get(address.getSymbolId() - methodIndexOffset);

    int argumentCount = (int) operationStack.pop().asNumber();
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

}
