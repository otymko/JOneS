/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.Objects;

@ContextClass(name = "Число", alias = "Number")
public class NumberValue extends PrimitiveValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(NumberValue.class);

  private final BigDecimal value;

  private NumberValue(BigDecimal value) {
    if(value == null) {
      throw new IllegalArgumentException();
    }

    this.value = value;
    setDataType(DataType.NUMBER);
  }

  public static NumberValue create(BigDecimal value) {
    return new NumberValue(value);
  }

  @Override
  public boolean asBoolean() {
    return value.intValue() != 0;
  }

  @Override
  public BigDecimal asNumber() {
    return value;
  }

  @Override
  public String asString() {
    var number = asNumber();
    return number.toPlainString();
  }

  @Override
  public int compareTo(IValue object) {
    if (object.getDataType() == DataType.BOOLEAN || object.getDataType() == DataType.NUMBER) {
      return value.compareTo(object.asNumber());
    }
    return super.compareTo(object);
  }

  public static IValue parse(String view) {
    BigDecimal value;
    try {
      value = new BigDecimal(view);
    } catch (NumberFormatException exception) {
      throw MachineException.convertToNumberException();
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
      return value.equals(baseValue.asNumber());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
