package com.github.otymko.jos.runtime.type;

import com.github.otymko.jos.runtime.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.type.primitive.DateValue;
import com.github.otymko.jos.runtime.type.primitive.NullValue;
import com.github.otymko.jos.runtime.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.type.primitive.StringValue;
import com.github.otymko.jos.runtime.type.primitive.UndefinedValue;

import java.util.Date;

public class ValueFactory {

  private ValueFactory() {
    // none
  }

  public static BaseValue create() {
    return UndefinedValue.VALUE;
  }

  public static BaseValue create(String value) {
    return StringValue.create(value);
  }

  public static BaseValue create(float value) {
    return NumberValue.create(value);
  }

  public static BaseValue create(int value) {
    return NumberValue.create(value);
  }

  public static BaseValue create(Date value) {
    return new DateValue(value);
  }

  public static BaseValue create(boolean value) {
    return value ? BooleanValue.TRUE : BooleanValue.FALSE;
  }

  // invalidValue?

  public static BaseValue createNullValue() {
    return NullValue.VALUE;
  }

  // object

  public static BaseValue parse(String view, DataType type) {
    return null;
  }

}
