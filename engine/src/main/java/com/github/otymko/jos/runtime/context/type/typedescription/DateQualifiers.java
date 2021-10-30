/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.compiler.EnumerationHelper;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedLengthEnum;
import com.github.otymko.jos.runtime.context.type.enumeration.DateFractionsEnum;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Value;

/**
 * Квалификаторы даты для Описания
 */
@ContextClass(name = "КвалификаторыДаты", alias = "DateQualifiers")
@Value
public class DateQualifiers extends ContextValue {

  public static final ContextInfo INFO = ContextInfo.createByClass(DateQualifiers.class);

  /**
   * Части даты, хранимые в типе
   *
   * @see DateFractionsEnum
   */
  @ContextProperty(name = "ЧастиДаты", alias = "DateFractions", accessMode = PropertyAccessMode.READ_ONLY)
  public DateFractionsEnum dateFractions;

  public IValue getDateFractions() {
    return EnumerationHelper.getEnumByClass(DateFractionsEnum.class).getEnumValueType(dateFractions);
  }

  /**
   * Возвращает квалификаторы даты с указанными частями дат
   * @param dateParts Части дат
   * @see DateFractionsEnum
   * @return Квалификатор дат
   * @see DateQualifiers
   */
  @ContextConstructor
  public static DateQualifiers constructor(IValue dateParts) {
    final var datePartsValue = EnumerationHelper.getEnumValueOrDefault(dateParts, DateFractionsEnum.DATE_TIME);
    return new DateQualifiers((DateFractionsEnum) datePartsValue.getValue());
  }

  /**
   * Возвращает квалификаторы даты с частями дат Дата
   * @see DateFractionsEnum
   * @return Квалификатор дат
   * @see DateQualifiers
   */
  @ContextConstructor
  public static DateQualifiers constructor() {
    return new DateQualifiers(DateFractionsEnum.DATE);
  }

  public boolean equals(Object o) {
    if (!(o instanceof DateQualifiers)) {
      return false;
    }
    final var asDateQualifiers = (DateQualifiers) o;
    return asDateQualifiers.dateFractions == dateFractions;
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
