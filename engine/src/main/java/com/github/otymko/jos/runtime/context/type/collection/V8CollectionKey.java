/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class V8CollectionKey {
    private final Map<IValue, IValue> values;

    public static V8CollectionKey extract(List<IValue> fields, PropertyNameAccessor element) {

        final Map<IValue, IValue> result = new HashMap<>();

        for (final var field: fields) {
            if (element.hasProperty(field)) {
                result.put(field, element.getPropertyValue(field));
            }
        }

        return new V8CollectionKey(result);
    }

    public V8CollectionKey(Map<IValue, IValue> values) {
        this.values = values;
    }

    public List<IValue> getFields()
    {
        return new ArrayList<>(values.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        V8CollectionKey that = (V8CollectionKey) o;
        if (values.size() != that.values.size()) {
            return false;
        }
        var allKeys = new HashSet<IValue>();
        allKeys.addAll(values.keySet());
        allKeys.addAll(that.values.keySet());
        for (var key: allKeys) {
            if (!that.values.containsKey(key)
            || !values.containsKey(key)) {
                return false;
            }
            var v1 = values.get(key);
            var v2 = that.values.get(key);
            if (!v1.equals(v2)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
