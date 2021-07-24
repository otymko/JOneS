package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.type.BaseValue;
import lombok.Value;

@Value
public class ConstantDefinition {
  BaseValue value;

  public String toString() {
    return String.format("%s:%s", value.getDataType(), value.asString());
  }
}
