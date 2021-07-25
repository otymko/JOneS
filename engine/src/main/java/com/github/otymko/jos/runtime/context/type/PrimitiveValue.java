package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class PrimitiveValue implements IValue, ContextType, Comparable<IValue> {
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private DataType dataType;

  public float asNumber() {
    throw new RuntimeException("Not supported");
  }

  public Date asDate() {
    throw new RuntimeException("Not supported");
  }

  public boolean asBoolean() {
    throw new RuntimeException("Not supported");
  }

  public String asString() {
    return dataType.toString();
  }

  // TODO: asObject

  public IValue getRawValue() {
    return this;
  }

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public int compareTo(IValue o) {
    throw new RuntimeException("Not supported");
  }

  public static IValue parse(String view) {
    throw new RuntimeException("Not supported");
  }

}
