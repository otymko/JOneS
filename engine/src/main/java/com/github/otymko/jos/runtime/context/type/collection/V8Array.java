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
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.otymko.jos.runtime.machine.context.ContextValueConverter.convertValue;

/**
 * Реализация типа коллекции "Массив"
 */
@ContextClass(name = "Массив", alias = "Array")
public class V8Array extends ContextValue implements IndexAccessor, CollectionIterable {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8Array.class);

    @Getter(AccessLevel.PACKAGE)
    private final List<IValue> values;

    /**
     * Создать пустой массив.
     */
    @ContextConstructor
    public static V8Array create() {
        return new V8Array();
    }

    /**
     * Создать новый массив на основании списка значений.
     *
     * @param values список значений.
     */
    @ContextConstructor
    public static IValue createByValues(IValue... values) {
        IValue firstValue = values[0];
        if (firstValue instanceof V8FixedArray) {
            return new V8Array((V8FixedArray)firstValue);
        } else if (firstValue.getDataType() == DataType.NUMBER) {
            return new V8Array(values);
        }

        throw new IllegalArgumentException();
    }

    private V8Array(V8FixedArray array) {
        values = new ArrayList<>(array.getValues());
    }
    private V8Array() {
        values = new ArrayList<>();
    }

    private V8Array(IValue... numbers) {
        values = new ArrayList<>(Arrays.asList(numbers));
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public IValue count() {
        return ValueFactory.create(values.size());
    }

    @ContextMethod(name = "Очистить", alias = "Clear")
    public void clear() {
        values.clear();
    }

    @ContextMethod(name = "Добавить", alias = "Add")
    public void add(IValue value) {
        values.add(value);
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public void insert(int index, IValue value) {
        if (index < 0) {
            throw MachineException.indexValueOutOfRangeException();
        }

        if (index > values.size()) {
            extend(index - values.size());
        }
        if (value == null) {
            values.set(index, ValueFactory.create());
        } else {
            values.set(index, value);
        }
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

    @ContextMethod(name = "Удалить", alias = "Delete")
    public void delete(int index) {
        values.remove(index);
    }

    @ContextMethod(name = "ВГраница", alias = "UBound")
    public IValue upperBound() {
        return ValueFactory.create(values.size() - 1);
    }

    @ContextMethod(name = "Получить", alias = "Get")
    public IValue get(int index) {
        return values.get(index);
    }

    @ContextMethod(name = "Установить", alias = "Set")
    public void set(int index, IValue value) {
        if (index < 0 || index >= values.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        values.set(index, value);
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return get(convertValue(index, int.class));
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        set(convertValue(index, int.class), value);
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(values.iterator());
    }

    private void extend(int count) {
        for (var index = 0; index <= count; index++) {
            values.add(ValueFactory.create());
        }
    }

}
