/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Строка таблицы значений.
 * Запись, соответствующая структуре Таблицы Значений.
 * Для объекта доступен обход коллекции посредством оператора Для Каждого.
 * Возможно обращение к элементу коллекции посредством оператора [].
 * В качестве индекса передается индекс элемента или имя колонки.
 *
 * @see V8ValueTable
 */
@ContextClass(name = "СтрокаТаблицыЗначений", alias = "ValueTableRow")
public class V8ValueTableRow extends ContextValue implements IndexAccessor, PropertyNameAccessor,
        CollectionIterable<V8KeyAndValue> {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueTableRow.class);

    private final Map<V8ValueTableColumn, IValue> data;
    private final V8ValueTable owner;

    public V8ValueTableRow(V8ValueTable owner) {
        data = new HashMap<>();
        this.owner = owner;
    }

    @ContextMethod(name="Владелец", alias="Owner")
    public V8ValueTable owner() {
        return owner;
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(data.values().iterator());
    }

    IValue getIndexedValueInternal(V8ValueTableColumn column) {
        if (data.containsKey(column)) {
            return column.adjustValue(data.get(column));
        }
        return column.getDefaultValue();
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        final var column = owner.getColumns().getColumnInternal(index);
        return getIndexedValueInternal(column);
    }

    void setIndexedValueInternal(V8ValueTableColumn column, IValue value) {
        data.put(column, column.adjustValue(value));
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        final var column = owner.getColumns().getColumnInternal(index);
        setIndexedValueInternal(column, value);
    }

    @Override
    public IValue getPropertyValue(IValue index) {
        return getIndexedValue(index);
    }

    @Override
    public void setPropertyValue(IValue index, IValue value) {
        setIndexedValue(index, value);
    }

    @Override
    public boolean hasProperty(IValue index) {
        return owner.getColumns().hasProperty(index);
    }

    @ContextMethod(name="Получить", alias="Get")
    public IValue get(IValue index) {
        return getIndexedValue(index);
    }

    void columnDeleted(V8ValueTableColumn column) {
        data.remove(column);
    }
}
