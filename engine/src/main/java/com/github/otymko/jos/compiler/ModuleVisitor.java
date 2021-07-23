package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserBaseVisitor;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import com.github.otymko.jos.compiler.expression.Operator;
import com.github.otymko.jos.compiler.image.ModuleImageCache;
import com.github.otymko.jos.context.value.ValueFactory;
import com.github.otymko.jos.vm.Command;
import com.github.otymko.jos.vm.OperationCode;
import com.github.otymko.jos.vm.info.MethodInfo;
import com.github.otymko.jos.vm.info.ParameterInfo;
import com.github.otymko.jos.vm.info.VariableInfo;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

public class ModuleVisitor extends BSLParserBaseVisitor<ParseTree> {
  private static final String ENTRY_METHOD = "$entry";

  private final ScriptCompiler compiler;
  private final ModuleImageCache imageCache;

  private MethodDescriptor currentMethodDescriptor;
  private SymbolScope localScope;

  public ModuleVisitor(ModuleImageCache imageCache, ScriptCompiler compiler) {
    this.imageCache = imageCache;
    this.compiler = compiler;
  }

  @Override
  public ParseTree visitFile(BSLParser.FileContext ctx) {

//    int blockCount = 0;
//    if (!ctx.fileCodeBlock().codeBlock().statement().isEmpty()) {
//      blockCount++;
//    }
//    if (ctx.subs() != null) {
//      blockCount += ctx.subs().sub().size();
//    }
//
//    methods = new MethodDescriptor[blockCount];

    return super.visitFile(ctx);
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
    } else {
      throw new RuntimeException("Not supported");
    }
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

  private void processExpression(BSLParser.ExpressionContext expression, Deque<Operator> operators) {
    for (var index = 0; index < expression.getChildCount(); index++) {
      var child = (BSLParserRuleContext) expression.getChild(index);
      if (child.getRuleIndex() == BSLParser.RULE_member) {
        processMember((BSLParser.MemberContext) child);
      } else if (child.getRuleIndex() == BSLParser.RULE_operation) {
        processOperator((BSLParser.OperationContext) child, operators);
      } else {
        throw new RuntimeException("Не поддерживается");
      }
    }

    while (!operators.isEmpty()) {
      addOperator(operators.pop());
    }
  }

  private void processOperator(BSLParser.OperationContext operationContext, Deque<Operator> operators) {
    var operation = getOperatorByChild(operationContext);
    if (!operators.isEmpty()) {
      Operator operatorFromDeque;
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

  private void processMember(BSLParser.MemberContext memberContext) {
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
  }

  private void processCallStatement(BSLParser.CallStatementContext callStatement) {
    if (callStatement.globalMethodCall() != null) {
      var paramList = callStatement.globalMethodCall().doCall().callParamList();
      processParamList(paramList);
      addCommand(OperationCode.ArgNum, paramList.callParam().size());
      processMethodCall(callStatement.globalMethodCall().methodName());
    } else {

    }
  }

  private void processMethodCall(BSLParser.MethodNameContext methodNameContext) {
    var methodName = methodNameContext.getText();
    var address = compiler.findMethodInContext(methodName);
    if (address == null) {
      // todo: кричать
      throw new RuntimeException("Метод не найден");
    }
    if (!imageCache.getMethodRefs().contains(address)) {
      imageCache.getMethodRefs().add(address);
    }
    addCommand(OperationCode.CallProc, imageCache.getMethodRefs().indexOf(address));
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
    // FIXME: мракобесие
    var identifier = complexIdentifierContext.getText();
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
    } else {

    }
  }

  private void processSubCodeBlock(BSLParser.CodeBlockContext ctx) {
    var subContext = ctx.parent.parent;
    if (subContext.getRuleIndex() == BSLParser.RULE_procedure) {
      processProcedure(subContext);
    } else {
      processFunction(subContext);
    }

    if (ctx.statement() != null) {
      processStatements(ctx.statement());
    }

    // FIXME: ?? а как для функции?
    if (currentMethodDescriptor.getSignature().isFunction()) {

    }
    addReturn();

    if (currentMethodDescriptor.getEntry() < 0) {
      currentMethodDescriptor.setEntry(imageCache.getCode().size() - 1);
    }
  }

  private void processFunction(RuleContext ruleContext) {
    // TODO:
  }

  private void processProcedure(RuleContext ruleContext) {
    var procedure = (BSLParser.ProcedureContext) ruleContext;

    var name = procedure.procDeclaration().subName().getText();
    var paramList = procedure.procDeclaration().paramList();
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

    var info = new MethodInfo(name, "", false, parameterInfos, null);
    currentMethodDescriptor.setSignature(info);
  }

  private void addCommand(OperationCode operationCode, int argument) {
    imageCache.getCode().add(new Command(operationCode, argument));
  }

  private void addReturn() {
    addCommand(OperationCode.Return, 0);
  }

  private void fillMethodDescriptorFromScope(MethodDescriptor methodDescriptor, SymbolScope scope) {
    methodDescriptor.getVariables().addAll(scope.getVariables());
  }

  private void addOperator(Operator operator) {
    OperationCode operationCode;
    if (operator == Operator.ADD) {
      operationCode = OperationCode.Add;
    } else if (operator == Operator.SUB) {
      operationCode = OperationCode.Sub;
    } else if (operator == Operator.MUL) {
      operationCode = OperationCode.Mul;
    } else if (operator == Operator.DIV) {
      operationCode = OperationCode.Div;
    } else {
      throw new RuntimeException("Operator not supported");
    }
    addCommand(operationCode, 0);
  }

  private Operator getOperatorByChild(BSLParser.OperationContext operationContext) {
    Operator operator;
    if (operationContext.PLUS() != null) {
      operator = Operator.ADD;
    } else if (operationContext.MINUS() != null) {
      operator = Operator.SUB;
    } else if (operationContext.MUL() != null) {
      operator = Operator.MUL;
    } else if (operationContext.QUOTIENT() != null) {
      operator = Operator.DIV;
    } else {
      throw new RuntimeException("Not supported operator");
    }
    return operator;
  }

}
