/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends ContextValue implements IVariable {
  private String name;
  private IValue value;
  private DataType dataType;

  public void setValue(IValue value) {
    this.value = value;
    setDataType(value.getDataType());
  }

  @Override
  public ContextInfo getContextInfo() {
    if (getValue() instanceof ContextValue) {
      return ((ContextValue) getValue()).getContextInfo();
    }
    throw MachineException.operationNotImplementedException();
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
    return value.asBoolean();
  }

  @Override
  public IValue getRawValue() {
    return value;
  }

  public static Variable create(IValue value, String name) {
    var variable = new Variable();
    variable.setName(name);
    variable.setValue(value);
    return variable;
  }

}
