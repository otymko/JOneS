/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ValueFormatter {

  private static final String[] BOOLEAN_FALSE = {"БЛ", "BF"};
  private static final String[] BOOLEAN_TRUE = {"БИ", "BT"};
  private static final String[] LOCALE = { "Л", "L" };
  private static final String[] DATE_EMPTY = { "ДП", "DE" };
  private static final String[] DATE_FORMAT = { "ДФ", "DF" };
  private static final String[] DATE_LOCAL_FORMAT = { "ДЛФ", "DLF" };

  public static String format(IValue value, String formatString) {
    final var params = parseParameters(formatString);
    switch (value.getDataType()) {
      case BOOLEAN: return boolFormat(value.asBoolean(), params);
      case DATE: return dateFormat(value.asDate(), params);
      case STRING: return value.asString();
      case NUMBER: return numberFormat(value.asNumber(), params);
    }
    throw MachineException.operationNotSupportedException();
  }

  private static FormatParametersList parseParameters(String format) {
    return new FormatParametersList(format);
  }

  private static String boolFormat(boolean value, FormatParametersList params) {
    final var p = params.get( value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
    if (p == null) {
      return ValueFactory.create(value).asString();
    }
    return p;
  }

  private static String dateFormat(Date value, FormatParametersList params) {
    if (value.equals(DateValue.EMPTY_DATE)) {
      final var emptyDatePresentation = params.get(DATE_EMPTY);
      if (emptyDatePresentation == null) {
        return "";
      }
      return emptyDatePresentation;
    }

    final var locale = params.get(LOCALE);
    final var localDateFormat = params.get(DATE_LOCAL_FORMAT);
    if (localDateFormat != null) {
      return processLocalDateFormat(value, localDateFormat, locale);
    }

    final var commonDateFormat = params.get(DATE_FORMAT);
    if (commonDateFormat != null) {
      return processCommonDateFormat(value, commonDateFormat, locale);
    }

    throw MachineException.operationNotImplementedException();
  }

  private static String processLocalDateFormat(Date value, String localDateFormat, String localeParam) {
    final var DATETIME_RU = "ДВ";
    final var DATETIME_EN = "DT";
    final var DATE_RU = "Д";
    final var DATE_EN = "D";
    final var LONG_DATE_RU = "ДД";
    final var LONG_DATE_EN = "DD";
    final var LONG_DATETIME_RU = "ДДВ";
    final var LONG_DATETIME_EN = "DDT";
    final var TIME_RU = "В";
    final var TIME_EN = "T";

    final var locale = getLocale(localeParam);

    switch (localDateFormat) {

      case DATE_RU:
      case DATE_EN:
        return DateFormat.getDateInstance(DateFormat.SHORT, locale).format(value);

      case TIME_RU:
      case TIME_EN:
        return DateFormat.getTimeInstance(DateFormat.MEDIUM, locale).format(value);

      case DATETIME_RU:
      case DATETIME_EN: {
        // Штатный подход через
        // DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale)
        // даёт формат с запятой между датой и временем
        final var df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        final var tf = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
        return String.format("%s %s", df.format(value), tf.format(value));
      }
      case LONG_DATETIME_RU:
      case LONG_DATETIME_EN: {
        // Штатный подход через
        // DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale)
        // даёт формат с запятой между датой и временем
        final var df = DateFormat.getDateInstance(DateFormat.LONG, locale);
        final var tf = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
        return String.format("%s %s", df.format(value), tf.format(value));
      }

      case LONG_DATE_RU:
      case LONG_DATE_EN:
        return DateFormat.getDateInstance(DateFormat.LONG, locale).format(value);

    }

    throw MachineException.operationNotImplementedException();
  }

  private static String convertToNativeFormat(String param) {
    final var builder = new StringBuilder(param);
    for (var i = 0; i < param.length(); i++) {
      if (param.charAt(i) == 'д')
        builder.setCharAt(i, 'd');

      if (param.charAt(i) == 'М')
        builder.setCharAt(i, 'M');

      if (param.charAt(i) == 'г')
        builder.setCharAt(i, 'y');

      if (param.charAt(i) == 'к')
        builder.setCharAt(i, 'q');

      if (param.charAt(i) == 'ч')
        builder.setCharAt(i, 'h');
      if (param.charAt(i) == 'Ч')
        builder.setCharAt(i, 'H');
      if (param.charAt(i) == 'м')
        builder.setCharAt(i, 'm');
      if (param.charAt(i) == 'с')
        builder.setCharAt(i, 's');

      if (param.charAt(i) == 'в' && i + 1 < param.length() && param.charAt(i + 1) == 'в') {
        builder.setCharAt(i, 't');
        i++;
        builder.setCharAt(i, 't');
      }

      if (param.charAt(i) == 'р') {
        builder.setCharAt(i, 'S');
      }
    }

    return builder.toString();
  }

  private static Locale getLocale(String localeParamValue) {
    if (localeParamValue == null) {
      return Locale.forLanguageTag("ru_RU"); // TODO: вынести в константу
    }
    return Locale.forLanguageTag(localeParamValue.replace('_', '-'));
  }

  private static String processCommonDateFormat(Date value, String localDateFormat, String locale) {
    final var sdf = new SimpleDateFormat(convertToNativeFormat(localDateFormat), getLocale(locale));
    return sdf.format(value);
  }

  private static String numberFormat(BigDecimal value, FormatParametersList params) {
    throw MachineException.operationNotImplementedException();
  }

}
