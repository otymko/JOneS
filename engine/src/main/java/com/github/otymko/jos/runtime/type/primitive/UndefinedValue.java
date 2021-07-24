package com.github.otymko.jos.runtime.type.primitive;

import com.github.otymko.jos.runtime.type.BaseValue;
import com.github.otymko.jos.runtime.type.DataType;

public class UndefinedValue extends BaseValue {
  public static final UndefinedValue VALUE = new UndefinedValue();

  private UndefinedValue() {
    setDataType(DataType.UNDEFINED);
  }

  @Override
  public String asString() {
    return "";
  }

  @Override
  public int compareTo(BaseValue object) {
    if (object.getDataType() == getDataType()) {
      return 0;
    }
    return super.compareTo(object);
  }

}
