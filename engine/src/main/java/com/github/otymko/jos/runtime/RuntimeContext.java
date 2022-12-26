/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.localization.Resources;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.ContextMethodCall;
import com.github.otymko.jos.runtime.machine.context.ContextValueConverter;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;

import java.lang.reflect.InvocationTargetException;

import static com.github.otymko.jos.core.localization.MessageResource.*;

/**
 * Базовый класс контекста.
 */
public interface RuntimeContext {
    /**
     * Получить описание контекста.
     */
    ContextInfo getContextInfo();

    /**
     * Выполнить как процедуру.
     *
     * @param methodId Индекс метода.
     * @param arguments Аргументы.
     */
    default void callAsProcedure(int methodId, IValue[] arguments) {
        var methodInfo = getContextInfo().getMethods()[methodId];
        var callMethod = methodInfo.getMethod();

        ContextMethodCall.callAsProcedure(this, callMethod, arguments);
    }

    /**
     * Выполнить как функцию и вернуть результат.
     *
     * @param methodId Индекс метода.
     * @param arguments Аргументы.
     */
    default IValue callAsFunction(int methodId, IValue[] arguments) {
        var methodInfo = getContextInfo().getMethods()[methodId];
        var callMethod = methodInfo.getMethod();

        return ContextMethodCall.callAsFunction(this, callMethod, arguments);
    }

    /**
     * Найти индекс методы по имени.
     *
     * @param name Имя метода.
     *
     * @return -1 - если метод не найден.
     */
    default int findMethodId(String name) {
        var contextInfo = getContextInfo();
        for (var index = 0; index < contextInfo.getMethods().length; index++) {
            var method = contextInfo.getMethods()[index];
            if (method.equalsByName(name)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Получить метод по индексу.
     *
     * @param methodId Индекс метода.
     */
    default MethodInfo getMethodById(int methodId) {
        return getContextInfo().getMethods()[methodId];
    }

    /**
     * Найти свойство по имени.
     *
     * @param propertyName Имя свойство.
     *
     * @return -1 - если свойство не найден.
     */
    default int findProperty(String propertyName) {
        int position = 0;
        for (var property : getContextInfo().getProperties()) {
            if (property.getName().equalsIgnoreCase(propertyName) || property.getAlias().equalsIgnoreCase(propertyName)) {
                return position;
            }
            position++;
        }
        return -1;
    }

    /**
     * Получить свойство по индексу.
     *
     * @param index Индекс свойства.
     */
    default IValue getPropertyValue(int index) {
        var property = getContextInfo().getProperties()[index];
        Object result;
        if (property.hasGetter()) {
            var getter = property.getGetter();
            try {
                result = getter.invoke(this);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MachineException(Resources.getResourceString(ERROR_GET_PROPERTY_VALUE));
            }

            return ContextValueConverter.convertReturnValue(result, getter.getReturnType());
        }

        var field = property.getField();
        try {
            result = field.get(this);
        } catch (IllegalAccessException exception) {
            throw new MachineException(Resources.getResourceString(ERROR_GET_PROPERTY_VALUE));
        }

        return ContextValueConverter.convertReturnValue(result, field.getType());
    }

    /**
     * Установить значение свойства.
     *
     * @param index Индекс свойства.
     * @param value Значение свойства.
     */
    default void setPropertyValue(int index, IValue value) {
        var property = getContextInfo().getProperties()[index];
        if (property.hasSetter()) {
            var setter = property.getSetter();
            try {
                setter.invoke(this, ContextValueConverter.convertValue(value, setter.getParameterTypes()[0]));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MachineException(Resources.getResourceString(ERROR_SET_PROPERTY_VALUE));
            }
        } else {
            var field = property.getField();
            try {
                field.set(this, ContextValueConverter.convertValue(value, field.getType()));
            } catch (IllegalAccessException e) {
                throw new MachineException(Resources.getResourceString(ERROR_SET_PROPERTY_VALUE));
            }
        }
    }

    /**
     * Это свойство только для чтения.
     *
     * @param index Индекс свойства.
     */
    default boolean isPropertyReadOnly(int index) {
        var property = getContextInfo().getProperties()[index];
        return property.getAccessMode() == PropertyAccessMode.READ_ONLY;
    }

    /**
     * Это свойство только для записи.
     *
     * @param index Индекс свойства.
     */
    default boolean isPropertyWriteOnly(int index) {
        var property = getContextInfo().getProperties()[index];
        return property.getAccessMode() == PropertyAccessMode.WRITE_ONLY;
    }
}
