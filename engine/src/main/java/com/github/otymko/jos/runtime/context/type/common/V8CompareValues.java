/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.common;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

/**
 * Сравнение значений для использования в методах сортировки.
 */
@ContextClass(name = "СравнениеЗначений", alias = "CompareValues")
public class V8CompareValues extends ContextValue {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8CompareValues.class);

    @ContextConstructor
    public static IValue create() {
        return new V8CompareValues();
    }

    public V8CompareValues() {}

    @ContextMethod(name = "Сравнить", alias = "Compare")
    public IValue compare(IValue v1, IValue v2) {
        return ValueFactory.create(compareInternal(v1, v2));
    }

    int compareInternal(IValue v1, IValue v2) {
        var r1 = ValueFactory.rawValueOrUndefined(v1);
        var r2 = ValueFactory.rawValueOrUndefined(v2);
        try {
            return r1.compareTo(r2);
        } catch (Exception ignored) {
        }
        return r1.asString().compareTo(r2.asString());
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
