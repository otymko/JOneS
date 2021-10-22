/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.format;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class FormatParametersList {

  private static final Pattern intListSeparatorPattern = Pattern.compile("\\D");

  private final Map<String, String> paramList;

  public FormatParametersList(Map<String, String> paramList) {
    this.paramList = paramList;
  }

  public Optional<String> get(List<String> names) {
    return names.stream()
            .map(name -> name.toUpperCase(Locale.ENGLISH))
            .map(paramList::get)
            .filter(Objects::nonNull)
            .findFirst();
  }

  public Locale getLocale(List<String> names) {
    return get(names)
            .map(FormatParametersList::getLocale)
            .orElse(Locale.getDefault());
  }

  private static Locale getLocale(String localeParamValue) {
    return Locale.forLanguageTag(localeParamValue.replace('_', '-'));
  }


  public Optional<Integer> getInt(List<String> names) {
    return get(names)
            .map(FormatParametersList::parseInt);
  }

  public boolean containsKey(List<String> names) {
    return names.stream()
            .map(name -> name.toUpperCase(Locale.ENGLISH))
            .anyMatch(paramList::containsKey);
  }

  public List<Integer> getIntList(List<String> names) {
    return get(names)
            .stream()
            .flatMap(stringValue -> Arrays.stream(intListSeparatorPattern.split(stringValue)))
            .filter(Predicate.not(String::isBlank))
            .map(FormatParametersList::parseInt)
            .collect(Collectors.toList());
  }

  private static int parseInt(String value) {
    String result = value.chars()
            .filter(ch -> Character.isDigit(ch) || ch == '-')
            .mapToObj(ch -> (char) ch)
            .map(String::valueOf)
            .collect(Collectors.joining());

    return result.isBlank() ? 0 : Integer.parseInt(result);
  }
}
