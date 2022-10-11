/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.HashMap;
import java.util.Map;

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
    public IValue owner() {
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

    @Override
    public IValue getIndexedValue(IValue index) {
        final var column = owner.getColumns().getColumnInternal(index);
        if (data.containsKey(column)) {
            return column.adjustValue(data.get(column));
        }
        return column.getDefaultValue();
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        final var column = owner.getColumns().getColumnInternal(index);
        data.put(column, column.adjustValue(value));
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
