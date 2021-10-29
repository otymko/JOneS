/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.format;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.MessageResource;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Класс с методами форматирования значений.
 */
@UtilityClass
public final class ValueFormatter {

  private static final List<String> BOOLEAN_FALSE = List.of("БЛ", "BF");
  private static final List<String> BOOLEAN_TRUE = List.of("БИ", "BT");
  private static final List<String> LOCALE = List.of( "Л", "L");
  private static final List<String> NUM_MAX_SIZE = List.of( "ЧЦ", "ND");
  private static final List<String> NUM_DECIMAL_SIZE = List.of( "ЧДЦ", "NFD");
  private static final List<String> NUM_FRACTION_DELIMITER = List.of( "ЧРД", "NDS");
  private static final List<String> NUM_DECIMAL_SHIFT = List.of( "ЧС", "NS");
  private static final List<String> NUM_GROUPS_DELIMITER = List.of( "ЧРГ", "NGS");
  private static final List<String> NUM_ZERO_APPEARANCE = List.of( "ЧН", "NZ");
  private static final List<String> NUM_GROUPING = List.of( "ЧГ", "NG");
  private static final List<String> NUM_LEADING_ZERO = List.of( "ЧВН", "NLZ");
  private static final List<String> NUM_NEGATIVE_APPEARANCE = List.of( "ЧО", "NN");
  private static final List<String> DATE_EMPTY = List.of( "ДП", "DE");
  private static final List<String> DATE_FORMAT = List.of( "ДФ", "DF");
  private static final List<String> DATE_LOCAL_FORMAT = List.of( "ДЛФ", "DLF");

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

  /**
   * Возвращает текстовое представление значения с учетом переданного формата.
   * В качестве первого параметра может принимать Дату, Булево, Строку и Числа.
   * Если переданы другие типы, будет вызвано исключение.
   *
   * @param value        Значение, для которого необходимо сформировать представление
   * @param formatString Параметры форматирования значения
   * @return Текстовое представление значения
   */
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
    return FormatParametersListBuilder.build(format);
  }

  private static String boolFormat(boolean value, FormatParametersList params) {
    if (value) {
      return params.get(BOOLEAN_TRUE).orElse(
              Resources.getResourceString(MessageResource.DEFAULT_TRUE_PRESENTATION));
    }
    return params.get(BOOLEAN_FALSE).orElse(
            Resources.getResourceString(MessageResource.DEFAULT_FALSE_PRESENTATION));
  }

  private static String dateFormat(Date value, FormatParametersList params) {
    if (DateValue.isEmpty(value)) {
      final var emptyDatePresentation = params.get(DATE_EMPTY);
      return emptyDatePresentation.orElse("");
    }

    final var locale = params.getLocale(LOCALE);
    final var localDateFormat = params.get(DATE_LOCAL_FORMAT);

    final var commonDateFormat = params.get(DATE_FORMAT);
    if (commonDateFormat.isPresent()) {
      return processCommonDateFormat(value, commonDateFormat.get(), locale);
    }

    return processLocalDateFormat(value, localDateFormat.orElse(""), locale);
  }

  private static String processLocalDateFormat(Date value, String localDateFormat, Locale locale) {

    switch (localDateFormat) {

      case DATE_RU:
      case DATE_EN: {
        final var pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                        FormatStyle.SHORT,
                        null,
                        IsoChronology.INSTANCE,
                        locale).
                replaceAll("y+", "yyyy");
        final var dateFormatter = DateTimeFormatter.ofPattern(pattern)
                .withLocale(locale)
                .withZone(ZoneId.systemDefault());

        return dateFormatter.format(value.toInstant());
      }

      case TIME_RU:
      case TIME_EN:
        return DateFormat.getTimeInstance(DateFormat.MEDIUM, locale).format(value);

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
      case DATETIME_RU:
      case DATETIME_EN: {
        // Штатный подход через
        // DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale)
        // даёт формат с запятой между датой и временем
        return String.format("%s %s",
                processLocalDateFormat(value, DATE_EN, locale),
                processLocalDateFormat(value, TIME_EN, locale));
      }
    }
  }

  private static String convertToNativeFormat(String jonesFormat) {
    final var nativeFormatBuilder = new StringBuilder(jonesFormat);

    int i = 0;
    while (i < jonesFormat.length()) {

      if (jonesFormat.charAt(i) == 'в' && i + 1 < jonesFormat.length() && jonesFormat.charAt(i + 1) == 'в'
      || jonesFormat.charAt(i) == 't' && i + 1 < jonesFormat.length() && jonesFormat.charAt(i + 1) == 't') {
        nativeFormatBuilder.setCharAt(i, 'a');
        i++;
        nativeFormatBuilder.delete(i, i + 1);
      }

      if (dateNativeFormatMap.containsKey(jonesFormat.charAt(i))) {
        nativeFormatBuilder.setCharAt(i, dateNativeFormatMap.get(jonesFormat.charAt(i)));
      }

      i++;
    }

    return nativeFormatBuilder.toString();
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
    if (params.containsKey(NUM_GROUPING)) {
      nf.setGroupingSize(params.getIntList(NUM_GROUPING));
    }
    params.getInt(NUM_NEGATIVE_APPEARANCE).ifPresent(nf::setNegativeAppearance);
    params.getInt(NUM_DECIMAL_SHIFT).ifPresent(nf::setDecimalShift);
    params.get(NUM_GROUPS_DELIMITER).ifPresent(nf::setGroupDelimiter);

    return nf.format(value);
  }

}
