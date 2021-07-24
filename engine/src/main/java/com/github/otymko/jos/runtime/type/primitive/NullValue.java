package com.github.otymko.jos.runtime.type.primitive;

import com.github.otymko.jos.runtime.type.BaseValue;
import com.github.otymko.jos.runtime.type.DataType;
import com.github.otymko.jos.runtime.type.ValueFactory;

public class NullValue extends BaseValue {
  public static final NullValue VALUE = new NullValue();

  private NullValue() {
    setDataType(DataType.GENERIC_VALUE);
  }

  public static BaseValue parse(String view) {
    BaseValue result;
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
