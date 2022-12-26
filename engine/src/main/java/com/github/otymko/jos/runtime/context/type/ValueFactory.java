/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Фабрика примитивных значений.
 */
public class ValueFactory {
    private ValueFactory() {
        // none
    }

    /**
     * Создать Неопределено.
     */
    public static IValue create() {
        return UndefinedValue.VALUE;
    }

    /**
     * Создать строку.
     */
    public static IValue create(String value) {
        return StringValue.create(value);
    }

    /**
     * Создать число.
     */
    public static IValue create(float value) {
        return NumberValue.create(BigDecimal.valueOf(value));
    }

    /**
     * Создать число.
     */
    public static IValue create(BigDecimal value) {
        return NumberValue.create(value);
    }

    /**
     * Создать число.
     */
    public static IValue create(int value) {
        return NumberValue.create(BigDecimal.valueOf(value));
    }

    /**
     * Создать дату.
     */
    public static IValue create(Date value) {
        return new DateValue(value);
    }

    /**
     * Создать булево.
     */
    public static IValue create(boolean value) {
        return value ? BooleanValue.TRUE : BooleanValue.FALSE;
    }

    /**
     * Возвращает изначальное значение или значений по умолчанию.
     */
    public static IValue rawValueOrDefault(IValue inValue, IValue defaultValue) {
        if (inValue == null) {
            return defaultValue;
        }
        final var raw = inValue.getRawValue();
        if (raw.getDataType() == DataType.UNDEFINED) {
            return defaultValue;
        }
        return raw;
    }

    /**
     * Возвращает изначальное значение или Неопределено.
     */
    public static IValue rawValueOrUndefined(IValue inValue) {
        return rawValueOrDefault(inValue, create());
    }

    // TODO: invalidValue?

    /**
     * Создать Null.
     */
    public static IValue createNullValue() {
        return NullValue.VALUE;
    }

    // TODO: object

    /**
     * Распарсить значение.
     */
    public static IValue parse(String view, DataType type) {
        if (type == DataType.DATE) {
            return DateValue.parse(view);
        }

        throw MachineException.operationNotImplementedException();
    }
}
