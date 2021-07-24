package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.type.BaseValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends BaseValue {
  private String name;
  private BaseValue value;

  public void setValue(BaseValue value) {
    this.value = value;
    setDataType(value.getDataType());
  }

}
