package com.github.otymko.jos.runtime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public abstract class GenericIValue implements IValue, Comparable<IValue> {
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private DataType dataType;

  // equals

  @Override
  public float asNumber() {
    throw new RuntimeException("Not supported");
  }

  @Override
  public Date asDate() {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean asBoolean() {
    throw new RuntimeException("Not supported");
  }

  @Override
  public String asString() {
    return dataType.toString();
  }

  // asObject

  @Override
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
