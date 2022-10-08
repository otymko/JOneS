/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.util.Common;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@ContextClass(name = "Структура", alias = "Structure")
public class V8Structure extends ContextValue implements IndexAccessor, PropertyNameAccessor,
        CollectionIterable<V8KeyAndValue> {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8Structure.class);

    private final Map<IValue, IValue> values;
    private final Map<String, IValue> views;

    private V8Structure() {
        values = new HashMap<>();
        views = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    @ContextConstructor
    public static V8Structure constructor() {
        return new V8Structure();
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public void insert(IValue key, IValue value) {
        var keyValue = key.asString();
        if (!Common.isValidStringIdentifier(key)) {
            throw MachineException.invalidPropertyNameStructureException(keyValue);
        }

        var addingValue = value == null ? ValueFactory.create() : value;
        if (views.containsKey(keyValue)) {
            var objectKey = views.get(keyValue);
            values.put(objectKey, addingValue);
        } else {
            views.put(keyValue, key);
            values.put(key, addingValue);
        }
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public IValue count() {
        return ValueFactory.create(values.size());
    }

    @ContextMethod(name = "Очистить", alias = "Clear")
    public void clear() {
        values.clear();
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

    @ContextMethod(name = "Свойство", alias = "Property")
    public IValue hasProperty(IValue key, Variable value) {
        if (!Common.isValidStringIdentifier(key)) {
            throw MachineException.invalidPropertyNameStructureException(key.asString());
        }

        var keyValue = key.asString();
        if (views.containsKey(keyValue)) {
            var objectKey = views.get(keyValue);
            if (value != null) {
                value.setValue(values.get(objectKey));
            }
            return ValueFactory.create(true);
        } else {
            if (value != null) {
                value.setValue(ValueFactory.create());
            }
        }

        return ValueFactory.create(false);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return getValueInternal(index);
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        setValueInternal(index, value);
    }

    @Override
    public IValue getPropertyValue(IValue index) {
        return getValueInternal(index);
    }

    @Override
    public void setPropertyValue(IValue index, IValue value) {
        setValueInternal(index, value);
    }

    @Override
    public boolean hasProperty(IValue index) {
        var key = index.asString();
        if (!Common.isValidStringIdentifier(index)) {
            throw MachineException.invalidPropertyNameStructureException(key);
        }
        return views.containsKey(key);
    }

    private IValue getValueInternal(IValue index) {
        var key = index.asString();
        if (!Common.isValidStringIdentifier(index)) {
            throw MachineException.invalidPropertyNameStructureException(key);
        }

        if (!views.containsKey(key)) {
            throw MachineException.getPropertyNotFoundException(key);
        }
        var keyObject = views.get(key);
        return values.get(keyObject);
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

    @Override
    public IteratorValue iterator() {
        return V8KeyAndValue.iteratorOf(values.entrySet());
    }
}
