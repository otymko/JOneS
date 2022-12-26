/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.machine.StackTraceRecord;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Коллекция кадров стека вызовов. Исключение в информации об ошибке.
 */
@ContextClass(name = "КоллекцияКадровСтекаВызовов", alias = "CallStackFramesCollection")
public class StackTraceCollectionContext extends ContextValue implements IndexAccessor,
        CollectionIterable<StackTraceItemContext> {

    public static final ContextInfo INFO = ContextInfo.createByClass(StackTraceCollectionContext.class);
    /**
     * Кадры стека вызовов.
     */
    private final List<IValue> frames;

    public StackTraceCollectionContext(List<StackTraceRecord> frames) {
        this.frames = frames.stream()
                .map(StackTraceItemContext::new)
                .collect(Collectors.toList());
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(frames.iterator());
    }

    @Override
    public IValue getIndexedValue(IValue sourceIndex) {
        if (sourceIndex.getDataType() != DataType.NUMBER) {
            throw MachineException.invalidArgumentValueException();
        }

        int index = sourceIndex.getRawValue().asNumber().intValue();
        if (index < 0) {
            throw MachineException.invalidArgumentValueException();
        }

        return frames.get(index);
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.indexedValueIsReadOnly();
    }
}
