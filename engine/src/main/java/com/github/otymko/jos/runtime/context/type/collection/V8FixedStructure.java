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
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.util.Common;

import java.util.Collections;
import java.util.HashMap;

/**
 * Фиксированная коллекция из КлючИЗначение. Хранит ключи как свойства объекта.
 *
 * @see V8KeyAndValue
 */
@ContextClass(name = "ФиксированнаяСтруктура", alias = "FixedStructure")
public class V8FixedStructure extends V8AbstractStructure {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8FixedStructure.class);

    @ContextConstructor
    public static V8FixedStructure constructor() {
        return new V8FixedStructure();
    }

    @ContextConstructor
    public static V8FixedStructure constructor(IValue value) {
        if (value.getRawValue() instanceof V8Structure) {
            return new V8FixedStructure((V8Structure)value);
        } else if (value.getDataType() == DataType.STRING) {
            return constructorExtended(value);
        }

        throw MachineException.invalidArgumentValueException();
    }

    @ContextConstructor
    public static V8FixedStructure constructorExtended(IValue keysOrFixedStructure, IValue... values) {
        var structure = V8Structure.constructorExtended(keysOrFixedStructure, values);

        return new V8FixedStructure(structure);
    }

    public V8FixedStructure() {
        super(Collections.emptyMap(), Collections.emptyMap());
    }

    public V8FixedStructure(V8Structure structure) {
        super(new HashMap<>(structure.values), copyTreeMap(structure.views));
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        var key = index.asString();
        if (!Common.isValidStringIdentifier(index)) {
            throw MachineException.invalidPropertyNameStructureException(key);
        }

        if (!hasProperty(index)) {
            throw MachineException.objectFiledNotFound(key);
        }

        throw MachineException.objectFieldIsNotWritable(index.asString());
    }

    @Override
    public void setPropertyValue(IValue index, IValue value) {
        setIndexedValue(index, value);
    }
}
