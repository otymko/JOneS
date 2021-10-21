/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class FormatParametersList {

  private static final char SINGLE_QUOTE = '\'';
  private static final char DOUBLE_QUOTE = '\"';
  private static final char SPACE = ' ';

  private final Map<String, String> paramList = new HashMap<>();
  private final String format;

  private int index;
  private String paramName;
  private String paramValue;

  public FormatParametersList(String format) {
    this.format = format;
    parseParams();
  }

  public Optional<String> get(String[] names) {
    for (String name : names) {
      if (paramList.containsKey(name.toUpperCase())) {
        return Optional.of(paramList.get(name.toUpperCase()));
      }
    }
    return Optional.empty();
  }

  public Locale getLocale(String[] names) {
return get(names)
      .map(this::getLocale)
      .orElse(Locale.getDefault());
  }

  private static Locale getLocale(String localeParamValue) {
    return Locale.forLanguageTag(localeParamValue.replace('_', '-'));
  }


  public Optional<Integer> getInt(String[] names) {
    return get(names)
        .map(stringValue -> parseInt(stringValue));
  }

  public Optional<List<Integer>> getIntList(String[] names) {
    get(names)
      .stream()
      .flatMap(stringValue -> Arrays.stream(stringValue.split("\\D")))
      .filter(Predicate.not(String::isBlank))
      .map(this::parseInt)
      .collect(Collectors.toList());
  }

  private static int parseInt(String value) {
    String result = value.chars()
      .filter(ch -> Character.isDigit(ch) || ch == '-')
      .mapToObj(ch -> (char)ch)
      .map(String::valueOf)
      .collect(Collectors.joining());
      
      return Integer.parseInt(result)
  }

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
