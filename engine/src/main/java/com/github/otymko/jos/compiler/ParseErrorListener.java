/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserBaseListener;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import com.github.otymko.jos.exception.CompilerException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ErrorNodeImpl;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.tree.Trees;

import java.util.Objects;
import java.util.function.Predicate;

public class ParseErrorListener extends BSLParserBaseListener {

  @Override
  public void visitErrorNode(ErrorNode node) {
    var errorNode = (ErrorNodeImpl) node;
    if (errorNode.symbol.getTokenIndex() == -1) {
      var message = String.format("Ошибка разбора исходного кода. %s", node.getText());
      throw new CompilerException(message);
    }
  }

  @Override
  public void enterFile(BSLParser.FileContext ctx) {
    Trees.getDescendants(ctx).stream()
      .filter(Predicate.not(parseTree -> parseTree instanceof TerminalNodeImpl))
      .map(BSLParserRuleContext.class::cast)
      .filter(node -> Objects.nonNull(node.exception))
      .map(this::getErrorToken)
      .forEach(this::throwCompileException);
  }

  private Token getErrorToken(BSLParserRuleContext node) {
    var errorToken = node.exception.getOffendingToken();
    return errorToken.getType() == Token.EOF ? node.getStart() : errorToken;
  }

  private void throwCompileException(Token token) {
    var message = String.format("Ошибка разбора исходного кода в [%d:%d]",
      token.getLine(), token.getCharPositionInLine() + 1);
    throw new CompilerException(message);
  }

}
