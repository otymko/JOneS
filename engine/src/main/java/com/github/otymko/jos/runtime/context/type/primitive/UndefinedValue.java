package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "Неопределено", alias = "Undefined")
public class UndefinedValue extends PrimitiveValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(UndefinedValue.class);

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

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
