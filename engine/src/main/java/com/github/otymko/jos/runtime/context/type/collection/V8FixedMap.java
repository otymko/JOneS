/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "ФиксированноеСоответствие", alias = "FixedMap")
public class V8FixedMap extends V8BaseMap {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8FixedMap.class);

    private V8FixedMap() {
        // nope
    }

    public V8FixedMap(V8Map source) {
        source.iterator().forEach(value -> {
            final var element = (V8KeyAndValue) value;
            data.put(element.getKey(), element.getValue());
        });
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextConstructor
    public static IValue constructor(V8Map source) {
        return new V8FixedMap(source);
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return getInternal(index).orElseThrow(MachineException::keyNotFound);
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException(index.asString());
    }
}
