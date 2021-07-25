package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;

public class UndefinedValue extends PrimitiveValue {
  public static final UndefinedValue VALUE = new UndefinedValue();

  private UndefinedValue() {
    setDataType(DataType.UNDEFINED);
  }

  @Override
  public String asString() {
    return "";
  }

  @Override
  public int compareTo(IValue object) {
    if (object.getDataType() == getDataType()) {
      return 0;
    }
    return super.compareTo(object);
  }

}
