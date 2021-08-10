/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.HashMap;
import java.util.Map;

@ContextClass(name = "Структура", alias = "Structure")
public class StructureImpl extends ContextValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(StructureImpl.class);

  private final Map<IValue, IValue> values;

  private StructureImpl() {
    values = new HashMap<>();
  }

  @ContextConstructor
  public static StructureImpl constructor() {
    return new StructureImpl();
  }

  @ContextMethod(name = "Вставить", alias = "Insert")
  public void insert(IValue key, IValue value) {
    if (!(key.getDataType() == DataType.STRING)) {
      throw MachineException.invalidPropertyNameStructureException(key.asString());
    }

    if (value == null) {
      values.put(key, ValueFactory.create());
    } else {
      values.put(key, value);
    }
  }

  @ContextMethod(name = "Количество", alias = "Count")
  public IValue count() {
    return ValueFactory.create(values.size());
  }

  @ContextMethod(name = "Очистить", alias = "Clear")
  public void clear() {
    values.clear();
  }

  @ContextMethod(name = "Удалить", alias = "Delete")
  public void remove(IValue key) {
    if (!(key.getDataType() == DataType.STRING)) {
      throw MachineException.invalidPropertyNameStructureException(key.asString());
    }

    values.remove(key);
  }

  @ContextMethod(name = "Свойство", alias = "Property")
  public IValue hasProperty(IValue key, Variable value) {
    if (!(key.getDataType() == DataType.STRING)) {
      throw MachineException.invalidPropertyNameStructureException(key.asString());
    }

    if (values.containsKey(key)) {
      if (value != null) {
        value.setValue(values.get(key));
      }
      return ValueFactory.create(true);
    }

    return ValueFactory.create(false);
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

}
