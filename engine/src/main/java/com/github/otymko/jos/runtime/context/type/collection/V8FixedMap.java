/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ContextClass(name = "ФиксированноеСоответствие", alias = "FixedMap")
public class V8FixedMap extends ContextValue implements IndexAccessor,
        CollectionIterable<V8KeyAndValue> {

  public static final ContextInfo INFO = ContextInfo.createByClass(V8FixedMap.class);

  private final Map<IValue, IValue> data = new HashMap<>();

  private V8FixedMap() {}

  public V8FixedMap(V8Map source) {
    for (final var value : source.iterator()) {
      final var element = (V8KeyAndValue)value;
      data.put(element.key, element.value);
    }
  }

  @ContextConstructor
  public static IValue constructor(IValue source) {
    final var map = (V8Map) source.getRawValue();
    if (map == null) {
      throw MachineException.invalidArgumentValueException();
    }
    return new V8FixedMap(map);
  }

  @ContextMethod(name = "Количество", alias = "Count")
  public IValue count() {
    return ValueFactory.create(data.size());
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

  private Optional<IValue> getInternal(IValue key) {
    final var rawKey = ValueFactory.rawValueOrUndefined(key);
    if (data.containsKey(rawKey)) {
      return Optional.of(data.get(rawKey));
    }
    return Optional.empty();
  }

  @ContextMethod(name = "Получить", alias = "Get")
  public IValue get(IValue key) {
    return getInternal(key).orElse(ValueFactory.create());
  }

  @Override
  public IteratorValue iterator() {
    return V8KeyAndValue.iteratorOf(data.entrySet());
  }

  @Override
  public IValue getIndexedValue(IValue index) {
    return getInternal(index).orElseThrow();
  }

  @Override
  public void setIndexedValue(IValue index, IValue value) {
    throw MachineException.getPropertyIsNotWritableException(index.asString());
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
