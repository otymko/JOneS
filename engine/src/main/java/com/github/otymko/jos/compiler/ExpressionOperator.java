/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpressionOperator {
  OR(1, "ИЛИ"),
  AND(2, "И"),
  NOT(3, "НЕ"),

  EQUAL(4, "="),
  LESS(4, "<"),
  LESS_OR_EQUAL(4, "<="),
  GREATER(4, ">"),
  GREATER_OR_EQUAL(4, ">="),
  NOT_EQUAL(4, "<>"),

  ADD(5, "+"),
  SUB(5, "-"),
  MUL(6, "*"),
  DIV(6, "/"),

  UNARY_PLUS(7, "+"),
  UNARY_MINUS(7, "-"),
  OPENED_PARENTHESIS(0, "("),
  CLOSED_PARENTHESIS(0, ")");

  private final int priority;
  private final String text;
}
