package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.IValue;
import lombok.Value;

@Value
public class ConstantDefinition {
  IValue value;

  public String toString() {
    return String.format("%s:%s", value.getDataType(), value.asString());
  }
}
