/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Абстрактная реализация коллекции для `Соответствие` И `ФиксированноеСоответствие`
 */
public abstract class V8BaseMap extends ContextValue implements IndexAccessor, CollectionIterable<V8KeyAndValue> {
    protected final Map<IValue, IValue> data = new HashMap<>();

    // region ContextMethod

    @ContextMethod(name = "Количество", alias = "Count")
    public int count() {
        return data.size();
    }

    @ContextMethod(name = "Получить", alias = "Get")
    public IValue get(IValue key) {
        return getInternal(key).orElse(ValueFactory.create());
    }

    // endregion

    // region CollectionIterable

    @Override
    public IteratorValue iterator() {
        return V8KeyAndValue.iteratorOf(data.entrySet());
    }

    protected Optional<IValue> getInternal(IValue key) {
        final var rawKey = ValueFactory.rawValueOrUndefined(key);
        if (data.containsKey(rawKey)) {
            return Optional.of(data.get(rawKey));
        }

        return Optional.empty();
    }

    // endregion
}
