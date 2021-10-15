/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import java.util.HashMap;
import java.util.Map;

public class FormatParametersList {

  private final Map<String, String> _paramList = new HashMap<>();
  private  final String format;

  public FormatParametersList(String format) {
    this.format = format;
    ParseParams();
  }

  public String get(String[] names) {
    for (String n : names) {
      if (_paramList.containsKey(n.toUpperCase())) {
        return _paramList.get(n.toUpperCase());
      }
    }
    return null;
  }

  private int index;
  private String paramName;
  private String paramValue;

  private void ParseParams() {
    index = 0;
    while (readParameterDefinition()) {
      _paramList.put(paramName.toUpperCase(), paramValue);
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

  private void readParameterValue() {
    final var SINGLE_QUOTE = '\'';
    final var DOUBLE_QUOTE = '\"';
    final var SPACE = ' ';

    skipWhitespace();

    paramValue = "";
    if (index >= format.length()) {
      return;
    }

    final var sb = new StringBuilder();

    final var terminalChar =
            format.charAt(index) == SINGLE_QUOTE ? SINGLE_QUOTE :
                    format.charAt(index) == DOUBLE_QUOTE ? DOUBLE_QUOTE :
                            SPACE;
    if (terminalChar != SPACE) {
      index++;
    }
    while (index < format.length()) {
      final var c = format.charAt(index);
      if (c == terminalChar) {
        if (index + 1 < format.length()) {
          if (format.charAt(index + 1) == terminalChar) {
            index += 2;
            sb.append(terminalChar);
            continue;
          }
        }
        break;
      } else if (c == ';' && terminalChar == SPACE) {
        break;
      } else {
        sb.append(c);
        index++;
      }
    }

    if (index < format.length()) {
      if (terminalChar != SPACE) {
        index++;
      }
      skipWhitespace();
      if (index < format.length()) {
        if (format.charAt(index) == ';') {
          index++;
        }
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
