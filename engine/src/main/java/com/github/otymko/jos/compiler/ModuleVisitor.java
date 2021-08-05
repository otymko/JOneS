package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserBaseVisitor;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import com.github.otymko.jos.module.ModuleImageCache;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.runtime.machine.OperationCode;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

public class ModuleVisitor extends BSLParserBaseVisitor<ParseTree> {
  private static final String ENTRY_METHOD = "$entry";
  private final ScriptCompiler compiler;
  private final ModuleImageCache imageCache;
  private MethodDescriptor currentMethodDescriptor;
  private List<Integer> currentCommandReturnInMethod;
  private SymbolScope localScope;

  public ModuleVisitor(ModuleImageCache imageCache, ScriptCompiler compiler) {
    this.imageCache = imageCache;
    this.compiler = compiler;
  }

  @Override
  public ParseTree visitModuleVarDeclaration(BSLParser.ModuleVarDeclarationContext ctx) {
    var variableName = ctx.var_name().getText();
    var variableInfo = new VariableInfo(variableName);
    compiler.getModuleContext().defineVariable(variableInfo);
    imageCache.getVariables().add(variableInfo);
    return ctx;
  }

  @Override
  public ParseTree visitCodeBlock(BSLParser.CodeBlockContext ctx) {
    localScope = new SymbolScope();
    compiler.getModuleContext().pushScope(localScope);
    currentMethodDescriptor = new MethodDescriptor();

    var isBody = false;
    var parent = ctx.parent;
    if (parent.getRuleIndex() == BSLParser.RULE_fileCodeBlock) {
      processFileCodeBlock(ctx);
      isBody = true;
    } else if (parent.getRuleIndex() == BSLParser.RULE_subCodeBlock) {
      processSubCodeBlock(ctx);
    }

    if (currentMethodDescriptor.getEntry() >= 0) {
      fillMethodDescriptorFromScope(currentMethodDescriptor, localScope);
      imageCache.getMethods().add(currentMethodDescriptor);

      var topScopes = compiler.getModuleContext().getScopes().get(0);
      topScopes.getMethods().add(currentMethodDescriptor.getSignature());
      var index = topScopes.getMethods().indexOf(currentMethodDescriptor.getSignature());
      topScopes.getMethodNumbers().put(currentMethodDescriptor.getSignature().getName().toUpperCase(Locale.ENGLISH), index);

      compiler.getModuleContext().popScope(localScope);
      if (isBody) {
        imageCache.setEntryPoint(imageCache.getMethods().size() - 1);
      }
    }

    currentMethodDescriptor = null;
    localScope = null;

    return super.visitCodeBlock(ctx);
  }

  private void processFileCodeBlock(BSLParser.CodeBlockContext codeBlock) {
    var statements = codeBlock.statement();
    if (statements == null) {
      return;
    }

    var methodInfo = new MethodInfo(ENTRY_METHOD, ENTRY_METHOD, false, new ParameterInfo[0], null);
    currentMethodDescriptor.setSignature(methodInfo);

    // загрузить переменные?

    processStatements(statements);
  }

  private void processStatements(List<? extends BSLParser.StatementContext> statements) {
    for (var statement : statements) {
      int numberLint = statement.getStart().getLine();
      addCommand(OperationCode.LineNum, numberLint);
      if (currentMethodDescriptor.getEntry() < 0) {
        currentMethodDescriptor.setEntry(imageCache.getCode().size() - 1);
      }
      processStatement(statement);
    }
  }

  private void processStatement(BSLParser.StatementContext statementContext) {
    if (statementContext.callStatement() != null) {
      processCallStatement(statementContext.callStatement());
    } else if (statementContext.assignment() != null) {
      processAssigment(statementContext.assignment());
    } else if (statementContext.compoundStatement() != null) {
      var compoundStatement = statementContext.compoundStatement();
      if (compoundStatement.returnStatement() != null) {
        processReturnStatement(compoundStatement.returnStatement());
      } else {
        throw new RuntimeException("Not supported");
      }
    } else {
      throw new RuntimeException("Not supported");
    }
  }

  private void processReturnStatement(BSLParser.ReturnStatementContext returnStatementContext) {
    processExpression(returnStatementContext.expression(), new ArrayDeque<>());
    addCommand(OperationCode.MakeRawValue, 0);
    var indexJump = addCommand(OperationCode.Jmp, -1);
    currentCommandReturnInMethod.add(indexJump);
  }

  private void processAssigment(BSLParser.AssignmentContext assignment) {
    var lValue = assignment.lValue();
    var expression = assignment.expression();
    processExpression(expression, new ArrayDeque<>());

    var variableName = lValue.getText();
    var address = compiler.findVariableInContext(variableName);
    if (address != null) {

      if (address.getContextId() == compiler.getModuleContext().getMaxScopeIndex()) {
        addCommand(OperationCode.LoadLoc, address.getSymbolId());
      } else {
        addCommand(OperationCode.LoadVar, address.getSymbolId());
      }

    } else {

      var variableInfo = new VariableInfo(variableName);
      localScope.getVariables().add(variableInfo);
      localScope.getVariableNumbers().put(variableName.toUpperCase(Locale.ENGLISH), localScope.getVariables().indexOf(variableInfo));
      var index = localScope.getVariables().size() - 1;
      addCommand(OperationCode.LoadLoc, index);

    }
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
        throw new RuntimeException("Не поддерживается");
      }
    }

    while (!operators.isEmpty()) {
      var indexCommand = addOperator(operators.pop());
      booleanCommands.add(indexCommand);
    }

    if (booleanExpression) {
      var lastIndexCommand = addCommand(OperationCode.MakeBool, 0);
      booleanCommands.forEach(commandId -> {
        var command = imageCache.getCode().get(commandId);
        command.setArgument(lastIndexCommand);
      });
    }

  }

  private void processUnaryModifier(BSLParser.UnaryModifierContext child, Deque<ExpressionOperator> operators) {
    if (child.NOT_KEYWORD() != null) {
      operators.push(ExpressionOperator.NOT);
    } else if (child.PLUS() != null) {
      operators.push(ExpressionOperator.UNARY_PLUS);
    } else if (child.MINUS() != null) {
      operators.push(ExpressionOperator.UNARY_MINUS);
    } else {
      throw new RuntimeException("Not supported");
    }
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

  private void processMember(BSLParser.MemberContext memberContext, Deque<ExpressionOperator> operators) {
    if (memberContext.constValue() != null) {
      processConstValue(memberContext.constValue());
    } else if (memberContext.expression() != null) {
      // тут скобки `(` \ `)`
      processExpression(memberContext.expression(), new ArrayDeque<>());
    } else if (memberContext.complexIdentifier() != null) {
      processComplexIdentifier(memberContext.complexIdentifier());
    } else {
      throw new RuntimeException("Member not supported");
    }

    if (memberContext.unaryModifier() != null) {
      processUnaryModifier(memberContext.unaryModifier(), operators);
    }

  }

  private void processCallStatement(BSLParser.CallStatementContext callStatement) {
    if (callStatement.globalMethodCall() != null) {
      var paramList = callStatement.globalMethodCall().doCall().callParamList();
      processParamList(paramList);
      addCommand(OperationCode.ArgNum, calcParams(paramList));
      processMethodCall(callStatement.globalMethodCall().methodName(), false);
    } else if (callStatement.accessCall() != null) {
      var identifier = callStatement.IDENTIFIER().getText();
      processIdentifier(identifier);
      processAccessCall(callStatement.accessCall(), false);
    } else {
      throw new RuntimeException("Не реализовано");
    }
  }

  private void processAccessCall(BSLParser.AccessCallContext accessCallContext, boolean ifFunction) {
    var paramList = accessCallContext.methodCall().doCall().callParamList();
    processParamList(paramList);
    addCommand(OperationCode.ArgNum, calcParams(paramList));

    var constant = new ConstantDefinition(ValueFactory.create(accessCallContext.methodCall().methodName().getText()));
    imageCache.getConstants().add(constant);
    var index = imageCache.getConstants().indexOf(constant);

    if (ifFunction) {
      addCommand(OperationCode.ResolveMethodFunc, index);
    } else {
      addCommand(OperationCode.ResolveMethodProc, index);
    }
  }

  private void processIdentifier(String identifier) {
    // FIXME: мракобесие
    var address = compiler.findVariableInContext(identifier);
    if (address == null) {
      // fixme:
      throw new RuntimeException("var not found: " + identifier);
    }

    if (address.getContextId() == compiler.getModuleContext().getMaxScopeIndex()) {
      addCommand(OperationCode.PushLoc, address.getSymbolId());
    } else {
      imageCache.getVariableRefs().add(address);
      addCommand(OperationCode.PushVar, imageCache.getVariableRefs().indexOf(address));
    }
  }

  private void processGlobalStatement(BSLParser.GlobalMethodCallContext globalMethodCall) {
    var paramList = globalMethodCall.doCall().callParamList();
    processParamList(paramList);
    addCommand(OperationCode.ArgNum, calcParams(paramList));
    processMethodCall(globalMethodCall.methodName(), true);
  }

  private int calcParams(BSLParser.CallParamListContext callParamListContext) {
    var count = 0;
    for (var callParam : callParamListContext.callParam()) {
      if (callParam.getChildCount() > 0) {
        count++;
      }
    }
    return count;
  }

  private void processMethodCall(BSLParser.MethodNameContext methodNameContext, boolean isFunction) {
    var methodName = methodNameContext.getText();
    var address = compiler.findMethodInContext(methodName);
    if (address == null) {
      // todo: кричать
      throw new RuntimeException("Метод не найден");
    }
    if (!imageCache.getMethodRefs().contains(address)) {
      imageCache.getMethodRefs().add(address);
    }
    var refs = imageCache.getMethodRefs().indexOf(address);
    if (isFunction) {
      addCommand(OperationCode.CallFunc, refs);
    } else {
      addCommand(OperationCode.CallProc, refs);
    }
  }

  private void processParamList(BSLParser.CallParamListContext paramList) {
    paramList.callParam().forEach(callParamContext -> {
      if (callParamContext.expression() == null) {
        return;
      }
      processExpression(callParamContext.expression(), new ArrayDeque<>());
    });

  }

  private void processComplexIdentifier(BSLParser.ComplexIdentifierContext complexIdentifierContext) {
    if (complexIdentifierContext.globalMethodCall() != null) {
      processGlobalStatement(complexIdentifierContext.globalMethodCall());
      return;
    } else if (complexIdentifierContext.newExpression() != null) {
      processNewExpression(complexIdentifierContext.newExpression());
      return;
    }

    processIdentifier(complexIdentifierContext.IDENTIFIER().getText());

    // проверим, что идет дальше
    if (complexIdentifierContext.modifier() != null) {
      for (var modifier : complexIdentifierContext.modifier()) {

        if (modifier.accessCall() != null) {
          processAccessCall(modifier.accessCall(), true);
        } else if (modifier.accessProperty() != null) {
          throw new RuntimeException("accessProperty");
        } else if (modifier.accessIndex() != null) {
          processExpression(modifier.accessIndex().expression(), new ArrayDeque<>());
          addCommand(OperationCode.PushIndexed, 0);
        } else {
          throw new RuntimeException("Обработка modifier не поддерживается");
        }

      }
    }

  }

  private void processNewExpression(BSLParser.NewExpressionContext newExpressionContext) {
    // TODO: хранить в отдельной стопке, не в контантах?
    var typeName = newExpressionContext.typeName().getText();
    var constant = new ConstantDefinition(ValueFactory.create(typeName));
    imageCache.getConstants().add(constant);
    addCommand(OperationCode.PushConst, imageCache.getConstants().indexOf(constant));

    // TODO:
    var argumentCount = 0;

    addCommand(OperationCode.NewInstance, argumentCount);
  }

  private void processConstValue(BSLParser.ConstValueContext constValue) {
    if (constValue.string() != null) {
      var value = constValue.string().getText();
      var constant = new ConstantDefinition(ValueFactory.create(value));
      imageCache.getConstants().add(constant);
      addCommand(OperationCode.PushConst, imageCache.getConstants().indexOf(constant));
    } else if (constValue.numeric() != null) {
      var value = Integer.parseInt(constValue.numeric().getText());
      var constant = new ConstantDefinition(ValueFactory.create(value));
      imageCache.getConstants().add(constant);
      addCommand(OperationCode.PushConst, imageCache.getConstants().indexOf(constant));
    } else if (constValue.FALSE() != null) {
      var constant = new ConstantDefinition(ValueFactory.create(false));
      imageCache.getConstants().add(constant);
      addCommand(OperationCode.PushConst, imageCache.getConstants().indexOf(constant));
    } else if (constValue.TRUE() != null) {
      var constant = new ConstantDefinition(ValueFactory.create(true));
      imageCache.getConstants().add(constant);
      addCommand(OperationCode.PushConst, imageCache.getConstants().indexOf(constant));
    } else {
      throw new RuntimeException("Constant value not supported");
    }
  }

  private void processSubCodeBlock(BSLParser.CodeBlockContext ctx) {
    currentCommandReturnInMethod = new ArrayList<>();

    var subContext = ctx.parent.parent;
    if (subContext.getRuleIndex() == BSLParser.RULE_procedure) {
      processProcedure(subContext);
    } else {
      processFunction(subContext);
    }

    if (ctx.statement() != null) {
      processStatements(ctx.statement());
    }

    if (currentMethodDescriptor.getSignature().isFunction()) {
      // скрытый возврат
      var constant = new ConstantDefinition(ValueFactory.create());
      imageCache.getConstants().add(constant);
      addCommand(OperationCode.PushConst, imageCache.getConstants().indexOf(constant));
    }

    var indexEndMethod = addReturn();
    currentCommandReturnInMethod.forEach(index -> {
      var command = imageCache.getCode().get(index);
      command.setArgument(indexEndMethod);
    });

    if (currentMethodDescriptor.getEntry() < 0) {
      currentMethodDescriptor.setEntry(imageCache.getCode().size() - 1);
    }
  }

  private ParameterInfo[] buildParameterInfos(BSLParser.ParamListContext paramList) {
    ParameterInfo[] parameterInfos;
    if (paramList != null) {
      int index = 0;
      parameterInfos = new ParameterInfo[paramList.param().size()];
      for (var param : paramList.param()) {
        var parameterName = param.IDENTIFIER().toString();
        parameterInfos[index] = new ParameterInfo(parameterName);
        index++;
      }
    } else {
      parameterInfos = new ParameterInfo[0];
    }
    return parameterInfos;
  }

  private void processFunction(RuleContext ruleContext) {
    var function = (BSLParser.FunctionContext) ruleContext;
    var name = function.funcDeclaration().subName().getText();
    var paramList = function.funcDeclaration().paramList();
    var parameterInfos = buildParameterInfos(paramList);

    addParametersToCurrentScope(parameterInfos);

    var info = new MethodInfo(name, name, true, parameterInfos, null);
    currentMethodDescriptor.setSignature(info);
  }

  private void processProcedure(RuleContext ruleContext) {
    var procedure = (BSLParser.ProcedureContext) ruleContext;
    var name = procedure.procDeclaration().subName().getText();
    var paramList = procedure.procDeclaration().paramList();
    var parameterInfos = buildParameterInfos(paramList);

    addParametersToCurrentScope(parameterInfos);

    var info = new MethodInfo(name, name, false, parameterInfos, null);
    currentMethodDescriptor.setSignature(info);
  }

  private int addCommand(OperationCode operationCode, int argument) {
    var index = imageCache.getCode().size();
    imageCache.getCode().add(new Command(operationCode, argument));
    return index;
  }

  private int addReturn() {
    return addCommand(OperationCode.Return, 0);
  }

  private void fillMethodDescriptorFromScope(MethodDescriptor methodDescriptor, SymbolScope scope) {
    methodDescriptor.getVariables().addAll(scope.getVariables());
  }

  private void addParametersToCurrentScope(ParameterInfo[] parameterInfos) {
    for (var parameterInfo : parameterInfos) {
      var variableInfo = new VariableInfo(parameterInfo.getName());
      var upperName = parameterInfo.getName().toUpperCase(Locale.ENGLISH);
      localScope.getVariables().add(variableInfo);
      localScope.getVariableNumbers().put(upperName, localScope.getVariables().indexOf(variableInfo));
    }
  }

  private int addOperator(ExpressionOperator operator) {
    OperationCode operationCode;
    if (operator == ExpressionOperator.ADD) {
      operationCode = OperationCode.Add;
    } else if (operator == ExpressionOperator.SUB) {
      operationCode = OperationCode.Sub;
    } else if (operator == ExpressionOperator.MUL) {
      operationCode = OperationCode.Mul;
    } else if (operator == ExpressionOperator.DIV) {
      operationCode = OperationCode.Div;
    } else if (operator == ExpressionOperator.UNARY_PLUS) {
      operationCode = OperationCode.Number;
    } else if (operator == ExpressionOperator.UNARY_MINUS) {
      operationCode = OperationCode.Neg;
    } else if (operator == ExpressionOperator.NOT) {
      operationCode = OperationCode.Not;
    } else if (operator == ExpressionOperator.OR) {
      operationCode = OperationCode.Or;
    } else if (operator == ExpressionOperator.AND) {
      operationCode = OperationCode.And;
    } else if (operator == ExpressionOperator.EQUAL) {
      operationCode = OperationCode.Equals;
    } else if (operator == ExpressionOperator.LESS) {
      operationCode = OperationCode.Less;
    } else if (operator == ExpressionOperator.LESS_OR_EQUAL) {
      operationCode = OperationCode.LessOrEqual;
    } else if (operator == ExpressionOperator.GREATER) {
      operationCode = OperationCode.Greater;
    } else if (operator == ExpressionOperator.GREATER_OR_EQUAL) {
      operationCode = OperationCode.GreaterOrEqual;
    } else if (operator == ExpressionOperator.NOT_EQUAL) {
      operationCode = OperationCode.NotEqual;
    } else {
      throw new RuntimeException("Operator not supported");
    }
    return addCommand(operationCode, 0);
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
    } else if (operationContext.boolOperation() != null) {
      var bool = operationContext.boolOperation();
      if (bool.AND_KEYWORD() != null) {
        operator = ExpressionOperator.AND;
      } else if (bool.OR_KEYWORD() != null) {
        operator = ExpressionOperator.OR;
      } else {
        throw new RuntimeException("Not supported operator");
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
        throw new RuntimeException("Not supported operator");
      }
    } else {
      throw new RuntimeException("Not supported operator");
    }
    return operator;
  }

  private boolean isLogicOperator(ExpressionOperator operator) {
    return operator == ExpressionOperator.OR || operator == ExpressionOperator.AND;
  }

}
