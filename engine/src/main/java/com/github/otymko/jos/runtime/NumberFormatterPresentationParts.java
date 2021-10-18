/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

final class NumberFormatterPresentationParts {

  NumberFormatterPresentationParts(String[] parts) {
    this.integerPart = parts.length > 0 ? parts[0] : "0";
    this.fractionPart = parts.length > 1 ? parts[1] : "";
  }

  @Getter
  private String integerPart;

  @Getter
  private String fractionPart;

  public boolean isEqualsZero() {
    return integerPart.equals("0") && fractionPart.isEmpty();
  }

  public void applySizeRestrictions(int maxSize, int decimalSize) {

    if (maxSize > 0 && integerPart.length() + fractionPart.length() > maxSize) {
      if (integerPart.equals("0") && fractionPart.length() == maxSize) {
        integerPart = "";
      } else {

        final var fracToFill = Math.max(decimalSize, 0);
        final var intToFill = maxSize - fracToFill;

        fractionPart = "9".repeat(fracToFill);
        integerPart = "9".repeat(intToFill);
      }
    }

    if (decimalSize == 0 || (decimalSize == -1 && fractionPart.isEmpty())) {
      fractionPart = "";
    }

  }

  public void applyGroups(String groupDelimiter, int lowerGroupingSize, int higherGroupingSize) {
    if (lowerGroupingSize == 0 || integerPart.length() < lowerGroupingSize) {
      return;
    }
    final var group1pos = integerPart.length() - lowerGroupingSize;

    final var groupsBuilder = new ArrayList<String>();
    groupsBuilder.add(integerPart.substring(group1pos));

    var rest = integerPart.substring(0, group1pos);
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
    integerPart = String.join(groupDelimiter, groupsBuilder);
  }

  public void applyLeadingZeroes(int maxSize) {
    if (maxSize > integerPart.length() + fractionPart.length()) {
      final var zeroesCount = maxSize - (integerPart.length() + fractionPart.length());
      integerPart = "0".repeat(zeroesCount) + integerPart;
    }
  }

  public String presentation(String delimiter) {

    final var presentationBuilder = new StringBuilder();
    presentationBuilder.append(integerPart);

    if (!fractionPart.isBlank()) {
      presentationBuilder.append(delimiter);
      presentationBuilder.append(fractionPart);
    }

    return presentationBuilder.toString();
  }

}
