package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    if (value instanceof ContextValue) {
      return ((ContextValue) value).getContextInfo();
    }
    throw new RuntimeException("Не реализовано");
  }

  @Override
  public String asString() {
    return value.asString();
  }

  @Override
  public float asNumber() {
    return value.asNumber();
  }

  @Override
  public Date asDate() {
    return value.asDate();
  }

  @Override
  public boolean asBoolean() {
    return super.asBoolean();
  }

  @Override
  public IValue getRawValue() {
    return value;
  }

}
