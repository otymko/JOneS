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
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedLengthEnum;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Value;

/**
 * Квалификатор двоичных данных для Описания типов
 */
@ContextClass(name = "КвалификаторыДвоичныхДанных", alias = "BinaryDataQualifiers")
@Value
public class BinaryDataQualifiers extends ContextValue {

  public static final ContextInfo INFO = ContextInfo.createByClass(BinaryDataQualifiers.class);

  /**
   * Размер двоичных данных
   */
  @ContextProperty(name = "Длина", alias = "Length", accessMode = PropertyAccessMode.READ_ONLY)
  public int length;

  /**
   * Допустимый размер данных. 0 - Неограниченно
   *
   * @see AllowedLengthEnum
   */
  @ContextProperty(name = "ДопустимаяДлина", alias = "AllowedLength", accessMode = PropertyAccessMode.READ_ONLY)
  public AllowedLengthEnum allowedLength;

  public IValue getLength() {
    return ValueFactory.create(length);
  }

  public IValue getAllowedLength() {
    return EnumerationHelper.getEnumByClass(AllowedLengthEnum.class).getEnumValueType(allowedLength);
  }

  /**
   * Возвращает квалификаторы двоичных данных по указанным параметрам
   * @param length        Максимальный размер данных
   * @param allowedLength Допустимый размер данных
   * @return КвалификаторыДвоичныхДанных
   * @see BinaryDataQualifiers
   * @see AllowedLengthEnum
   */
  @ContextConstructor
  public static BinaryDataQualifiers constructor(IValue length, IValue allowedLength) {
    final var allowedLengthValue = EnumerationHelper.getEnumValueOrDefault(allowedLength, AllowedLengthEnum.VARIABLE);
    return new BinaryDataQualifiers(length.asNumber().intValue(), (AllowedLengthEnum) allowedLengthValue.getValue());
  }

  /**
   * Возвращает квалификаторы двоичных данных указанной длины. Допустимая длина устанавливается Переменная.
   * @param length        Максимальный размер данных
   * @return КвалификаторыДвоичныхДанных
   * @see BinaryDataQualifiers
   * @see AllowedLengthEnum
   */
  @ContextConstructor
  public static BinaryDataQualifiers constructor(IValue length) {
    return new BinaryDataQualifiers(length.asNumber().intValue(), AllowedLengthEnum.VARIABLE);
  }

  /**
   * Возвращает квалификаторы двоичных данных неограниченной длины. Допустимая длина устанавливается Переменная.
   * @return КвалификаторыДвоичныхДанных
   * @see BinaryDataQualifiers
   * @see AllowedLengthEnum
   */
  @ContextConstructor
  public static BinaryDataQualifiers constructor() {
    return new BinaryDataQualifiers(0, AllowedLengthEnum.VARIABLE);
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
