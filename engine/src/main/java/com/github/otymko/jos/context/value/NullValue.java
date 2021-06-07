package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;

public class NullValue extends GenericValue {
  public static final NullValue VALUE = new NullValue();

  private NullValue() {
    setDataType(DataType.GENERIC_VALUE);
  }

  public static Value parse(String view) {
    Value result;
    if (view.equalsIgnoreCase("null")) {
      result = ValueFactory.createNullValue();
    } else {
      throw new RuntimeException("constant type is not supported");
    }
    return result;
  }

  @Override
  public String asString() {
    return "";
  }

}
