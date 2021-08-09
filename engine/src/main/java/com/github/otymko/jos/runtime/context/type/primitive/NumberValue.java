/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "Число", alias = "Number")
public class NumberValue extends PrimitiveValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(NumberValue.class);

  private final float value;

  private NumberValue(float value) {
    this.value = value;
    setDataType(DataType.NUMBER);
  }

  public static NumberValue create(float value) {
    return new NumberValue(value);
  }

  public static NumberValue create(int value) {
    return create((float) value);
  }

  @Override
  public boolean asBoolean() {
    return value != 0;
  }

  @Override
  public float asNumber() {
    return value;
  }

  @Override
  public String asString() {
    var number = asNumber();
    var longNumber = (long) number;
    if (number == longNumber) {
      return String.format("%d", longNumber);
    }
    return String.format("%s", number);
  }

  @Override
  public int compareTo(IValue object) {
    if (object.getDataType() == DataType.BOOLEAN || object.getDataType() == DataType.NUMBER) {
      return Float.compare(value, object.asNumber());
    }
    return super.compareTo(object);
  }

  public static IValue parse(String view) {
    float value;
    try {
      value = Float.parseFloat(view);
    } catch (NumberFormatException exception) {
      throw new RuntimeException("Преобразование к типу 'Число' не поддерживается");
    }
    return ValueFactory.create(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof IValue)) {
      return false;
    }
    var baseValue = (IValue) obj;
    if (baseValue.getDataType() == DataType.BOOLEAN || baseValue.getDataType() == DataType.NUMBER) {
      return value == baseValue.asNumber();
    }
    return false;
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
