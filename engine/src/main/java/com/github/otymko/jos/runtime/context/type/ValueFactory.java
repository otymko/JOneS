/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;

import java.math.BigDecimal;
import java.util.Date;

public class ValueFactory {

  private ValueFactory() {
    // none
  }

  public static IValue create() {
    return UndefinedValue.VALUE;
  }

  public static IValue create(String value) {
    return StringValue.create(value);
  }

  public static IValue create(float value) {
    return NumberValue.create(BigDecimal.valueOf(value));
  }

  public static IValue create(BigDecimal value) {
    return NumberValue.create(value);
  }

  public static IValue create(int value) {
    return NumberValue.create(BigDecimal.valueOf(value));
  }

  public static IValue create(Date value) {
    return new DateValue(value);
  }

  public static IValue create(boolean value) {
    return value ? BooleanValue.TRUE : BooleanValue.FALSE;
  }

  public static IValue rawValueOrDefault(IValue inValue, IValue defaultValue) {
    if (inValue == null) {
      return defaultValue;
    }
    final var raw = inValue.getRawValue();
    if (raw.getDataType() == DataType.UNDEFINED) {
      return defaultValue;
    }
    return raw;
  }

  public static IValue rawValueOrUndefined(IValue inValue) {
    return rawValueOrDefault(inValue, create());
  }

  // invalidValue?

  public static IValue createNullValue() {
    return NullValue.VALUE;
  }

  // object

  public static IValue parse(String view, DataType type) {
    if (type == DataType.DATE) {
      return DateValue.parse(view);
    }
    throw MachineException.operationNotImplementedException();
  }

}
