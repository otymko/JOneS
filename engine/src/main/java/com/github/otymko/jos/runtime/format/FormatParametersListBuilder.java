/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.format;

import java.util.HashMap;
import java.util.Map;

final class FormatParametersListBuilder {

  private static final char SINGLE_QUOTE = '\'';
  private static final char DOUBLE_QUOTE = '\"';
  private static final char SPACE = ' ';

  private final String format;

  private int index;
  private String paramName;
  private String paramValue;

  FormatParametersListBuilder(String format) {
    this.format = format;
  }

  public static FormatParametersList build(String params) {
    final var builder = new FormatParametersListBuilder(params);
    return new FormatParametersList(builder.parseParams());
  }

  Map<String, String> parseParams() {
    final Map<String, String> paramList = new HashMap<>();
    index = 0;
    while (readParameterDefinition()) {
      paramList.put(paramName.toUpperCase(), paramValue);
    }
    return paramList;
  }

  private boolean readParameterDefinition() {
    if (!readParameterName()) {
      return false;
    }
    readParameterValue();
    return true;
  }

  private boolean readParameterName() {
    skipWhitespace();
    if (index >= format.length()) {
      return false;
    }
    if (!Character.isLetter(format.charAt(index))) {
      return false;
    }

    final var start = index;

    while (index < format.length()) {
      final var currentChar = format.charAt(index);
      if (Character.isLetterOrDigit(currentChar) || currentChar == '.' || currentChar == '_') {
        index++;
      } else if (currentChar == '=') {
        paramName = format.substring(start, index).trim();
        index++;
        return true;
      } else if (Character.isWhitespace(currentChar)) {
        skipWhitespace();
      } else {
        return false;
      }
    }
    return false;
  }

  private static char getTerminalChar(char c) {
    if (c == DOUBLE_QUOTE) {
      return DOUBLE_QUOTE;
    }
    if (c == SINGLE_QUOTE) {
      return SINGLE_QUOTE;
    }
    return SPACE;
  }

  private char nextChar() {
    if (index + 1 < format.length()) {
      return format.charAt(index + 1);
    }
    return '\0';
  }

  private char currentChar() {
    if (index < format.length()) {
      return format.charAt(index);
    }
    return '\0';
  }

  private void readParameterValue() {

    skipWhitespace();

    paramValue = "";
    if (index >= format.length()) {
      return;
    }

    final var valueBuilder = new StringBuilder();

    final var terminalChar = getTerminalChar(currentChar());
    if (terminalChar != SPACE) {
      index++;
    }

    while (index < format.length()) {

      if (terminalChar == SPACE && currentChar() == ';') {
        break;
      }

      if (currentChar() == terminalChar) {
        if (nextChar() != terminalChar) {
          break;
        }

        index++;
      }

      valueBuilder.append(currentChar());
      index++;

    }

    if (terminalChar != SPACE) {
      index++;
    }

    skipWhitespace();

    if (currentChar() == ';') {
      index++;
    }

    paramValue = valueBuilder.toString();
  }

  private void skipWhitespace() {
    while (Character.isWhitespace(currentChar())) {
      index++;
    }
  }
}
