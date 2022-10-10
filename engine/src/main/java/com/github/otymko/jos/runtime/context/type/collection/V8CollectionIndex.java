/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.*;

@ContextClass(name = "ИндексКоллекции", alias = "CollectionIndex")
public class V8CollectionIndex extends ContextValue {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8CollectionIndex.class);

    private final List<IValue> fields;
    private final Map<V8CollectionKey, List<IValue>> data;

    public V8CollectionIndex(List<IValue> fields) {
        this.fields = fields;
        this.data = new Hashtable<>();
    }

    void columnRemoved(V8ValueTableColumn column) {
        if (fields.contains(column)) {
            fields.remove(column);
            rebuild();
        }
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
        return new ArrayList<V8CollectionKey>(data.keySet());
    }

    public List<IValue> data(V8CollectionKey key) {
        final var result = data.get(key);
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }
}
