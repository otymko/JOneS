package com.github.otymko.jos.runtime.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public abstract class BaseValue implements Comparable<BaseValue> {
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

  public BaseValue getRawValue() {
    return this;
  }

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public int compareTo(BaseValue o) {
    throw new RuntimeException("Not supported");
  }

  public static BaseValue parse(String view) {
    throw new RuntimeException("Not supported");
  }
}
