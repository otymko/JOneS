/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.EnumValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.type.EnumerationValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class EnumerationContext extends ContextValue implements PropertyNameAccessor {
  private final ContextInfo info;

  @Getter
  private final List<EnumerationValue> values = new ArrayList<>();

  public EnumerationContext(Class<? extends EnumType> enumTargetClass) {
    var enumClass = enumTargetClass.getAnnotation(EnumClass.class);
    if (enumClass == null) {
      throw MachineException.operationNotSupportedException();
    }

    this.info = ContextInfo.createByEnumClass(enumClass);
    for (var field : enumTargetClass.getFields()) {
      var enumValue = field.getAnnotation(EnumValue.class);
      if (enumValue == null) {
        continue;
      }
      var value = new EnumerationValue(this, enumValue);
      values.add(value);
    }
  }

  @Override
  public String asString() {
    // TODO: вынести в конструктор
    return "Перечисление" + info.getName();
  }

  @Override
  public IValue getPropertyValue(IValue index) {
    // todo: проверить ключ на валидность
    var key = index.asString();
    for (var value : values) {
      if (value.getContextInfo().getName().equalsIgnoreCase(key) || value.getContextInfo().getAlias().equalsIgnoreCase(key)) {
        return value;
      }
    }
    throw MachineException.getPropertyNotFoundException(key);
  }

  @Override
  public void setPropertyValue(IValue index, IValue value) {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public boolean hasProperty(IValue index) {
    // todo: проверить ключ на валидность
    var key = index.asString();
    for (var value : values) {
      if (value.getContextInfo().getName().equalsIgnoreCase(key) || value.getContextInfo().getAlias().equalsIgnoreCase(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ContextInfo getContextInfo() {
    return info;
  }

}
