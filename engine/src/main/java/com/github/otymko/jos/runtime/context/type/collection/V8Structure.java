/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.util.Common;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Коллекция пар КлючИЗначение. Хранит ключи как свойства объекта.
 *
 * @see V8KeyAndValue
 */
@ContextClass(name = "Структура", alias = "Structure")
public class V8Structure extends V8AbstractStructure {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8Structure.class);
    private static final Pattern FIELDS_SPLITTER = Pattern.compile(",");

    @ContextConstructor
    public static V8Structure constructor() {
        return new V8Structure();
    }

    @ContextConstructor
    public static V8Structure constructorByValue(IValue value) {
        if (value instanceof V8FixedStructure) {
            return new V8Structure((V8FixedStructure)value);
        } else if (value.getDataType() == DataType.STRING) {
            return constructorExtended(value);
        }

        throw MachineException.invalidArgumentValueException();
    }

    @ContextConstructor
    public static V8Structure constructorExtended(IValue keysOrFixedStructure, IValue... values) {
        if (keysOrFixedStructure.getDataType() != DataType.STRING
                && keysOrFixedStructure.getDataType() != DataType.UNDEFINED) {
            
            throw MachineException.invalidArgumentValueException();
        }

        final var result = new V8Structure();
        final var fieldNames = FIELDS_SPLITTER.split(keysOrFixedStructure.asString());
        int valueIndex = 0;
        for (final var fieldName : fieldNames) {
            if (fieldName.isBlank()) {
                continue;
            }
            final var valueToPut = valueIndex < values.length ? values[valueIndex] : UndefinedValue.VALUE;
            result.insert(ValueFactory.create(fieldName.trim()), valueToPut);
            ++valueIndex;
        }

        return result;
    }

    private V8Structure(V8FixedStructure fixedStructure) {
        super(new HashMap<>(fixedStructure.values), copyTreeMap(fixedStructure.views));
    }
    private V8Structure() {
        super(new HashMap<>(), new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public void insert(IValue key, IValue value) {
        var keyValue = key.asString();
        if (!Common.isValidStringIdentifier(key)) {
            throw MachineException.invalidPropertyNameStructureException(keyValue);
        }

        var addingValue = ValueFactory.rawValueOrUndefined(value);
        if (views.containsKey(keyValue)) {
            var objectKey = views.get(keyValue);
            values.put(objectKey, addingValue);
        } else {
            views.put(keyValue, key);
            values.put(key, addingValue);
        }
    }

    @ContextMethod(name = "Очистить", alias = "Clear")
    public void clear() {
        values.clear();
        views.clear();
    }

    @ContextMethod(name = "Удалить", alias = "Delete")
    public void remove(IValue key) {
        if (!Common.isValidStringIdentifier(key)) {
            throw MachineException.invalidPropertyNameStructureException(key.asString());
        }

        var keyValue = key.asString();
        views.remove(keyValue);
        values.remove(key);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        setValueInternal(index, value);
    }

    @Override
    public void setPropertyValue(IValue index, IValue value) {
        setValueInternal(index, value);
    }

    private void setValueInternal(IValue index, IValue value) {
        var key = index.asString();
        if (!Common.isValidStringIdentifier(index)) {
            throw MachineException.invalidPropertyNameStructureException(key);
        }

        if (!views.containsKey(key)) {
            throw MachineException.getPropertyNotFoundException(key);
        }
        insert(index, value);
    }
}
