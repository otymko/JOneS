package com.github.otymko.jos.compiler.expression;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;

public class ExpressionParser {

  public void parse(BSLParser.ExpressionContext expressionContext) {

    visitExpression(expressionContext);

  }

  public void visitExpression(BSLParser.ExpressionContext ctx) {

    visitMember(ctx.member(0));
    var count = ctx.getChildCount();
    for (var index = 1; index < count; index++) {
      var child = (BSLParserRuleContext) ctx.getChild(index);
      if (child.getRuleIndex() == BSLParser.RULE_operation) {
        visitOperation((BSLParser.OperationContext) child);
      } else { // member
        visitMember((BSLParser.MemberContext) child);
      }
    }

  }

  public void visitMember(BSLParser.MemberContext ctx) {

  }

  public void visitOperation(BSLParser.OperationContext ctx) {

  }
}
