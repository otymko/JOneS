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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ValueFormatter {

  private static final String[] BOOLEAN_FALSE = {"БЛ", "BF"};
  private static final String[] BOOLEAN_TRUE = {"БИ", "BT"};
  private static final String[] LOCALE = { "Л", "L" };
  private static final String[] NUM_MAX_SIZE = { "ЧЦ", "ND" };
  private static final String[] NUM_DECIMAL_SIZE = { "ЧДЦ", "NFD" };
  private static final String[] NUM_FRACTION_DELIMITER = { "ЧРД", "NDS" };
  private static final String[] NUM_DECIMAL_SHIFT = { "ЧС", "NS" };
  private static final String[] NUM_GROUPS_DELIMITER = { "ЧРГ", "NGS" };
  private static final String[] NUM_ZERO_APPEARANCE = { "ЧН", "NZ" };
  private static final String[] NUM_GROUPING = { "ЧГ", "NG" };
  private static final String[] NUM_LEADING_ZERO = { "ЧВН", "NLZ" };
  private static final String[] NUM_NEGATIVE_APPEARANCE = { "ЧО", "NN" };
  private static final String[] DATE_EMPTY = { "ДП", "DE" };
  private static final String[] DATE_FORMAT = { "ДФ", "DF" };
  private static final String[] DATE_LOCAL_FORMAT = { "ДЛФ", "DLF" };

  private static final String DATETIME_RU = "ДВ";
  private static final String DATETIME_EN = "DT";
  private static final String DATE_RU = "Д";
  private static final String DATE_EN = "D";
  private static final String LONG_DATE_RU = "ДД";
  private static final String LONG_DATE_EN = "DD";
  private static final String LONG_DATETIME_RU = "ДДВ";
  private static final String LONG_DATETIME_EN = "DDT";
  private static final String TIME_RU = "В";
  private static final String TIME_EN = "T";

  private static final Map<Character, Character> dateNativeFormatMap = new HashMap<>();


  private ValueFormatter() {}

  public static String format(IValue value, String formatString) {
    final var params = parseParameters(formatString);
    switch (value.getDataType()) {
      case BOOLEAN: return boolFormat(value.asBoolean(), params);
      case DATE: return dateFormat(value.asDate(), params);
      case STRING: return value.asString();
      case NUMBER: return numberFormat(value.asNumber(), params);
      default:
        throw MachineException.operationNotSupportedException();
    }
  }

  private static FormatParametersList parseParameters(String format) {
    return new FormatParametersList(format);
  }

  private static String boolFormat(boolean value, FormatParametersList params) {
    final var presentation = params.get( value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
    return presentation.orElse(ValueFactory.create(value).asString());
  }

  private static String dateFormat(Date value, FormatParametersList params) {
    if (DateValue.isEmpty(value)) {
      final var emptyDatePresentation = params.get(DATE_EMPTY);
      return emptyDatePresentation.orElse("");
    }

    final var locale = params.getLocale(LOCALE);
    final var localDateFormat = params.get(DATE_LOCAL_FORMAT);
    if (localDateFormat.isPresent()) {
      return processLocalDateFormat(value, localDateFormat.get(), locale);
    }

    final var commonDateFormat = params.get(DATE_FORMAT);
    if (commonDateFormat.isPresent()) {
      return processCommonDateFormat(value, commonDateFormat.get(), locale);
    }

    return processLocalDateFormat(value, DATETIME_EN, locale);
  }

  private static String processLocalDateFormat(Date value, String localDateFormat, Locale locale) {

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

      default:
        throw MachineException.operationNotImplementedException();
    }
  }

  private static String convertToNativeFormat(String param) {
    final var builder = new StringBuilder(param);

    int i = 0;
    while (i < param.length()) {

      if (param.charAt(i) == 'в' && i + 1 < param.length() && param.charAt(i + 1) == 'в') {
        builder.setCharAt(i, 't');
        i++;
        builder.setCharAt(i, 't');
      }

      if (dateNativeFormatMap.containsKey(param.charAt(i))) {
        builder.setCharAt(i, dateNativeFormatMap.get(param.charAt(i)));
      }

      i++;
    }

    return builder.toString();
  }

  private static String processCommonDateFormat(Date value, String localDateFormat, Locale locale) {
    final var sdf = new SimpleDateFormat(convertToNativeFormat(localDateFormat), locale);
    return sdf.format(value);
  }

  private static String numberFormat(BigDecimal value, FormatParametersList params) {

    final var nf = new NumberFormatter(params.getLocale(LOCALE));

    params.get(NUM_ZERO_APPEARANCE).ifPresent(nf::setZeroAppearance);
    params.getInt(NUM_DECIMAL_SIZE).ifPresent(nf::setDecimalSize);
    params.get(NUM_LEADING_ZERO).ifPresent(v -> nf.setLeadingZeroes(true));
    params.getInt(NUM_MAX_SIZE).ifPresent(nf::setMaxSize);
    params.get(NUM_FRACTION_DELIMITER).ifPresent(nf::setFractionDelimiter);
    params.getIntList(NUM_GROUPING).ifPresent(nf::setGroupingSize);
    params.getInt(NUM_NEGATIVE_APPEARANCE).ifPresent(nf::setNegativeAppearance);
    params.getInt(NUM_DECIMAL_SHIFT).ifPresent(nf::setDecimalShift);
    params.get(NUM_GROUPS_DELIMITER).ifPresent(nf::setGroupDelimiter);

    return nf.format(value);
  }

  static {
    dateNativeFormatMap.put('д', 'd');
    dateNativeFormatMap.put('М', 'M');
    dateNativeFormatMap.put('г', 'y');
    dateNativeFormatMap.put('к', 'q');
    dateNativeFormatMap.put('ч', 'h');
    dateNativeFormatMap.put('Ч', 'H');
    dateNativeFormatMap.put('м', 'm');
    dateNativeFormatMap.put('с', 's');
    dateNativeFormatMap.put('р', 'S');
  }

}
