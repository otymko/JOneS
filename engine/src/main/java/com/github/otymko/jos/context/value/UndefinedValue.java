package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;

public class UndefinedValue extends GenericValue {
  public static final UndefinedValue VALUE = new UndefinedValue();

  private UndefinedValue() {
    setDataType(DataType.UNDEFINED);
  }

  @Override
  public String asString() {
    return "";
  }

  @Override
  public int compareTo(Value object) {
    if (object.getDataType() == getDataType()) {
      return 0;
    }
    return super.compareTo(object);
  }

}
