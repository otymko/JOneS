/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserBaseVisitor;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import com.github.otymko.jos.exception.CompilerException;
import com.github.otymko.jos.module.ModuleImageCache;
import com.github.otymko.jos.runtime.SymbolType;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.runtime.machine.OperationCode;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import com.github.otymko.jos.util.StringLineCleaner;
import lombok.Data;
import org.antlr.v4.runtime.tree.ParseTree;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * Компилятор модулей.
 */
public class Compiler extends BSLParserBaseVisitor<ParseTree> {
    private static final String ENTRY_METHOD_NAME = "$entry";
    private static final Integer DUMMY_ADDRESS = -1;

    private final ScriptCompiler compiler;
    private final ModuleImageCache imageCache;
    /**
     * Обработка аннотаций
     */
    private final AnnotationProcessing annotationProcessing;
    private final List<Integer> currentCommandReturnInMethod = new ArrayList<>();
    private final Deque<Compiler.NestedLoopInfo> nestedLoops = new ArrayDeque<>();
    private MethodDefinition currentMethodDefinition;
    private SymbolScope localScope;

    private static void checkFactNativeMethodArguments(ParameterInfo[] parameters, int factArguments) {
        if (factArguments > parameters.length) {
            throw CompilerException.tooManyMethodArgumentsException();
        }

        for (var position = factArguments; position < parameters.length; position++) {
            var parameter = parameters[factArguments];
            if (!parameter.hasDefaultValue()) {
                throw CompilerException.tooFewMethodArgumentsException();
            }
        }
    }

    private static String extractStringFromConstValue(BSLParser.StringContext context) {
        String value;
        if (context.multilineString().size() > 0) {
            var joiner = new StringJoiner("\n");
            context.multilineString().get(0).children.forEach(token -> {
                joiner.add(StringLineCleaner.cleanFromPipelineSymbol(token.getText()));
            });
            value = StringLineCleaner.clean(joiner.toString());
        } else {
            value = StringLineCleaner.clean(context.getText());
        }

        return value;
    }

    public Compiler(ModuleImageCache imageCache, ScriptCompiler compiler) {
        this.imageCache = imageCache;
        this.compiler = compiler;
        this.annotationProcessing = new AnnotationProcessing(this);
    }

    @Override
    public ParseTree visitFile(BSLParser.FileContext fileContext) {
        var moduleScope = compiler.getModuleContext().getScopes().get(0);

        var subsContext = fileContext.subs();
        if (subsContext != null) {
            var subs = subsContext.sub();
            for (var sub : subs) {
                var method = createMethodDescriptor(sub);
                // TODO:
                imageCache.getMethods().add(method);
                moduleScope.getMethods().add(method.getSignature());

                var index = moduleScope.getMethods().indexOf(method.getSignature());
                moduleScope.getMethodNumbers().put(method.getSignature().getName().toUpperCase(Locale.ENGLISH), index);
            }
        }

        if (fileContext.fileCodeBlock() != null && !fileContext.fileCodeBlock().codeBlock().statement().isEmpty()) {
            var method = createMethodDescriptorFromBody();
            // TODO:
            imageCache.getMethods().add(method);
            moduleScope.getMethods().add(method.getSignature());

            var index = moduleScope.getMethods().indexOf(method.getSignature());
            moduleScope.getMethodNumbers().put(method.getSignature().getName().toUpperCase(Locale.ENGLISH), index);
        }

        return super.visitFile(fileContext);
    }

    @Override
    public ParseTree visitModuleVar(BSLParser.ModuleVarContext moduleVarContext) {
        // TODO: обработка аннотаций

        var variables = moduleVarContext.moduleVarsList().moduleVarDeclaration();
        for (var variable : variables) {
            var name = variable.var_name().getText();
            var variableInfo = new VariableInfo(name);
            compiler.getModuleContext().defineVariable(variableInfo);
            imageCache.getVariables().add(variableInfo);
        }

        return moduleVarContext;
    }

    @Override
    public ParseTree visitProcedure(BSLParser.ProcedureContext procedure) {
        var methodName = getMethodName(procedure);
        prepareModuleMethod(methodName, false);
        super.visitProcedure(procedure);
        processMethodEnd(false);
        pruneContextModuleMethod();
        return procedure;
    }

    @Override
    public ParseTree visitFunction(BSLParser.FunctionContext function) {
        var methodName = getMethodName(function);
        prepareModuleMethod(methodName, false);
        super.visitFunction(function);
        addHiddenReturnForMethod();
        processMethodEnd(false);
        pruneContextModuleMethod();
        return function;
    }

    @Override
    public ParseTree visitFileCodeBlock(BSLParser.FileCodeBlockContext fileCodeBlock) {
        // TODO
        if (fileCodeBlock.codeBlock().statement().isEmpty()) {
            return fileCodeBlock;
        }

        prepareModuleMethod(ENTRY_METHOD_NAME, true);
        super.visitFileCodeBlock(fileCodeBlock);
        processMethodEnd(true);
        pruneContextModuleMethod();
        return fileCodeBlock;
    }

    @Override
    public ParseTree visitParamList(BSLParser.ParamListContext paramList) {
        return paramList;
    }

    @Override
    public ParseTree visitSubVar(BSLParser.SubVarContext subVarContext) {
        // TODO: обработка аннотаций ?

        var variables = subVarContext.subVarsList().subVarDeclaration();
        for (var variable : variables) {
            var name = variable.var_name().getText();
            var variableInfo = new VariableInfo(name);
            currentMethodDefinition.getVariables().add(variableInfo);

            localScope.getVariables().add(variableInfo);
            localScope.getVariableNumbers().put(name.toUpperCase(Locale.ENGLISH), localScope.getVariables().indexOf(variableInfo));
            // LoadLoc ?
        }

        return subVarContext;
    }

    @Override
    public ParseTree visitCodeBlock(BSLParser.CodeBlockContext codeBlock) {
        return super.visitCodeBlock(codeBlock);
    }

    @Override
    public ParseTree visitFileCodeBlockBeforeSub(BSLParser.FileCodeBlockBeforeSubContext fileCodeBlockBeforeSub) {
        // TODO: выкидывать ошибку, если есть код в codeblocks
        return fileCodeBlockBeforeSub;
    }

    @Override
    public ParseTree visitStatement(BSLParser.StatementContext statement) {
        int numberLint = statement.getStart().getLine();
        addCommand(OperationCode.LINE_NUM, numberLint);
        if (currentMethodDefinition != null && currentMethodDefinition.getEntry() == DUMMY_ADDRESS) {
            currentMethodDefinition.setEntry(imageCache.getCode().size() - 1);
        }
        return super.visitStatement(statement);
    }

    @Override
    public ParseTree visitAssignment(BSLParser.AssignmentContext assignment) {
        var lValue = assignment.lValue();
        var variableName = lValue.IDENTIFIER().getText();

        boolean acceptor = false;
        if (lValue.acceptor() != null) {
            processIdentifier(variableName);
            visitAcceptor(lValue.acceptor());

            acceptor = true;
        }

        var expression = assignment.expression();
        visitExpression(expression);

        if (acceptor) {
            addCommand(OperationCode.ASSIGN_REF, 0);
        } else {
            buildLocalVariable(variableName);
        }

        return assignment;
    }

    @Override
    public ParseTree visitIfStatement(BSLParser.IfStatementContext ifStatement) {
        List<Integer> exits = new ArrayList<>();

        visitExpression(ifStatement.ifBranch().expression());
        var jumpFalse = addCommand(OperationCode.JMP_FALSE, DUMMY_ADDRESS);
        visitCodeBlock(ifStatement.ifBranch().codeBlock());
        exits.add(addCommand(OperationCode.JMP, DUMMY_ADDRESS));

        var alternativeBranches = false;
        for (var elseIfBranches : ifStatement.elsifBranch()) {
            correctCommandArgument(jumpFalse, imageCache.getCode().size());
            addCommand(OperationCode.LINE_NUM, elseIfBranches.getStart().getLine());

            visitExpression(elseIfBranches.expression());

            jumpFalse = addCommand(OperationCode.JMP_FALSE, DUMMY_ADDRESS);

            visitCodeBlock(elseIfBranches.codeBlock());
            exits.add(addCommand(OperationCode.JMP, DUMMY_ADDRESS));
        }

        if (ifStatement.elseBranch() != null) {
            alternativeBranches = true;
            correctCommandArgument(jumpFalse, imageCache.getCode().size());
            addCommand(OperationCode.LINE_NUM, ifStatement.elseBranch().getStart().getLine());

            visitCodeBlock(ifStatement.elseBranch().codeBlock());
        }

        var endIndex = addCommand(OperationCode.LINE_NUM, ifStatement.getStop().getLine());
        if (!alternativeBranches) {
            correctCommandArgument(jumpFalse, endIndex);
        }

        for (var exitIndex : exits) {
            correctCommandArgument(exitIndex, endIndex);
        }

        return ifStatement;
    }

    @Override
    public ParseTree visitWhileStatement(BSLParser.WhileStatementContext whileStatement) {
        // прыжок наверх цикла должен попадать на опкод LineNum
        // поэтому указываем адрес - 1
        var conditionIndex = imageCache.getCode().size() - 1;
        var loopRecord = Compiler.NestedLoopInfo.create(conditionIndex);

        nestedLoops.push(loopRecord);
        processExpression(whileStatement.expression(), new ArrayDeque<>());

        var jumpFalseIndex = addCommand(OperationCode.JMP_FALSE, DUMMY_ADDRESS);

        visitCodeBlock(whileStatement.codeBlock());

        addCommand(OperationCode.JMP, conditionIndex);
        var endLoop = addCommand(OperationCode.NOP, 0);
        correctCommandArgument(jumpFalseIndex, endLoop);
        correctBreakStatements(nestedLoops.pop(), endLoop);

        return whileStatement;
    }

    @Override
    public ParseTree visitForStatement(BSLParser.ForStatementContext forStatement) {
        visitExpression(forStatement.expression().get(0));

        var identifier = forStatement.IDENTIFIER().getText();
        buildLocalVariable(identifier);

        visitExpression(forStatement.expression().get(1));

        addCommand(OperationCode.MAKE_RAW_VALUE);
        addCommand(OperationCode.PUSH_TMP);

        var jumpIndex = addCommand(OperationCode.JMP, DUMMY_ADDRESS);
        var loopStart = addCommand(OperationCode.LINE_NUM, forStatement.getStart().getLine());

        // инкремент
        processIdentifier(identifier);
        addCommand(OperationCode.INC);
        buildLocalVariable(identifier);

        var counterIndex = imageCache.getCode().size();
        processIdentifier(identifier);
        correctCommandArgument(jumpIndex, counterIndex);

        var conditionIndex = addCommand(OperationCode.JMP_COUNTER, DUMMY_ADDRESS);

        var loop = new NestedLoopInfo();
        loop.setStartPoint(loopStart);
        nestedLoops.push(loop);

        visitCodeBlock(forStatement.codeBlock());

        addCommand(OperationCode.JMP, loopStart);

        var loopEnd = addCommand(OperationCode.POP_TMP, 1);
        correctCommandArgument(conditionIndex, loopEnd);
        correctBreakStatements(nestedLoops.pop(), loopEnd);

        return forStatement;
    }

    @Override
    public ParseTree visitForEachStatement(BSLParser.ForEachStatementContext forEachStatement) {
        visitExpression(forEachStatement.expression());

        addCommand(OperationCode.PUSH_ITERATOR);

        var loopStart = addCommand(OperationCode.LINE_NUM, forEachStatement.getStart().getLine());

        addCommand(OperationCode.ITERATOR_NEXT);

        var condition = addCommand(OperationCode.JMP_FALSE, DUMMY_ADDRESS);

        buildLocalVariable(forEachStatement.IDENTIFIER().getText());

        var loop = new NestedLoopInfo();
        loop.setStartPoint(loopStart);
        nestedLoops.push(loop);
        visitCodeBlock(forEachStatement.codeBlock());
        addCommand(OperationCode.JMP, loopStart);

        var loopEnd = addCommand(OperationCode.STOP_ITERATOR);
        correctCommandArgument(condition, loopEnd);
        correctBreakStatements(nestedLoops.pop(), loopEnd);
        return forEachStatement;
    }

    @Override
    public ParseTree visitTryStatement(BSLParser.TryStatementContext tryStatement) {
        var beginTryIndex = addCommand(OperationCode.BEGIN_TRY, DUMMY_ADDRESS);
        visitTryCodeBlock(tryStatement.tryCodeBlock());
        var jmpIndex = addCommand(OperationCode.JMP, DUMMY_ADDRESS);

        var beginHandler = addCommand(OperationCode.LINE_NUM, tryStatement.exceptCodeBlock().getStart().getLine());

        correctCommandArgument(beginTryIndex, beginHandler);

        visitExceptCodeBlock(tryStatement.exceptCodeBlock());

        var endIndex = addCommand(OperationCode.LINE_NUM, tryStatement.getStop().getLine());

        addCommand(OperationCode.END_TRY, 0);
        correctCommandArgument(jmpIndex, endIndex);

        return tryStatement;
    }

    @Override
    public ParseTree visitTryCodeBlock(BSLParser.TryCodeBlockContext tryCodeBlock) {
        pushTryNesting();
        super.visitTryCodeBlock(tryCodeBlock);
        popTryNesting();
        return tryCodeBlock;
    }

    @Override
    public ParseTree visitReturnStatement(BSLParser.ReturnStatementContext returnStatement) {
        super.visitReturnStatement(returnStatement);

        assert currentMethodDefinition != null;

        if (currentMethodDefinition.isBodyMethod()) {
            throw CompilerException.returnStatementOutsideMethod();
        }
        if (currentMethodDefinition.getSignature().isFunction()) {
            addCommand(OperationCode.MAKE_RAW_VALUE, 0);
        }
        var indexJump = addCommand(OperationCode.JMP, DUMMY_ADDRESS);
        currentCommandReturnInMethod.add(indexJump);

        return returnStatement;
    }

    @Override
    public ParseTree visitContinueStatement(BSLParser.ContinueStatementContext continueStatement) {
        exitTryBlocks();
        var loopInfo = nestedLoops.peek();
        // FIXME
        assert loopInfo != null;
        addCommand(OperationCode.JMP, loopInfo.getStartPoint());
        return continueStatement;
    }

    @Override
    public ParseTree visitBreakStatement(BSLParser.BreakStatementContext breakStatement) {
        exitTryBlocks();
        var loopInfo = nestedLoops.peek();
        // FIXME
        assert loopInfo != null;
        var idx = addCommand(OperationCode.JMP, DUMMY_ADDRESS);
        loopInfo.getBreakStatements().add(idx);
        return breakStatement;
    }

    @Override
    public ParseTree visitRaiseStatement(BSLParser.RaiseStatementContext raiseStatement) {

        var expression = raiseStatement.expression();
        if (expression == null) {
            var parent = raiseStatement.getParent();
            var isValidRaise = false;
            while (parent != null
                    && parent.getRuleIndex() != BSLParser.RULE_subCodeBlock
                    && parent.getRuleIndex() != BSLParser.RULE_fileCodeBlock) {

                if (parent.getRuleIndex() == BSLParser.RULE_exceptCodeBlock) {
                    isValidRaise = true;
                    break;
                }
                parent = parent.getParent();
            }

            if (!isValidRaise) {
                throw CompilerException.mismatchedRaiseExpressionException();
            }

            addCommand(OperationCode.RAISE_EXCEPTION, -1);

        } else {
            visitExpression(expression);
            addCommand(OperationCode.RAISE_EXCEPTION, 0);
        }

        return raiseStatement;
    }

    @Override
    public ParseTree visitExecuteStatement(BSLParser.ExecuteStatementContext executeStatement) {
        // TODO
        throw CompilerException.notImplementedException("executeStatement");
    }

    @Override
    public ParseTree visitGotoStatement(BSLParser.GotoStatementContext gotoStatement) {
        // TODO
        throw CompilerException.notImplementedException("gotoStatement");
    }

    @Override
    public ParseTree visitAddHandlerStatement(BSLParser.AddHandlerStatementContext addHandlerStatement) {
        // TODO
        throw CompilerException.notSupportedException();
    }

    @Override
    public ParseTree visitRemoveHandlerStatement(BSLParser.RemoveHandlerStatementContext removeHandlerStatement) {
        // TODO
        throw CompilerException.notSupportedException();
    }

    @Override
    public ParseTree visitWaitStatement(BSLParser.WaitStatementContext waitStatement) {
        // TODO
        throw CompilerException.notSupportedException();
    }

    @Override
    public ParseTree visitCallStatement(BSLParser.CallStatementContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            processIdentifier(ctx.IDENTIFIER().getText());
        }

        processModifier(ctx.modifier());

        if (ctx.globalMethodCall() != null) {
            visitGlobalMethodCall(ctx.globalMethodCall());
        } else if (ctx.accessCall() != null) {
            visitAccessCall(ctx.accessCall());
        }
        return ctx;
    }

    @Override
    public ParseTree visitGlobalMethodCall(BSLParser.GlobalMethodCallContext globalMethodCall) {
        var callStatement = (BSLParser.CallStatementContext) globalMethodCall.getParent();

        var paramList = callStatement.globalMethodCall().doCall().callParamList();
        final var paramCount = processParamList(paramList);
        addCommand(OperationCode.ARG_NUN, paramCount);
        processMethodCall(callStatement.globalMethodCall().methodName(), false);
        return globalMethodCall;
    }

    @Override
    public ParseTree visitAccessCall(BSLParser.AccessCallContext accessCall) {
        processAccessCall(accessCall, false);
        return accessCall;
    }

    @Override
    public ParseTree visitExpression(BSLParser.ExpressionContext expression) {
        processExpression(expression, new ArrayDeque<>());
        return expression;
    }

    @Override
    public ParseTree visitConstValue(BSLParser.ConstValueContext constValue) {
        var constant = getConstantDefinitionByConstValue(constValue, false);
        if (!imageCache.getConstants().contains(constant)) {
            imageCache.getConstants().add(constant);
        }
        addCommand(OperationCode.PUSH_CONST, imageCache.getConstants().indexOf(constant));
        return constValue;
    }

    @Override
    public ParseTree visitComplexIdentifier(BSLParser.ComplexIdentifierContext complexIdentifier) {
        processComplexIdentifier(complexIdentifier);
        return complexIdentifier;
    }

    @Override
    public ParseTree visitAcceptor(BSLParser.AcceptorContext acceptor) {
        processModifier(acceptor.modifier());
        if (acceptor.accessIndex() != null) {
            visitAccessIndex(acceptor.accessIndex());
        } else {
            visitAccessProperty(acceptor.accessProperty());
        }
        return acceptor;
    }

    @Override
    public ParseTree visitAccessIndex(BSLParser.AccessIndexContext ctx) {
        visitExpression(ctx.expression());
        addCommand(OperationCode.PUSH_INDEXED, 0);
        return ctx;
    }

    @Override
    public ParseTree visitAccessProperty(BSLParser.AccessPropertyContext accessProperty) {
        var propertyName = accessProperty.IDENTIFIER().getText();
        var constant = new ConstantDefinition(ValueFactory.create(propertyName));
        if (!imageCache.getConstants().contains(constant)) {
            imageCache.getConstants().add(constant);
        }
        var indexConstant = imageCache.getConstants().indexOf(constant);
        addCommand(OperationCode.RESOLVE_PROP, indexConstant);
        return accessProperty;
    }

    private void processModifier(List<? extends BSLParser.ModifierContext> modifierList) {
        if (modifierList == null) {
            return;
        }
        for (final var modifier : modifierList) {
            if (modifier.accessCall() != null) {
                processAccessCall(modifier.accessCall(), true);
            } else if (modifier.accessProperty() != null) {
                visitAccessProperty(modifier.accessProperty());
            } else if (modifier.accessIndex() != null) {
                visitAccessIndex(modifier.accessIndex());
            } else {
                visitModifier(modifier);
            }
        }
    }

    private MethodDefinition createMethodDescriptor(BSLParser.SubContext subContext) {
        boolean isFunction;
        String methodName;
        BSLParser.ParamListContext parametersList;
        List<? extends BSLParser.AnnotationContext> annotations;

        if (subContext.function() != null) {
            isFunction = true;
            methodName = getMethodName(subContext.function());
            parametersList = getMethodParamListContext(subContext.function());
            annotations = subContext.function().funcDeclaration().annotation();
        } else {
            isFunction = false;
            methodName = getMethodName(subContext.procedure());
            parametersList = getMethodParamListContext(subContext.procedure());
            annotations = subContext.procedure().procDeclaration().annotation();
        }

        var parameterInfos = buildParameterInfos(parametersList);
        var annotationDefinitions = annotationProcessing.getAnnotationsFromContext(annotations);
        var methodInfo = new MethodInfo(methodName, methodName, isFunction, parameterInfos, annotationDefinitions);

        var methodDescriptor = new MethodDefinition();
        methodDescriptor.setSignature(methodInfo);
        return methodDescriptor;
    }

    private MethodDefinition createMethodDescriptorFromBody() {
        var methodInfo = new MethodInfo(ENTRY_METHOD_NAME, ENTRY_METHOD_NAME, false, new ParameterInfo[0],
                new AnnotationDefinition[0]);

        var methodDescriptor = new MethodDefinition();
        methodDescriptor.setSignature(methodInfo);
        methodDescriptor.setBodyMethod(true);
        return methodDescriptor;
    }

    private String getMethodName(BSLParser.FunctionContext function) {
        return function.funcDeclaration().subName().getText();
    }

    private String getMethodName(BSLParser.ProcedureContext procedure) {
        return procedure.procDeclaration().subName().getText();
    }

    private BSLParser.ParamListContext getMethodParamListContext(BSLParser.FunctionContext function) {
        return function.funcDeclaration().paramList();
    }

    private BSLParser.ParamListContext getMethodParamListContext(BSLParser.ProcedureContext procedure) {
        return procedure.procDeclaration().paramList();
    }

    private ParameterInfo[] buildParameterInfos(BSLParser.ParamListContext paramList) {
        ParameterInfo[] parameterInfos;
        if (paramList != null) {
            int index = 0;
            parameterInfos = new ParameterInfo[paramList.param().size()];
            for (var param : paramList.param()) {
                var parameterName = param.IDENTIFIER().toString();

                var builder = ParameterInfo.builder()
                        .name(parameterName)
                        .byValue(param.VAL_KEYWORD() != null);

                if (param.defaultValue() != null) {
                    var constant = getConstantDefinitionByConstValue(param.defaultValue().constValue(), true);
                    imageCache.getConstants().add(constant);
                    var indexConstant = imageCache.getConstants().indexOf(constant);
                    addCommand(OperationCode.PUSH_CONST, indexConstant);

                    builder.hasDefaultValue(true);
                    builder.defaultValueIndex(indexConstant);
                }

                parameterInfos[index] = builder.build();
                index++;
            }
        } else {
            parameterInfos = new ParameterInfo[0];
        }
        return parameterInfos;
    }

    private MethodDefinition getMethodDescriptor(String name) {
        // TODO: поиск в локальной мапе

        //noinspection OptionalGetWithoutIsPresent
        return imageCache.getMethods().stream()
                .filter(methodDescriptor -> methodDescriptor.getSignature().getName().equals(name))
                .findAny()
                .get();
    }

    private int addCommand(OperationCode operationCode, int argument) {
        var index = imageCache.getCode().size();
        imageCache.getCode().add(new Command(operationCode, argument));
        return index;
    }

    private int addCommand(OperationCode operationCode) {
        return addCommand(operationCode, 0);
    }

    private int addReturn() {
        return addCommand(OperationCode.RETURN, 0);
    }

    private void processExpression(BSLParser.ExpressionContext expression, Deque<ExpressionOperator> operators) {
        var booleanExpression = false;
        List<Integer> booleanCommands = new ArrayList<>();
        for (var index = 0; index < expression.getChildCount(); index++) {
            var child = (BSLParserRuleContext) expression.getChild(index);
            if (child.getRuleIndex() == BSLParser.RULE_member) {
                processMember((BSLParser.MemberContext) child, operators);
            } else if (child.getRuleIndex() == BSLParser.RULE_operation) {
                var operation = getOperatorByChild((BSLParser.OperationContext) child);
                if (isLogicOperator(operation)) {
                    booleanExpression = true;
                    var indexCommand = addOperator(operation);
                    booleanCommands.add(indexCommand);
                } else {
                    processOperator(operation, operators);
                }
            } else {
                throw CompilerException.notSupportedException();
            }
        }

        while (!operators.isEmpty()) {
            var indexCommand = addOperator(operators.pop());
            booleanCommands.add(indexCommand);
        }

        if (booleanExpression) {
            var lastIndexCommand = addCommand(OperationCode.MAKE_BOOL, 0);
            booleanCommands.forEach(commandId -> correctCommandArgument(commandId, lastIndexCommand));
        }

    }

    private ExpressionOperator getOperatorByChild(BSLParser.OperationContext operationContext) {
        ExpressionOperator operator;
        if (operationContext.PLUS() != null) {
            operator = ExpressionOperator.ADD;
        } else if (operationContext.MINUS() != null) {
            operator = ExpressionOperator.SUB;
        } else if (operationContext.MUL() != null) {
            operator = ExpressionOperator.MUL;
        } else if (operationContext.QUOTIENT() != null) {
            operator = ExpressionOperator.DIV;
        } else if (operationContext.MODULO() != null) {
            operator = ExpressionOperator.MOD;
        } else if (operationContext.boolOperation() != null) {
            var bool = operationContext.boolOperation();
            if (bool.AND_KEYWORD() != null) {
                operator = ExpressionOperator.AND;
            } else if (bool.OR_KEYWORD() != null) {
                operator = ExpressionOperator.OR;
            } else {
                throw CompilerException.notSupportedExpressionOperatorException(bool.getText());
            }
        } else if (operationContext.compareOperation() != null) {
            var compare = operationContext.compareOperation();
            if (compare.ASSIGN() != null) {
                operator = ExpressionOperator.EQUAL;
            } else if (compare.LESS() != null) {
                operator = ExpressionOperator.LESS;
            } else if (compare.LESS_OR_EQUAL() != null) {
                operator = ExpressionOperator.LESS_OR_EQUAL;
            } else if (compare.GREATER() != null) {
                operator = ExpressionOperator.GREATER;
            } else if (compare.GREATER_OR_EQUAL() != null) {
                operator = ExpressionOperator.GREATER_OR_EQUAL;
            } else if (compare.NOT_EQUAL() != null) {
                operator = ExpressionOperator.NOT_EQUAL;
            } else {
                throw CompilerException.notSupportedExpressionOperatorException(compare.getText());
            }
        } else {
            throw CompilerException.notSupportedExpressionOperatorException(operationContext.getText());
        }
        return operator;
    }

    private void processMember(BSLParser.MemberContext memberContext, Deque<ExpressionOperator> operators) {
        if (memberContext.constValue() != null) {
            visitConstValue(memberContext.constValue());
        } else if (memberContext.expression() != null) {
            // тут скобки `(` \ `)`
            visitExpression(memberContext.expression());
        } else if (memberContext.complexIdentifier() != null) {
            processComplexIdentifier(memberContext.complexIdentifier());
        } else {
            throw CompilerException.notSupportedException();
        }

        if (memberContext.unaryModifier() != null) {
            processUnaryModifier(memberContext.unaryModifier(), operators);
        }

    }

    private int addOperator(ExpressionOperator operator) {
        OperationCode operationCode;
        switch (operator) {
            case ADD:
                operationCode = OperationCode.ADD;
                break;
            case SUB:
                operationCode = OperationCode.SUB;
                break;
            case MUL:
                operationCode = OperationCode.MUL;
                break;
            case DIV:
                operationCode = OperationCode.DIV;
                break;
            case MOD:
                operationCode = OperationCode.MOD;
                break;
            case UNARY_PLUS:
                operationCode = OperationCode.NUMBER;
                break;
            case UNARY_MINUS:
                operationCode = OperationCode.NEG;
                break;
            case NOT:
                operationCode = OperationCode.NOT;
                break;
            case OR:
                operationCode = OperationCode.OR;
                break;
            case AND:
                operationCode = OperationCode.AND;
                break;
            case EQUAL:
                operationCode = OperationCode.EQUALS;
                break;
            case LESS:
                operationCode = OperationCode.LESS;
                break;
            case LESS_OR_EQUAL:
                operationCode = OperationCode.LESS_OR_EQUAL;
                break;
            case GREATER:
                operationCode = OperationCode.GREATER;
                break;
            case GREATER_OR_EQUAL:
                operationCode = OperationCode.GREATER_OR_EQUAL;
                break;
            case NOT_EQUAL:
                operationCode = OperationCode.NOT_EQUAL;
                break;
            default:
                throw CompilerException.notSupportedExpressionOperatorException(operator.getText());
        }
        return addCommand(operationCode, 0);
    }

    private boolean isLogicOperator(ExpressionOperator operator) {
        return operator == ExpressionOperator.OR || operator == ExpressionOperator.AND;
    }

    private void correctCommandArgument(int index, int newValue) {
        var cmd = imageCache.getCode().get(index);
        cmd.setArgument(newValue);
    }

    private void processOperator(ExpressionOperator operation, Deque<ExpressionOperator> operators) {
        if (!operators.isEmpty()) {
            ExpressionOperator operatorFromDeque;
            while (!operators.isEmpty()) {
                operatorFromDeque = operators.peek();
                if (operatorFromDeque.getPriority() >= operation.getPriority()) {
                    addOperator(operators.pop());
                } else {
                    break;
                }
            }
        }
        operators.push(operation);
    }

    private ConstantDefinition getConstantDefinitionByConstValue(BSLParser.ConstValueContext constValue,
                                                                 boolean isDefaultValue) {
        ConstantDefinition constant;
        if (constValue.string() != null) {
            String value = extractStringFromConstValue(constValue.string());
            constant = new ConstantDefinition(ValueFactory.create(value));
        } else if (constValue.numeric() != null) {
            var value = new BigDecimal(constValue.numeric().getText());
            if (isDefaultValue) {
                if (constValue.MINUS() != null) {
                    value = value.negate();
                }
            }
            constant = new ConstantDefinition(ValueFactory.create(value));
        } else if (constValue.FALSE() != null) {
            constant = new ConstantDefinition(ValueFactory.create(false));
        } else if (constValue.TRUE() != null) {
            constant = new ConstantDefinition(ValueFactory.create(true));
        } else if (constValue.UNDEFINED() != null) {
            constant = new ConstantDefinition(ValueFactory.create());
        } else if (constValue.DATETIME() != null) {
            var value = StringLineCleaner.cleanSingleQuote(constValue.DATETIME().getText());
            constant = new ConstantDefinition(ValueFactory.parse(value, DataType.DATE));
        } else if (constValue.NULL() != null) {
            constant = new ConstantDefinition(ValueFactory.createNullValue());
        } else {
            throw CompilerException.notSupportedException();
        }
        return constant;
    }

    private void processComplexIdentifier(BSLParser.ComplexIdentifierContext complexIdentifierContext) {
        if (complexIdentifierContext.globalMethodCall() != null) {
            processGlobalStatement(complexIdentifierContext.globalMethodCall());
            return;
        } else if (complexIdentifierContext.newExpression() != null) {
            processNewExpression(complexIdentifierContext.newExpression());
            return;
        } else if (complexIdentifierContext.ternaryOperator() != null) {
            processTernaryOperator(complexIdentifierContext.ternaryOperator());
            return;
        }

        processIdentifier(complexIdentifierContext.IDENTIFIER().getText());

        // проверим, что идет дальше
        processModifier(complexIdentifierContext.modifier());
    }

    private void processUnaryModifier(BSLParser.UnaryModifierContext child, Deque<ExpressionOperator> operators) {
        if (child.NOT_KEYWORD() != null) {
            operators.push(ExpressionOperator.NOT);
        } else if (child.PLUS() != null) {
            operators.push(ExpressionOperator.UNARY_PLUS);
        } else if (child.MINUS() != null) {
            operators.push(ExpressionOperator.UNARY_MINUS);
        } else {
            throw CompilerException.notSupportedExpressionOperatorException(child.getText());
        }
    }

    private void processGlobalStatement(BSLParser.GlobalMethodCallContext globalMethodCall) {
        var paramList = globalMethodCall.doCall().callParamList();
        var factArguments = processParamList(paramList);

        var identifier = globalMethodCall.methodName().getText();
        var optionalOperationCode = NativeGlobalMethod.getOperationCode(identifier);
        if (optionalOperationCode.isPresent()) {
            var opCode = optionalOperationCode.get();
            if (opCode == OperationCode.MIN || opCode == OperationCode.MAX) {
                if (factArguments == 0) {
                    throw CompilerException.tooFewMethodArgumentsException();
                }
            } else {
                var parameters = NativeGlobalMethod.getMethodParameters(opCode);
                checkFactNativeMethodArguments(parameters, factArguments);
            }
            addCommand(opCode, factArguments);

        } else {

            addCommand(OperationCode.ARG_NUN, factArguments);
            processMethodCall(globalMethodCall.methodName(), true);

        }
    }

    private void processNewExpression(BSLParser.NewExpressionContext newExpressionContext) {
        // TODO: хранить в отдельной стопке, не в контантах?
        var typeName = newExpressionContext.typeName().getText();
        var constant = new ConstantDefinition(ValueFactory.create(typeName));
        if (!imageCache.getConstants().contains(constant)) {
            imageCache.getConstants().add(constant);
        }
        addCommand(OperationCode.PUSH_CONST, imageCache.getConstants().indexOf(constant));

        var argumentCount = 0;
        if (newExpressionContext.doCall() != null) {
            var paramList = newExpressionContext.doCall().callParamList();
            argumentCount = processParamList(paramList);
        }

        addCommand(OperationCode.NEW_INSTANCE, argumentCount);
    }

    /**
     * Обработка:
     * <p>
     * ?(<Логическое выражение>, <Выражение 1>, <Выражение 2>)
     */
    private void processTernaryOperator(BSLParser.TernaryOperatorContext ternaryOperator) {
        var expressions = ternaryOperator.expression();
        if (expressions.size() != 3) {
            throw CompilerException.errorInExpression();
        }

        processExpression(expressions.get(0), new ArrayDeque<>());
        addCommand(OperationCode.MAKE_BOOL);

        var falsePart = addCommand(OperationCode.JMP_FALSE, DUMMY_ADDRESS);

        processExpression(expressions.get(1), new ArrayDeque<>()); // true

        var endPart = addCommand(OperationCode.JMP, DUMMY_ADDRESS);

        correctCommandArgument(falsePart, imageCache.getCode().size());
        processExpression(expressions.get(2), new ArrayDeque<>()); // false

        correctCommandArgument(endPart, imageCache.getCode().size());
    }

    private int processParamList(BSLParser.CallParamListContext paramList) {
        final var callParam = paramList.callParam();
        if (callParam.isEmpty()) {
            return 0;
        }
        if (callParam.size() == 1 && callParam.get(0).expression() == null) {
            return 0;
        }
        int count = 0;
        for (var callParamContext : callParam) {
            if (callParamContext.expression() == null) {
                addCommand(OperationCode.PUSH_DEFAULT_ARG);
            } else {
                processExpression(callParamContext.expression(), new ArrayDeque<>());
            }
            count++;
        }
        return count;
    }

    private void processIdentifier(String identifier) {
        var binding = compiler.findVariableBindingInContext(identifier);
        if (binding == null) {
            throw CompilerException.symbolNotFoundException(identifier);
        }
        var address = binding.getAddress();
        if (binding.getType() == SymbolType.VARIABLE) {
            if (address.getContextId() == compiler.getModuleContext().getMaxScopeIndex()) {
                addCommand(OperationCode.PUSH_LOC, address.getSymbolId());
            } else {
                imageCache.getVariableRefs().add(address);
                addCommand(OperationCode.PUSH_VAR, imageCache.getVariableRefs().indexOf(address));
            }
        } else if (binding.getType() == SymbolType.CONTEXT_PROPERTY) {
            imageCache.getVariableRefs().add(address);
            addCommand(OperationCode.PUSH_REF, imageCache.getVariableRefs().indexOf(address));
        } else {
            throw CompilerException.notSupportedException();
        }
    }

    private void processAccessCall(BSLParser.AccessCallContext accessCallContext, boolean ifFunction) {
        var paramList = accessCallContext.methodCall().doCall().callParamList();
        final var paramCount = processParamList(paramList);
        addCommand(OperationCode.ARG_NUN, paramCount);

        var constant = new ConstantDefinition(ValueFactory.create(accessCallContext.methodCall().methodName().getText()));
        if (!imageCache.getConstants().contains(constant)) {
            imageCache.getConstants().add(constant);
        }
        var index = imageCache.getConstants().indexOf(constant);

        if (ifFunction) {
            addCommand(OperationCode.RESOLVE_METHOD_FUNC, index);
        } else {
            addCommand(OperationCode.RESOLVE_METHOD_PROC, index);
        }
    }

    private void processMethodCall(BSLParser.MethodNameContext methodNameContext, boolean isFunction) {
        var methodName = methodNameContext.getText();
        var address = compiler.findMethodInContext(methodName);
        if (address == null) {
            throw CompilerException.methodNotFoundException(methodName);
        }
        if (!imageCache.getMethodRefs().contains(address)) {
            imageCache.getMethodRefs().add(address);
        }
        var refs = imageCache.getMethodRefs().indexOf(address);
        if (isFunction) {
            addCommand(OperationCode.CALL_FUNC, refs);
        } else {
            addCommand(OperationCode.CALL_PROC, refs);
        }
    }

    private void correctBreakStatements(Compiler.NestedLoopInfo loop, int endLoop) {
        for (var breakCmdIndex : loop.breakStatements) {
            correctCommandArgument(breakCmdIndex, endLoop);
        }
    }

    private void exitTryBlocks() {
        assert nestedLoops.peek() != null;
        var tryBlocks = nestedLoops.peek().getTryNesting();
        if (tryBlocks > 0) {
            addCommand(OperationCode.EXIT_TRY, tryBlocks);
        }
    }

    private void pushTryNesting() {
        if (!nestedLoops.isEmpty()) {
            nestedLoops.peek().setTryNesting(nestedLoops.peek().getTryNesting() + 1);
        }
    }

    private void popTryNesting() {
        if (!nestedLoops.isEmpty()) {
            nestedLoops.peek().setTryNesting(nestedLoops.peek().getTryNesting() - 1);
        }
    }

    private void addParametersToCurrentScope(ParameterInfo[] parameterInfos) {
        for (var parameterInfo : parameterInfos) {
            var variableInfo = new VariableInfo(parameterInfo.getName());
            var upperName = parameterInfo.getName().toUpperCase(Locale.ENGLISH);
            localScope.getVariables().add(variableInfo);
            localScope.getVariableNumbers().put(upperName, localScope.getVariables().indexOf(variableInfo));
        }
    }

    private void prepareModuleMethod(String methodName, boolean isBodyMethod) {
        currentMethodDefinition = getMethodDescriptor(methodName);

        localScope = new SymbolScope();
        compiler.getModuleContext().pushScope(localScope);

        if (!isBodyMethod) {
            addParametersToCurrentScope(currentMethodDefinition.getSignature().getParameters());
        }
    }

    private void pruneContextModuleMethod() {
        currentCommandReturnInMethod.clear();
        currentMethodDefinition = null;
        compiler.getModuleContext().popScope(localScope);
        localScope = null;
    }

    private void processMethodEnd(boolean isBody) {
        if (isBody) {
            if (currentMethodDefinition.getEntry() >= 0) {
                imageCache.setEntryPoint(imageCache.getMethods().size() - 1);
            }
        } else {
            var indexEndMethod = addReturn();
            currentCommandReturnInMethod.forEach(index -> correctCommandArgument(index, indexEndMethod));
            if (currentMethodDefinition.getEntry() == DUMMY_ADDRESS) {
                currentMethodDefinition.setEntry(indexEndMethod);
            }
        }

        currentMethodDefinition.getVariables().addAll(localScope.getVariables());
    }

    private void addHiddenReturnForMethod() {
        // скрытый возврат Неопределено
        var constant = new ConstantDefinition(ValueFactory.create());
        imageCache.getConstants().add(constant);
        addCommand(OperationCode.PUSH_CONST, imageCache.getConstants().indexOf(constant));
    }

    private void buildLocalVariable(String variableName) {
        var address = compiler.findVariableInContext(variableName);
        if (address == null) {

            var variableInfo = new VariableInfo(variableName);
            localScope.getVariables().add(variableInfo);
            localScope.getVariableNumbers().put(variableName.toUpperCase(Locale.ENGLISH), localScope.getVariables().indexOf(variableInfo));
            var index = localScope.getVariables().size() - 1;
            addCommand(OperationCode.LOAD_LOC, index);

        } else {
            if (address.getContextId() == compiler.getModuleContext().getMaxScopeIndex()) {
                addCommand(OperationCode.LOAD_LOC, address.getSymbolId());
            } else {
                imageCache.getVariableRefs().add(address);
                addCommand(OperationCode.LOAD_VAR, imageCache.getVariableRefs().indexOf(address));
            }
        }
    }

    // TODO: вынести в хелпер
    public int getConstantIndexByValue(BSLParser.ConstValueContext constValue, boolean isDefaultValue) {
        var constant = getConstantDefinitionByConstValue(constValue, isDefaultValue);
        return addConstant(constant);
    }

    public int getConstantIndexByIdentifier(String identifier) {
        var constant = new ConstantDefinition(ValueFactory.create(identifier));
        return addConstant(constant);
    }

    public int addConstant(ConstantDefinition constant) {
        if (!imageCache.getConstants().contains(constant)) {
            imageCache.getConstants().add(constant);
        }
        return imageCache.getConstants().indexOf(constant);
    }

    @Data
    private static class NestedLoopInfo {
        private int startPoint = DUMMY_ADDRESS;
        private List<Integer> breakStatements = new ArrayList<>();
        private int tryNesting = 0;

        private NestedLoopInfo() {
            // none
        }

        public static Compiler.NestedLoopInfo create() {
            return new Compiler.NestedLoopInfo();
        }

        public static Compiler.NestedLoopInfo create(int startIndex) {
            var loop = new Compiler.NestedLoopInfo();
            loop.setStartPoint(startIndex);
            return loop;
        }
    }
}
