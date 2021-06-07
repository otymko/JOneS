package com.github.otymko.jos.compiler;

import com.github.otymko.jos.context.value.Value;

@lombok.Value
public class ConstantDefinition {
  Value value;

  public String toString() {
    return String.format("%s:%s", value.getDataType(), value.asString());
  }
}
