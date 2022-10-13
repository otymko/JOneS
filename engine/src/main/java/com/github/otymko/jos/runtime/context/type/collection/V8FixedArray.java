/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;

import static com.github.otymko.jos.runtime.machine.context.ContextValueConverter.convertValue;

/**
 * Реализация типа `ФиксированныйМассив`
 */
@ContextClass(name = "ФиксированныйМассив", alias = "FixedArray")
public class V8FixedArray extends ContextValue implements IndexAccessor, CollectionIterable {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8FixedArray.class);

    @Getter(AccessLevel.PACKAGE)
    private final List<IValue> values;

    @ContextConstructor
    public static IValue createByV8Array(V8Array array) {
        return new V8FixedArray(array);
    }

    private V8FixedArray(V8Array array) {
        values = List.copyOf(array.getValues());
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public IValue count() {
        return ValueFactory.create(values.size());
    }

    @ContextMethod(name = "ВГраница", alias = "UBound")
    public IValue upperBound() {
        return ValueFactory.create(values.size() - 1);
    }

    @ContextMethod(name = "Получить", alias = "Get")
    public IValue get(int index) {
        return values.get(index);
    }

    @ContextMethod(name = "Найти", alias = "Find")
    public IValue find(IValue inValue) {
        var index = 0;
        while (index < values.size()) {
            var value = values.get(index);
            if (value.equals(inValue)) {
                return ValueFactory.create(index);
            }
            index++;
        }
        return ValueFactory.create();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(values.iterator());
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return get(convertValue(index, int.class));
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.indexedValueIsReadOnly();
    }
}
