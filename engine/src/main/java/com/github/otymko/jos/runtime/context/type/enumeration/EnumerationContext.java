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
  private static final String ENUM_PREFIX = "Перечисление";
  private final ContextInfo info;
  @Getter
  private final Class<? extends EnumType> enumType;
  private final String preview;

  @Getter
  private final List<EnumerationValue> values = new ArrayList<>();

  public EnumerationContext(Class<? extends EnumType> enumTargetClass) {
    var enumClass = enumTargetClass.getAnnotation(EnumClass.class);
    if (enumClass == null) {
      throw MachineException.operationNotSupportedException();
    }
    this.info = ContextInfo.createByEnumClass(enumClass);
    this.enumType = enumTargetClass;
    this.preview = ENUM_PREFIX + info.getName();

    for (var field : enumTargetClass.getFields()) {
      var enumValueClass = field.getAnnotation(EnumValue.class);
      if (enumValueClass == null) {
        continue;
      }

      EnumType enumValue = null;
      try {
        enumValue = (EnumType) field.get(null);
      } catch (IllegalAccessException e) {
        throw MachineException.operationNotSupportedException();
      }

      var value = new EnumerationValue(this, enumValueClass, enumValue);
      values.add(value);
    }
  }

  @Override
  public String asString() {
    return preview;
  }

  @Override
  public IValue getPropertyValue(IValue index) {
    // todo: проверить ключ на валидность
    var key = index.asString();
    for (var value : values) {
      if (value.getName().equalsIgnoreCase(key) || value.getAlias().equalsIgnoreCase(key)) {
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
      if (value.getName().equalsIgnoreCase(key) || value.getAlias().equalsIgnoreCase(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ContextInfo getContextInfo() {
    return info;
  }

  public EnumerationValue getEnumValueType(EnumType enumType) {
    return getValues().stream()
      .filter(enumerationValue -> enumerationValue.getValue() == enumType)
      .findAny()
      .get();
  }

}
