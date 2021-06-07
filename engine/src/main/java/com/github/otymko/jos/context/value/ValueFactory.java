package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;

import java.util.Date;

public class ValueFactory {

  private ValueFactory() {
    // none
  }

  public static Value create() {
    return UndefinedValue.VALUE;
  }

  public static Value create(String value) {
    return StringValue.create(value);
  }

  public static Value create(float value) {
    return NumberValue.create(value);
  }

  public static Value create(int value) {
    return NumberValue.create(value);
  }

  public static Value create(Date value) {
    return new DateValue(value);
  }

  public static Value create(boolean value) {
    return value ? BooleanValue.TRUE : BooleanValue.FALSE;
  }

  // invalidValue?

  public static Value createNullValue() {
    return NullValue.VALUE;
  }

  // object

  public static Value parse(String view, DataType type) {
    return null;
  }

}
