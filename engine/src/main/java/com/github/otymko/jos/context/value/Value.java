package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;

import java.util.Date;

public interface Value {
  DataType getDataType();

  // TypeDescriptor

  float asNumber();

  Date asDate();

  boolean asBoolean();

  String asString();

  // asObject

  Value getRawValue();

  static Value parse(String view) {
    return null;
  }

  int compareTo(Value value);

}
