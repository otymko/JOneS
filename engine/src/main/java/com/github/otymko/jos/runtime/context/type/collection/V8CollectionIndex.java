/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Индекс коллекции. Описывает набор ключей индекса и данные, проиндексированные по этим ключам.
 *
 * @see V8ValueTable
 */
@ContextClass(name = "ИндексКоллекции", alias = "CollectionIndex")
public class V8CollectionIndex extends ContextValue implements IndexAccessor, CollectionIterable<IValue> {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8CollectionIndex.class);

    private final List<IValue> fields;
    private final Map<V8CollectionKey, List<IValue>> data = new HashMap<>();
    private final IndexSourceCollection source;

    V8CollectionIndex(IndexSourceCollection source, List<IValue> fields) {
        this.fields = fields;
        this.source = source;
    }

    void columnRemoved(IValue column) {
        if (fields.contains(column)) {
            fields.remove(column);
            rebuild();
        }
    }

    public List<IValue> getValues(V8CollectionKey key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        return new ArrayList<>();
    }

    void rebuild() {
        final var allData = new ArrayList<IValue>();
        for (final var el: data.values()) {
            allData.addAll(el);
        }
        addAll(allData);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public void addElement(PropertyNameAccessor element) {
        final var key = V8CollectionKey.extract(fields, element);
        var list = data.get(key);
        if (list == null) {
            list = new ArrayList<>();
            data.put(key, list);
        }
        list.add((IValue)element);
    }

    public void addAll(Collection<? extends IValue> collection) {
        for (final var el: collection) {
            addElement((PropertyNameAccessor) el);
        }
    }

    public void removeElement(PropertyNameAccessor element) {
        final var key = V8CollectionKey.extract(fields, element);
        var list = data.get(key);
        if (list != null) {
            list.remove((IValue) element);
        }
    }

    public void clear() {
        data.clear();
    }

    public List<V8CollectionKey> keys() {
        return new ArrayList<>(data.keySet());
    }

    public List<IValue> data(V8CollectionKey key) {
        final var result = data.get(key);
        if (result == null) {
            return new ArrayList<>();
        }

        return result;
    }

    boolean fitsTo(Collection<IValue> others) {
        if (fields.size() != others.size()) {
            return false;
        }
        return fields.containsAll(others);
    }

    @Override
    public IteratorValue iterator() {
        var listOfStrings = new ArrayList<IValue>();
        for (var field : fields) {
            listOfStrings.add(ValueFactory.create(source.getName(field)));
        }
        return new IteratorValue(listOfStrings.iterator());
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        var rawIndex = ValueFactory.rawValueOrUndefined(index);
        if (rawIndex.getDataType() == DataType.NUMBER) {
            var intIndex = rawIndex.asNumber().intValueExact();
            return ValueFactory.create(source.getName(fields.get(intIndex)));
        }
        throw MachineException.invalidArgumentValueException();
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException("");
    }

    @Override
    public String asString() {
        var joiner = new StringJoiner(", ");
        for (var field: fields) {
            joiner.add(source.getName(field));
        }
        return joiner.toString();
    }
}
