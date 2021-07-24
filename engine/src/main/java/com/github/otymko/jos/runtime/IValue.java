package com.github.otymko.jos.runtime;

import java.util.Date;

public interface IValue {
  DataType getDataType();

  // TypeDescriptor

  float asNumber();

  Date asDate();

  boolean asBoolean();

  String asString();

  // asObject

  IValue getRawValue();

  static IValue parse(String view) {
    return null;
  }

  int compareTo(IValue IValue);

}
