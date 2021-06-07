package com.github.otymko.jos.context.value;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends GenericValue {
  private String name;
  private Value value;

  public void setValue(Value value) {
    this.value = value;
    setDataType(value.getDataType());
  }

}
