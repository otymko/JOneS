package com.github.otymko.jos.runtime.type.primitive;

import com.github.otymko.jos.runtime.DataType;
import com.github.otymko.jos.runtime.GenericIValue;
import com.github.otymko.jos.runtime.IValue;
import com.github.otymko.jos.runtime.ValueFactory;

public class NullValue extends GenericIValue {
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
