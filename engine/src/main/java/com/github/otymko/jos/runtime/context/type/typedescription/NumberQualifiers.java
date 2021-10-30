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
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedSignEnum;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

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

  BigDecimal getNines() {
    // формируем число из девяток
    var result = BigDecimal.ONE;
    for (var i = digits; i > 0; i--) {
      result = result.multiply(BigDecimal.TEN);
    }
    result = result.subtract(BigDecimal.ONE).setScale(fractionDigits);
    for (var i = fractionDigits; i > 0; i--) {
      result = result.divide(BigDecimal.TEN, RoundingMode.FLOOR);
    }
    return result;
  }

  private BigDecimal adjustNumber(BigDecimal number) {
    if (digits == 0) {
      return number;
    }
    if (allowedSign == AllowedSignEnum.NON_NEGATIVE && number.compareTo(BigDecimal.ZERO) < 0) {
      return BigDecimal.ZERO;
    }
    var result = number.setScale(fractionDigits, RoundingMode.HALF_UP);
    if (result.precision() <= digits) {
      return result;
    }
    return getNines();
  }

  public IValue adjustValue(IValue value) {
    try {
      final var castedValue = value.asNumber();
      return ValueFactory.create(adjustNumber(castedValue));
    } catch (Exception e) {
      return ValueFactory.create(0);
    }
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

  public int hashCode() {
    return digits;
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

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
