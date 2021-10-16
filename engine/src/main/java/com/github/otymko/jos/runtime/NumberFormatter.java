/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NumberFormatter {

  private static final int NEGATIVE_BRACES = 0; // (1,1)
  private static final int NEGATIVE_PREFIX_SIGN = 1; // -1,1
  private static final int NEGATIVE_PREFIX_SIGN_WHITESPACE = 2; // - 1,1
  private static final int NEGATIVE_POSTFIX_SIGN = 3; // 1,1-
  private static final int NEGATIVE_POSTFIX_SIGN_WHITESPACE = 4; // 1,1 -
  private static final int DEFAULT_FRACTION_DIGITS = 340;

  public NumberFormatter(Locale locale) {
    final var systemFormatter = (DecimalFormat) NumberFormat.getInstance(locale);
    fractionDelimiter = String.valueOf(systemFormatter.getDecimalFormatSymbols().getDecimalSeparator());
    groupDelimiter = String.valueOf(systemFormatter.getDecimalFormatSymbols().getGroupingSeparator());
    lowerGroupingSize = systemFormatter.getGroupingSize();
    higherGroupingSize = 0;
    if (systemFormatter.getNegativePrefix().startsWith("(")) {
      negativeAppearance = NEGATIVE_BRACES;
    }
  }

  @Getter
  private int maxSize = -1;

  public void setMaxSize(int value) {
    if (value < 0) {
      return;
    }

    maxSize = value;
    if (decimalSize == -1) {
      decimalSize = 0;
    }
  }

  @Getter
  private int decimalSize = -1;

  public void setDecimalSize(int value) {
    if (value >= 0) {
      decimalSize = value;
    }
  }

  @Getter
  @Setter
  private int negativeAppearance = NEGATIVE_PREFIX_SIGN;

  @Getter
  private String zeroAppearance = "";

  public void setZeroAppearance(String value) {
    if (value.isEmpty()) {
      zeroAppearance = "0";
    } else {
      zeroAppearance = value;
    }
  }

  @Getter
  @Setter
  private boolean leadingZeroes;

  @Getter
  private String fractionDelimiter;

  public void setFractionDelimiter(String value) {
    if (value == null || value.isBlank()) {
      return;
    }
    if (value.length() > 1) {
      fractionDelimiter = value.substring(0, 1);
    } else {
      fractionDelimiter = value;
    }
  }

  @Getter
  private String groupDelimiter;

  public void setGroupDelimiter(String value) {
    if (value == null || value.isBlank()) {
      return;
    }
    if (value.length() > 1) {
      groupDelimiter = value.substring(0, 1);
    } else {
      groupDelimiter = value;
    }
  }

  @Getter
  @Setter
  private int decimalShift;

  @Getter
  private int lowerGroupingSize;

  @Getter
  private int higherGroupingSize;

  public void setGroupingSize(int lowGroup) {
    setGroupingSize(lowGroup, -1);
  }

  public void setGroupingSize(int lowGroup, int hiGroup) {
    lowerGroupingSize = lowGroup;
    higherGroupingSize = hiGroup;
  }

  public void setGroupingSize(List<Integer> groups) {
    if (groups.isEmpty()) {

      setGroupingSize(0);

    } else if (groups.size() == 1) {

      setGroupingSize(groups.get(0));

    } else {

      setGroupingSize(groups.get(0), groups.get(1));

    }
  }

  private BigDecimal valueToFormat(BigDecimal value) {
    if (decimalShift < 0) {
      return value.movePointRight(-decimalShift);
    }
    if (decimalShift > 0) {
      return value.movePointLeft(decimalShift);
    }
    return value;
  }

  private String applySign(int sign, String unsignedValuePresentation) {
    if (sign >= 0) {
      return unsignedValuePresentation;
    }
    switch (negativeAppearance){
      case NEGATIVE_BRACES: return String.format("(%s)", unsignedValuePresentation);
      case NEGATIVE_POSTFIX_SIGN: return String.format("%s-", unsignedValuePresentation);
      case NEGATIVE_POSTFIX_SIGN_WHITESPACE: return String.format("%s -", unsignedValuePresentation);
      case NEGATIVE_PREFIX_SIGN_WHITESPACE: return String.format("- %s", unsignedValuePresentation);
      default:
      case NEGATIVE_PREFIX_SIGN: return String.format("-%s", unsignedValuePresentation);
    }
  }

  private String applyGroups(String value) {
    if (lowerGroupingSize == 0 || value.length() < lowerGroupingSize) {
      return value;
    }
    final var group1pos = value.length() - lowerGroupingSize;

    final var groupsBuilder = new ArrayList<String>();
    groupsBuilder.add(value.substring(group1pos));

    var rest = value.substring(0, group1pos);
    if (higherGroupingSize != -1) {
      final var groupSize = higherGroupingSize == 0 ? lowerGroupingSize : higherGroupingSize;
      while (rest.length() > groupSize) {
        final var group2pos = rest.length() - groupSize;
        groupsBuilder.add(rest.substring(group2pos));

        rest = rest.substring(0, group2pos);
      }
    }

    if (!rest.isEmpty()) {
      groupsBuilder.add(rest);
    }

    Collections.reverse(groupsBuilder);
    return String.join(groupDelimiter, groupsBuilder);
  }

  public String format(BigDecimal value) {
    final var valuePresentation = valueToFormat(value);
    if (valuePresentation.equals(BigDecimal.ZERO)) {
      return zeroAppearance;
    }

    final var systemFormat = getInvariantFormat();

    if (decimalSize != -1) {
      if (maxSize > 0 && decimalSize > maxSize) {
        decimalSize = maxSize;
      }
      systemFormat.setMaximumFractionDigits(decimalSize);
      systemFormat.setMinimumFractionDigits(decimalSize);
      systemFormat.setRoundingMode(RoundingMode.HALF_UP);
    } else {
      systemFormat.setMaximumFractionDigits(DEFAULT_FRACTION_DIGITS);
    }

    final var preValue = systemFormat.format(valuePresentation.abs()).split("\\.");

    var intPart = preValue[0];
    var fracPart = preValue.length < 2 ? "" : preValue[1];

    if (intPart.equals("0") && fracPart.isBlank()) {
      return zeroAppearance;
    }

    if (maxSize > 0 && intPart.length() + fracPart.length() > maxSize) {
      if (intPart.equals("0") && fracPart.length() == maxSize) {
        intPart = "";
      } else {

        final var intToFill = decimalSize > 0 ? (maxSize - decimalSize) : maxSize;
        final var fracToFill = decimalSize > 0 ? decimalSize : 0;

        fracPart = "9".repeat(fracToFill);
        intPart = "9".repeat(intToFill);
      }
    }

    if (decimalSize == 0 || (decimalSize == -1 && fracPart.isEmpty())) {
      fracPart = "";
    }

    if (leadingZeroes && maxSize > intPart.length() + fracPart.length()) {
      final var zeroesCount = maxSize - (intPart.length() + fracPart.length());
      intPart = "0".repeat(zeroesCount) + intPart;
    }

    if (!fracPart.isBlank()) {
      fracPart = String.format("%s%s", fractionDelimiter, fracPart);
    }

    intPart = applyGroups(intPart);

    return applySign(valuePresentation.signum(), String.format("%s%s", intPart, fracPart));
  }

  private static DecimalFormat getInvariantFormat() {
    final var systemFormat = (DecimalFormat)NumberFormat.getInstance(Locale.ROOT);

    systemFormat.setNegativeSuffix("");
    systemFormat.setPositivePrefix("");
    systemFormat.setNegativeSuffix("");
    systemFormat.setPositiveSuffix("");
    systemFormat.setGroupingUsed(false);
    systemFormat.setDecimalSeparatorAlwaysShown(true);

    return systemFormat;
  }
}
