package com.github.otymko.jos.runtime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends GenericIValue {
  private String name;
  private IValue value;

  public void setValue(IValue value) {
    this.value = value;
    setDataType(value.getDataType());
  }

}
