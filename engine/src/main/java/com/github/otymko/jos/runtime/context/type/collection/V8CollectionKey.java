/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class V8CollectionKey {

    private final Map<IValue, IValue> values;

    public V8CollectionKey(Map<IValue, IValue> values) {
        this.values = values;
    }

    public List<IValue> getFields()
    {
        return new ArrayList<IValue>(values.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        V8CollectionKey that = (V8CollectionKey) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    public static V8CollectionKey extract(List<IValue> fields, PropertyNameAccessor element) {

        final Map<IValue, IValue> result = new HashMap<>();

        for (final var field: fields) {
            if (element.hasProperty(field)) {
                result.put(field, element.getPropertyValue(field));
            }
        }

        return new V8CollectionKey(result);
    }
}
