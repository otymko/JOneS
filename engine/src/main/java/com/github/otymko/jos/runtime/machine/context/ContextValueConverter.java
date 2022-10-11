/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.context;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import lombok.experimental.UtilityClass;

import java.util.Date;

/**
 * Конвертер IValue в значения райнтайма
 */
@UtilityClass
public class ContextValueConverter {

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
    public <T> T convertValue(IValue value, Class<T> type) {
        try {
            return (T) convertValueByType(value, type);
        }
        catch (ClassCastException exception) {
            throw MachineException.invalidArgumentValueException();
        }
    }

    private Object convertValueByType(IValue value, Class<?> type) {
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
        } else {
            valueObject = value.getRawValue();
        }

        return valueObject;
    }
}
