/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.context.EnumType;
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
  @Getter
  private final EnumType value;

  public EnumerationValue(EnumerationContext owner, EnumValue enumValue, EnumType value) {
    this.owner = owner;
    var info = ContextInfo.createByEnumValue(enumValue);

    this.name = info.getName();
    this.alias = info.getAlias();
    this.value = value;
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
  public int compareTo(IValue inputValue) {
    // FIXME: кидать исключение при сравнении разных типов (не EnumerationValue)
    if (inputValue == null || !(inputValue.getRawValue() instanceof EnumerationValue)) {
      return 1;
    }
    var rawValue = (EnumerationValue) inputValue.getRawValue();
    if (rawValue.getValue() == getValue()) {
      return 0;
    }
    return 1;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IValue) {
      return ((IValue) obj).getRawValue().equals(this);
    }
    return false;
  }
}
