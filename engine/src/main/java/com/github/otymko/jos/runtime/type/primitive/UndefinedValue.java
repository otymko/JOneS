package com.github.otymko.jos.runtime.type.primitive;

import com.github.otymko.jos.runtime.DataType;
import com.github.otymko.jos.runtime.GenericIValue;
import com.github.otymko.jos.runtime.IValue;

public class UndefinedValue extends GenericIValue {
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
