/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.util.Common;

import java.util.Map;
import java.util.TreeMap;

/**
 * Абстрактная реализация типов "Структура" и "ФиксированнаяСтруктура".
 */
public abstract class V8AbstractStructure extends ContextValue implements IndexAccessor, PropertyNameAccessor,
        CollectionIterable<V8KeyAndValue> {
    protected final Map<IValue, IValue> values;
    protected final Map<String, IValue> views;

    protected static Map<String, IValue> copyTreeMap(Map<String, IValue> views) {
        var map = new TreeMap<String, IValue>(String.CASE_INSENSITIVE_ORDER);
        map.putAll(views);

        return map;
    }

    protected V8AbstractStructure(Map<IValue, IValue> values, Map<String, IValue> views) {
        this.values = values;
        this.views = views;
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public int count() {
        return values.size();
    }

    @ContextMethod(name = "Свойство", alias = "Property")
    public boolean hasProperty(IValue key, IVariable value) {
        if (!Common.isValidStringIdentifier(key)) {
            throw MachineException.invalidPropertyNameStructureException(key.asString());
        }

        var keyValue = key.asString();
        if (views.containsKey(keyValue)) {
            var objectKey = views.get(keyValue);
            if (value != null) {
                value.setValue(values.get(objectKey));
            }
            return true;
        } else {
            if (value != null) {
                value.setValue(ValueFactory.create());
            }
        }

        return false;
    }

    @Override
    public IValue getPropertyValue(IValue index) {
        return getValueInternal(index);
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return getValueInternal(index);
    }

    @Override
    public boolean hasProperty(IValue index) {
        var key = index.asString();
        if (!Common.isValidStringIdentifier(index)) {
            throw MachineException.invalidPropertyNameStructureException(key);
        }
        return views.containsKey(key);
    }

    @Override
    public IteratorValue iterator() {
        return V8KeyAndValue.iteratorOf(values.entrySet());
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
}
