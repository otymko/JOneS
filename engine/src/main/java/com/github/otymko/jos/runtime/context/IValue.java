package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.runtime.context.type.DataType;

import java.util.Date;

public interface IValue {

  float asNumber();

  Date asDate();

  boolean asBoolean();

  String asString();

  IValue getRawValue();

  DataType getDataType();

  int compareTo(IValue o);

}
