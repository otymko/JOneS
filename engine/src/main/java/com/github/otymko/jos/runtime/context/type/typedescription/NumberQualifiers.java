/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.compiler.EnumerationHelper;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedSignEnum;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Value;

/**
 * Квалификаторы числа для Описания типов
 */
@ContextClass(name = "КвалификаторыЧисла", alias = "NumberQualifiers")
@Value
public class NumberQualifiers extends ContextValue {

  public static final ContextInfo INFO = ContextInfo.createByClass(NumberQualifiers.class);

  /**
   * Общее количество десятичных знаков, доступное для числа. 0 - Неограниченно
   */
  @ContextProperty(name = "Разрядность", alias = "Digits", accessMode = PropertyAccessMode.READ_ONLY)
  public int digits;

  /**
   * Количество знаков дробной части числа. 0 - Без дробной части
   */
  @ContextProperty(name = "РазрядностьДробнойЧасти", alias = "FractionDigits", accessMode = PropertyAccessMode.READ_ONLY)
  public int fractionDigits;

  /**
   * Допустимый знак числа
   */
  @ContextProperty(name = "ДопустимыйЗнак", alias = "allowedSign", accessMode = PropertyAccessMode.READ_ONLY)
  public AllowedSignEnum allowedSign;

  public IValue getDigits() {
    return ValueFactory.create(digits);
  }

  public IValue getFractionDigits() {
    return ValueFactory.create(fractionDigits);
  }

  public IValue getAllowedSign() {
    return EnumerationHelper.getEnumByClass(AllowedSignEnum.class).getEnumValueType(allowedSign);
  }

  public boolean equals(Object o) {
    if (!(o instanceof NumberQualifiers)) {
      return false;
    }

    final var n = (NumberQualifiers)o;
    return n.digits == digits
            && n.fractionDigits == fractionDigits
            && n.allowedSign == allowedSign;
  }

  @ContextConstructor
  public static NumberQualifiers constructor(IValue digits, IValue fractionDigits, IValue allowedSign) {

    final var allowedSignValue = EnumerationHelper.getEnumValueOrDefault(allowedSign, AllowedSignEnum.ANY);
    return new NumberQualifiers(
            digits.asNumber().intValue(),
            fractionDigits.asNumber().intValue(),
            (AllowedSignEnum) allowedSignValue.getValue());
  }

  @ContextConstructor
  public static NumberQualifiers constructor(IValue digits, IValue fractionDigits) {

    return new NumberQualifiers(
            digits.asNumber().intValue(),
            fractionDigits.asNumber().intValue(),
            AllowedSignEnum.ANY);
  }

  @ContextConstructor
  public static NumberQualifiers constructor(IValue digits) {

    return new NumberQualifiers(
            digits.asNumber().intValue(),
            0,
            AllowedSignEnum.ANY);
  }

  @ContextConstructor
  public static NumberQualifiers constructor() {

    return new NumberQualifiers(
            0,
            0,
            AllowedSignEnum.ANY);
  }

  public static NumberQualifiers getOrDefault(IValue numberQualifiers) {
    if (numberQualifiers == null) {
      return constructor();
    }
    final var rawValue = numberQualifiers.getRawValue();
    if (rawValue.getDataType() == DataType.UNDEFINED) {
      return constructor();
    }
    if (!(rawValue instanceof NumberQualifiers)) {
      throw MachineException.invalidArgumentValueException();
    }
    return (NumberQualifiers) rawValue;
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
