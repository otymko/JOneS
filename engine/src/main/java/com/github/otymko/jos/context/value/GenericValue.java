package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public abstract class GenericValue implements Value, Comparable<Value> {
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
  public Value getRawValue() {
    return this;
  }

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public int compareTo(Value o) {
    throw new RuntimeException("Not supported");
  }

  public static Value parse(String view) {
    throw new RuntimeException("Not supported");
  }
}
