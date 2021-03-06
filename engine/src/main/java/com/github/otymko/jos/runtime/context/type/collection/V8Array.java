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

import java.util.ArrayList;
import java.util.List;

@ContextClass(name = "Массив", alias = "Array")
public class V8Array extends ContextValue implements IndexAccessor, CollectionIterable {
  public static final ContextInfo INFO = ContextInfo.createByClass(V8Array.class);

  private final List<IValue> values;

  public V8Array() {
    values = new ArrayList<>();
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  // TODO: конструктор

  @ContextConstructor
  public static V8Array constructor() {
    return new V8Array();
  }

  @ContextMethod(name = "Количество", alias = "Count")
  public IValue count() {
    return ValueFactory.create(values.size());
  }

  @ContextMethod(name = "Очистить", alias = "Clear")
  public void clear() {
    values.clear();
  }

  @ContextMethod(name = "Добавить", alias = "Add")
  public void add(IValue value) {
    values.add(value);
  }

  @ContextMethod(name = "Вставить", alias = "Insert")
  public void insert(IValue inputIndex, IValue value) {
    var index = inputIndex.getRawValue().asNumber().intValue();
    if (index < 0) {
      throw MachineException.indexValueOutOfRangeException();
    }
    if (index > values.size()) {
      extend(index - values.size());
    }
    if (value == null) {
      values.set(index, ValueFactory.create());
    } else {
      values.set(index, value);
    }
  }

  @ContextMethod(name = "Найти", alias = "Find")
  public IValue find(IValue inValue) {
    var index = 0;
    while (index < values.size()) {
      var value = values.get(index);
      if (value.equals(inValue)) {
        return ValueFactory.create(index);
      }
      index++;
    }
    return ValueFactory.create();
  }

  @ContextMethod(name = "Удалить", alias = "Delete")
  public void delete(IValue inputIndex) {
    var index = inputIndex.getRawValue().asNumber().intValue();
    values.remove(index);
  }

  @ContextMethod(name = "ВГраница", alias = "UBound")
  public IValue upperBound() {
    return ValueFactory.create(values.size() - 1);
  }

  @ContextMethod(name = "Получить", alias = "Get")
  public IValue get(IValue inputIndex) {
    var index = inputIndex.getRawValue().asNumber().intValue();
    return values.get(index);
  }

  @ContextMethod(name = "Установить", alias = "Set")
  public void set(IValue inputIndex, IValue value) {
    var index = inputIndex.getRawValue().asNumber().intValue();
    if (index < 0 || index >= values.size()) {
      throw MachineException.indexValueOutOfRangeException();
    }
    values.set(index, value);
  }

  @Override
  public IValue getIndexedValue(IValue index) {
    return get(index);
  }

  @Override
  public void setIndexedValue(IValue index, IValue value) {
    set(index, value);
  }

  @Override
  public IteratorValue iterator() {
    return new IteratorValue(values.iterator());
  }

  private void extend(int count) {
    for (var index = 0; index <= count; index++) {
      values.add(ValueFactory.create());
    }
  }

}
