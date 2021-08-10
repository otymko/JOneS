/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;

@ContextClass(name = "Массив", alias = "Array")
public class ArrayImpl extends ContextValue implements IndexAccessor {
  public static final ContextInfo INFO = ContextInfo.createByClass(ArrayImpl.class);

  private final List<IValue> values;

  public ArrayImpl() {
    values = new ArrayList<>();
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  // TODO: конструктор

  @ContextConstructor
  public static ArrayImpl constructor() {
    return new ArrayImpl();
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
  public void insert(int index, IValue value) {
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
    }
    return ValueFactory.create();
  }

  @ContextMethod(name = "Удалить", alias = "Delete")
  public void delete(int index) {
    values.remove(index);
  }

  @ContextMethod(name = "ВГраница", alias = "UBound")
  public int upperBound() {
    return values.size() - 1;
  }

  @ContextMethod(name = "Получить", alias = "Get")
  public IValue get(int index) {
    return values.get(index);
  }

  @ContextMethod(name = "Установить", alias = "Set")
  public void set(int index, IValue value) {
    if (index < 0 || index >= values.size()) {
      throw MachineException.indexValueOutOfRangeException();
    }
    values.set(index, value);
  }

  private void extend(int count) {
    for (var index = 0; index < count; index++) {
      values.add(ValueFactory.create());
    }
  }

}
