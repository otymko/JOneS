package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;

public class NullValue extends PrimitiveValue {
  public static final NullValue VALUE = new NullValue();

  private NullValue() {
    setDataType(DataType.GENERIC_VALUE);
  }

  public static IValue parse(String view) {
    IValue result;
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
