/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Индексы коллекции.
 * Для объекта доступен обход коллекции посредством оператора Для Каждого.
 *
 * @see V8CollectionIndex
 * @see V8ValueTable
 */
@ContextClass(name = "ИндексыКоллекции", alias="CollectionIndexes")
public class V8CollectionIndexes extends ContextValue implements IndexAccessor, CollectionIterable<V8CollectionIndex> {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8CollectionIndexes.class);

    private final List<IValue> values;
    private final CollectionNamesResolver resolver;

    public V8CollectionIndexes(CollectionNamesResolver resolver) {
        values = new ArrayList<>();
        this.resolver = resolver;
    }

    @ContextMethod(name = "Добавить", alias = "Add")
    public V8CollectionIndex add(String columnNames) {
        var index = new V8CollectionIndex(resolver, resolver.parseNames(columnNames));
        values.add(index);
        return index;
    }

    @ContextMethod(name = "Очистить", alias = "clear")
    public void clear() {
        for (var index: values) {
            ((V8CollectionIndex)index).clear();
        }
        values.clear();
    }

    @ContextMethod(name = "Удалить", alias = "Delete")
    public void delete(IValue index) {
        var indexValue = getIndexInternal(index);
        indexValue.clear();
        values.remove(indexValue);
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public int count() {
        return values.size();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(values.iterator());
    }

    private int indexInternal(IValue index) {
        if (index == null) {
            throw MachineException.invalidArgumentValueException();
        }
        final var rawIndex = index.getRawValue();
        if (rawIndex.getDataType() != DataType.NUMBER) {
            throw MachineException.invalidArgumentValueException();
        }
        final var intIndex = rawIndex.asNumber().intValue();
        if (intIndex >= 0 && intIndex < values.size()) {
            return intIndex;
        }
        throw MachineException.indexValueOutOfRangeException();
    }

    private V8CollectionIndex getIndexInternal(IValue index) {
        if (index == null) {
            throw MachineException.invalidArgumentValueException();
        }
        final var rawIndex = index.getRawValue();
        if (rawIndex.getDataType() == DataType.NUMBER) {
            final var intIndex = rawIndex.asNumber().intValue();
            if (intIndex >= 0 && intIndex < values.size()) {
                return (V8CollectionIndex) values.get(intIndex);
            }
            throw MachineException.indexValueOutOfRangeException();
        }
        if (rawIndex instanceof V8CollectionIndex
            && values.contains(rawIndex)) {
            return (V8CollectionIndex) rawIndex;
        }
        throw MachineException.invalidArgumentValueException();
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return getIndexInternal(index);
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException("");
    }
}
