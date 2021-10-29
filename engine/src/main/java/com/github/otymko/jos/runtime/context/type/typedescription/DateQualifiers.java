/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.enumeration.DateFractionsEnum;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;
import lombok.Value;

@ContextClass(name = "КвалификаторыСтроки", alias = "StringQualifiers")
@Value
public class DateQualifiers extends ContextValue {

  public static final ContextInfo INFO = ContextInfo.createByClass(DateQualifiers.class);

  @Getter
  @ContextProperty(name = "ЧастиДаты", alias = "DateFractions", accessMode = PropertyAccessMode.READ_ONLY)
  DateFractionsEnum dateFractions;

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
