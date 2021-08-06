package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.IValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class ConstantDefinition {
  IValue value;

  public String toString() {
    return String.format("%s:%s", value.getDataType(), value.asString());
  }
}
