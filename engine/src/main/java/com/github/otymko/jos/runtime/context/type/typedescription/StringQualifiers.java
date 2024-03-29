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
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedLength;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;
import lombok.Value;

/**
 * Квалификаторы строки для Описания типов
 */
@ContextClass(name = "КвалификаторыСтроки", alias = "StringQualifiers")
@Value
public class StringQualifiers extends ContextValue {

    public static final ContextInfo INFO = ContextInfo.createByClass(StringQualifiers.class);

    /**
     * Длина строки. 0 - Неограниченно
     */
    @ContextProperty(name = "Длина", alias = "Length", accessMode = PropertyAccessMode.READ_ONLY)
    @Getter
    int length;

    /**
     * Допустимая длина строки
     *
     * @see AllowedLength
     */
    @ContextProperty(name = "ДопустимаяДлина", alias = "AllowedLength", accessMode = PropertyAccessMode.READ_ONLY)
    @Getter
    AllowedLength allowedLength;

    @ContextConstructor
    public static StringQualifiers constructor(int length, AllowedLength sourceAllowedLength) {
        var allowedLength = sourceAllowedLength == null ? AllowedLength.VARIABLE : sourceAllowedLength;

        return new StringQualifiers(length, allowedLength);
    }

    @ContextConstructor
    public static StringQualifiers constructor(int length) {
        return new StringQualifiers(length, AllowedLength.VARIABLE);
    }

    @ContextConstructor
    public static StringQualifiers constructor() {
        return new StringQualifiers(0, AllowedLength.VARIABLE);
    }

    private String adjustString(String value) {
        var result = value;
        if (length != 0 && result.length() > length) {
            result = result.substring(0, length);
        }

        if (allowedLength == AllowedLength.FIXED && result.length() < length) {
            result = result.concat(" ".repeat(length - result.length()));
        }

        return result;
    }

    public IValue adjustValue(IValue value) {
        return ValueFactory.create(adjustString(value.asString()));
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
