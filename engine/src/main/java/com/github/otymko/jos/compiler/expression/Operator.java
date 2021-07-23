package com.github.otymko.jos.compiler.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Operator {
  ADD(5, "+"),
  SUB(5, "-"),
  MUL(6, "*"),
  DIV(6, "/"),
  UNARY_ADD(7, "+"),
  UNARY_SUB(7, "-"),
  /**
   * Открывающая круглая скобка
   */
  OPENED_PARENTHESIS(0, "("),
  /**
   * Закрывающая круглая скобка
   */
  CLOSED_PARENTHESIS(0, ")");

  private final int priority;
  private final String text;
}
