/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedLength;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;
import lombok.Value;

/**
 * Квалификатор двоичных данных для Описания типов
 */
@ContextClass(name = "КвалификаторыДвоичныхДанных", alias = "BinaryDataQualifiers")
@Value
public class BinaryDataQualifiers extends ContextValue {

    public static final ContextInfo INFO = ContextInfo.createByClass(BinaryDataQualifiers.class);

    /**
     * Возвращает квалификаторы двоичных данных по указанным параметрам
     *
     * @param length        Максимальный размер данных
     * @param sourceAllowedLength Допустимый размер данных
     * @return КвалификаторыДвоичныхДанных
     * @see BinaryDataQualifiers
     * @see AllowedLength
     */
    @ContextConstructor
    public static BinaryDataQualifiers constructor(int length, AllowedLength sourceAllowedLength) {
        var allowedLengthValue = sourceAllowedLength == null ? AllowedLength.VARIABLE : sourceAllowedLength;

        return new BinaryDataQualifiers(length, allowedLengthValue);
    }

    /**
     * Возвращает квалификаторы двоичных данных указанной длины. Допустимая длина устанавливается Переменная.
     *
     * @param length Максимальный размер данных
     *
     * @return КвалификаторыДвоичныхДанных
     * @see BinaryDataQualifiers
     * @see AllowedLength
     */
    @ContextConstructor
    public static BinaryDataQualifiers constructor(int length) {
        return new BinaryDataQualifiers(length, AllowedLength.VARIABLE);
    }

    /**
     * Возвращает квалификаторы двоичных данных неограниченной длины. Допустимая длина устанавливается Переменная.
     *
     * @return КвалификаторыДвоичныхДанных
     * @see BinaryDataQualifiers
     * @see AllowedLength
     */
    @ContextConstructor
    public static BinaryDataQualifiers constructor() {
        return new BinaryDataQualifiers(0, AllowedLength.VARIABLE);
    }

    /**
     * Размер двоичных данных
     */
    @ContextProperty(name = "Длина", alias = "Length", accessMode = PropertyAccessMode.READ_ONLY)
    @Getter
    int length;

    /**
     * Допустимый размер данных. 0 - Неограниченно
     *
     * @see AllowedLength
     */
    @ContextProperty(name = "ДопустимаяДлина", alias = "AllowedLength", accessMode = PropertyAccessMode.READ_ONLY)
    @Getter
    AllowedLength allowedLength;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
