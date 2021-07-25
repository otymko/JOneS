package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends ContextValue {
  private String name;
  private IValue value;
  private DataType dataType;

  public void setValue(IValue value) {
    this.value = value;
    setDataType(value.getDataType());
  }

  @Override
  public ContextInfo getContextInfo() {
    throw new RuntimeException("Не реализовано");
  }

}
