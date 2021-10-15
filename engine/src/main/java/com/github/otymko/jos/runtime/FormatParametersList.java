/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import java.util.HashMap;
import java.util.Map;

public class FormatParametersList {

  final static Character SINGLE_QUOTE = '\'';
  final static Character DOUBLE_QUOTE = '\"';
  final static Character SPACE = ' ';

  private final Map<String, String> paramList = new HashMap<>();
  private  final String format;

  public FormatParametersList(String format) {
    this.format = format;
    parseParams();
  }

  public String get(String[] names) {
    for (String n : names) {
      if (paramList.containsKey(n.toUpperCase())) {
        return paramList.get(n.toUpperCase());
      }
    }
    return null;
  }

  private int index;
  private String paramName;
  private String paramValue;

  private void parseParams() {
    index = 0;
    while (readParameterDefinition()) {
      paramList.put(paramName.toUpperCase(), paramValue);
    }
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
      final var c = format.charAt(index);
      if (Character.isLetterOrDigit(c) || c == '.' || c == '_') {
        index++;
      } else if (c == '=') {
        paramName = format.substring(start, index).trim();
        index++;
        return true;
      } else if (Character.isWhitespace(c)) {
        skipWhitespace();
      } else {
        return false;
      }
    }
    return false;
  }

  private static Character getTerminalChar(Character c) {
    if (c.equals(DOUBLE_QUOTE)) return DOUBLE_QUOTE;
    if (c.equals(SINGLE_QUOTE)) return SINGLE_QUOTE;
    return SPACE;
  }

  private void readParameterValue() {

    skipWhitespace();

    paramValue = "";
    if (index >= format.length()) {
      return;
    }

    final var sb = new StringBuilder();

    final var terminalChar = getTerminalChar(format.charAt(index));
    if (!terminalChar.equals(SPACE)) {
      index++;
    }
    while (index < format.length()) {
      final var c = format.charAt(index);
      if (c == terminalChar) {
        if (index + 1 < format.length()
          && format.charAt(index + 1) == terminalChar) {
            index += 2;
            sb.append(terminalChar);
            continue;
          }

        break;
      } else if (c == ';' && terminalChar.equals(SPACE)) {
        break;
      } else {
        sb.append(c);
        index++;
      }
    }

    if (index < format.length()) {
      if (!terminalChar.equals(SPACE)) {
        index++;
      }
      skipWhitespace();
      if (index < format.length()
        && format.charAt(index) == ';') {
          index++;
        }

    }

    paramValue = sb.toString();
  }

  private void skipWhitespace() {
    while (index < format.length()) {
      if (!Character.isWhitespace(format.charAt(index))) {
        break;
      }
      index++;
    }
  }
}
