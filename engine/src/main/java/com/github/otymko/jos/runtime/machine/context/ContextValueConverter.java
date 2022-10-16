/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.context;

import com.github.otymko.jos.compiler.Enumerations;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.EnumerationValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Конвертер IValue в значения райнтайма
 */
public final class ContextValueConverter {
    /**
     * Конвертировать IValue в значение рантайма с указанным типом
     * @param value значение IValue
     * @param type тип
     *
     * @return конвертированное значение
     *
     * @param <T> тип итогового значения
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertValue(IValue value, Class<T> type) {
        try {
            return (T) convertValueByType(value, type);
        }
        catch (ClassCastException exception) {
            throw MachineException.invalidArgumentValueException();
        }
    }

    /**
     * Конвертировать возвращаемое значение в IValue.
     *
     * @param sourceValue Возвращаемое значение.
     * @param type Тип возвращаемого значения.
     */
    @SuppressWarnings("unchecked")
    public static IValue convertReturnValue(Object sourceValue, Class<?> type) {
        if (sourceValue == null) {
            return ValueFactory.create();
        }

        if (type == IValue.class) {
            return (IValue) sourceValue;
        } else if (type == String.class) {
            return ValueFactory.create((String) sourceValue);
        } else if (type == BigDecimal.class) {
            return ValueFactory.create((BigDecimal)sourceValue);
        } else if (type == int.class || type == Integer.class) {
            return ValueFactory.create((int) sourceValue);
        } else if (type == double.class || type == Double.class) {
            return ValueFactory.create(BigDecimal.valueOf((double) sourceValue));
        } else if (type == byte.class || type == Byte.class) {
            return ValueFactory.create(BigDecimal.valueOf((int) sourceValue));
        } else if (type == float.class || type == Float.class) {
            return ValueFactory.create((float) sourceValue);
        } else if (type == long.class || type == Long.class) {
            return ValueFactory.create((long) sourceValue);
        } else if (type == short.class || type == Short.class) {
            return ValueFactory.create((int) sourceValue);
        } else if (type == Date.class) {
            return ValueFactory.create((Date) sourceValue);
        } else if (type == boolean.class || type == Boolean.class) {
            return ValueFactory.create((boolean) sourceValue);
        } else if (EnumType.class.isAssignableFrom(type)) {
            return Enumerations
                    .getEnumByClass((Class<? extends EnumType>) type)
                    .getEnumValueType((EnumType) sourceValue);
        } else if (RuntimeContext.class.isAssignableFrom(type)){
            return (IValue)sourceValue;
        } else {
            throw MachineException.invalidArgumentValueException();
        }
    }

    private static Object convertValueByType(IValue value, Class<?> type) {
        if (value == null) {
            return null;
        }

        if (type == IValue.class) {
            return value.getRawValue();
        }

        if (type == IVariable.class) {
            return value;
        }

        Object valueObject;

        if (value.getDataType() == DataType.UNDEFINED) {
            valueObject = null;
        } else if (type == String.class) {
            valueObject = value.getRawValue().asString();
        } else if (type == BigDecimal.class) {
            valueObject = value.getRawValue().asNumber();
        } else if (type == int.class || type == Integer.class) {
            valueObject = value.getRawValue().asNumber().intValue();
        } else if (type == double.class || type == Double.class) {
            valueObject = value.getRawValue().asNumber().doubleValue();
        } else if (type == byte.class || type == Byte.class) {
            valueObject = value.getRawValue().asNumber().byteValue();
        } else if (type == float.class || type == Float.class) {
            valueObject = value.getRawValue().asNumber().floatValue();
        } else if (type == long.class || type == Long.class) {
            valueObject = value.getRawValue().asNumber().longValue();
        } else if (type == short.class || type == Short.class) {
            valueObject = value.getRawValue().asNumber().shortValue();
        } else if (type == Date.class) {
            valueObject = value.getRawValue().asDate();
        } else if (type == boolean.class || type == Boolean.class) {
            valueObject = value.getRawValue().asBoolean();
        } else if (EnumType.class.isAssignableFrom(type)) {
            var enumerationValue = (EnumerationValue)value.getRawValue();
            valueObject = enumerationValue.getValue();
        } else {
            valueObject = value.getRawValue();
        }

        return valueObject;
    }

    private ContextValueConverter() {
        // None
    }
}
