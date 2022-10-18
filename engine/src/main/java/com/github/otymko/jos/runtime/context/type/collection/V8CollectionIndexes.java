/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

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

    public V8CollectionIndexes() {
        values = new ArrayList<>();
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

    @Override
    public IValue getIndexedValue(IValue index) {
        return values.get(indexInternal(index));
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException("");
    }
}
