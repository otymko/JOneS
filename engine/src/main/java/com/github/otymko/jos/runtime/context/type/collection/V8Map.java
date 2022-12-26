/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "Соответствие", alias = "Map")
public class V8Map extends V8BaseMap {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8Map.class);

    @ContextConstructor
    public static V8Map constructor() {
        return new V8Map();
    }

    @ContextConstructor
    public static V8Map constructor(V8FixedMap fixedMap) {
        final var result = new V8Map();
        for (final var value : fixedMap.iterator()) {
            final var element = (V8KeyAndValue) value;
            result.insert(element.getKey(), element.getValue());
        }

        return result;
    }

    private V8Map() {
        // nope
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    // region ContextMethod

    @ContextMethod(name = "Вставить", alias = "Insert")
    public void insert(IValue key, IValue value) {
        final var rawKey = ValueFactory.rawValueOrUndefined(key);
        final var rawValue = ValueFactory.rawValueOrUndefined(value);
        data.put(rawKey, rawValue);
    }

    @ContextMethod(name = "Очистить", alias = "Clear")
    public void clear() {
        data.clear();
    }

    @ContextMethod(name = "Удалить", alias = "Delete")
    public void remove(IValue key) {
        final var rawKey = ValueFactory.rawValueOrUndefined(key);
        data.remove(rawKey);
    }

    // endregion

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        insert(index, value);
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return getInternal(index).orElse(ValueFactory.create());
    }

}
