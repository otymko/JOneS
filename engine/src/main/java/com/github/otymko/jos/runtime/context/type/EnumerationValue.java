/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.context.EnumValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.Date;

public class EnumerationValue implements ContextType, IValue {
  @Getter
  private final EnumerationContext owner;
  @Getter
  private final String name;
  @Getter
  private final String alias;

  public EnumerationValue(EnumerationContext owner, EnumValue enumValue) {
    this.owner = owner;
    var info = ContextInfo.createByEnumValue(enumValue);
    name = info.getName();
    alias = info.getAlias();
  }

  @Override
  public ContextInfo getContextInfo() {
    return owner.getContextInfo();
  }

  @Override
  public float asNumber() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public Date asDate() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public boolean asBoolean() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public String asString() {
    return getName();
  }

  @Override
  public IValue getRawValue() {
    return this;
  }

  @Override
  public DataType getDataType() {
    return DataType.GENERIC_VALUE;
  }

  @Override
  public int compareTo(IValue o) {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}