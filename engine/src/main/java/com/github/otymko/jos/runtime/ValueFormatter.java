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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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

  public static String format(IValue value, String formatString) {
    final var params = parseParameters(formatString);
    String formattedValue = null;
    switch (value.getDataType()) {
      case BOOLEAN: formattedValue = boolFormat(value.asBoolean(), params); break;
      case DATE: formattedValue = dateFormat(value.asDate(), params); break;
      case STRING: formattedValue = value.asString(); break;
      case NUMBER: formattedValue = numberFormat(value.asNumber(), params); break;
      default:
        throw MachineException.operationNotSupportedException();
    }
    if (formattedValue == null) return value.asString();
    return formattedValue;
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
    if (DateValue.isEmpty(value)) {
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

    return null;
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

      default:
        throw MachineException.operationNotImplementedException();
    }
  }

  private static String convertToNativeFormat(String param) {
    final var builder = new StringBuilder(param);
    final var nativeMap = new HashMap<Character, Character>();
    nativeMap.put('д', 'd');
    nativeMap.put('М', 'M');
    nativeMap.put('г', 'y');
    nativeMap.put('к', 'q');
    nativeMap.put('ч', 'h');
    nativeMap.put('Ч', 'H');
    nativeMap.put('м', 'm');
    nativeMap.put('с', 's');
    nativeMap.put('р', 'S');

    int i = 0;
    while (i < param.length()) {

      if (param.charAt(i) == 'в' && i + 1 < param.length() && param.charAt(i + 1) == 'в') {
        builder.setCharAt(i, 't');
        i++;
        builder.setCharAt(i, 't');
      }

      if (nativeMap.containsKey(param.charAt(i))) {
        builder.setCharAt(i, nativeMap.get(param.charAt(i)));
      }

      i++;
    }

    return builder.toString();
  }

  private static Locale getLocale(String localeParamValue) {
    if (localeParamValue == null) {
      return Locale.getDefault();
    }
    return Locale.forLanguageTag(localeParamValue.replace('_', '-'));
  }

  private static String processCommonDateFormat(Date value, String localDateFormat, String locale) {
    final var sdf = new SimpleDateFormat(convertToNativeFormat(localDateFormat), getLocale(locale));
    return sdf.format(value);
  }

  private static int parseInt(String param) {
    final var sb = new StringBuilder();
    for (var c : param.toCharArray()) {
      if (Character.isDigit(c) || c == '-')
        sb.append(c);
    }

    if (sb.length() == 0) {
      return 0;
    }

    return Integer.parseInt(sb.toString());
  }

  private static String numberFormat(BigDecimal value, FormatParametersList params) {

    final var nf = new NumberFormatter(getLocale(params.get(LOCALE)));

    final var nz = params.get(NUM_ZERO_APPEARANCE);
    if (nz != null) {
      nf.setZeroAppearance(nz.equals("") ? "0" : nz);
    }

    final var nfd = params.get(NUM_DECIMAL_SIZE);
    if (nfd != null && !nfd.isBlank()) {
      final var i_nfd = parseInt(nfd);
      if (i_nfd >= 0)
        nf.setDecimalSize(i_nfd);
    }

    nf.setLeadingZeroes(params.get(NUM_LEADING_ZERO) != null);

    final var nd = params.get(NUM_MAX_SIZE);
    if (nd != null) {
      final var i_nd = nd.isBlank() ? 0 : parseInt(nd);
      if (i_nd >= 0)
        nf.setMaxSize(i_nd);
    }

    final var nds = params.get(NUM_FRACTION_DELIMITER);
    if (nds != null && !nds.isBlank()) {
      nf.setFractionDelimiter(nds.length() < 2 ? nds : nds.substring(0, 1));
    }

    final var ng = params.get(NUM_GROUPING);
    if (ng != null) {
      if (ng.equals("")) nf.setGroupingSize(0);
      else {
        final var groups = new ArrayList<Integer>();

        for (var s : ng.split("\\D")) {
          if (s.isBlank()) continue;
          final var gv = parseInt(s);
          groups.add(gv);
          if (gv == 0) break;
        }

        if (groups.isEmpty()) {
          nf.setGroupingSize(0);
        } else if (groups.size() == 1) {
          final var lowerGroupSize = groups.get(0);
          nf.setGroupingSize(lowerGroupSize);
        } else {
          final var lowerGroupSize = groups.get(0);
          final var highGroupsSize = groups.get(1);
          nf.setGroupingSize(lowerGroupSize, highGroupsSize);
        }
      }
    }

    final var nn = params.get(NUM_NEGATIVE_APPEARANCE);
    if (nn != null) {
      final var i_nn = parseInt(nn);
      nf.setNegativeAppearance(i_nn);
    }

    final var ns = params.get(NUM_DECIMAL_SHIFT);
    if (ns != null) {
      final var i_ns = parseInt(ns);
      nf.setDecimalShift(i_ns);
    }

    final var ngs = params.get(NUM_GROUPS_DELIMITER);
    if (ngs != null && !ngs.isBlank()) {
      nf.setGroupDelimiter(ngs.length() < 2 ? ngs : ngs.substring(0, 1));
    }

    return nf.format(value);
  }

  private ValueFormatter() {}
}
