/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateLineCleaner {
  private final String QUOTE = "\'";

  public String clean(String inValue) {
    String value;
    if (inValue.startsWith(QUOTE)) {
      value = inValue.substring(1, inValue.length() - 1);
    } else {
      value = inValue;
    }
    return value;
  }

}
