/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;

import java.lang.reflect.InvocationTargetException;

public interface RuntimeContext {

  ContextInfo getContextInfo();

  default void callAsProcedure(int methodId, IValue[] arguments) {
    var methodInfo = getContextInfo().getMethods()[methodId];
    var callMethod = methodInfo.getMethod();
    try {
      callMethod.invoke(this, arguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  default IValue callAsFunction(int methodId, IValue[] arguments) {
    var methodInfo = getContextInfo().getMethods()[methodId];
    var callMethod = methodInfo.getMethod();
    Object result;
    try {
      result = callMethod.invoke(this, arguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MachineException("Ошибка при вызове функции");
    }
    return (IValue) result;
  }

  default int findMethodId(String name) {
    var contextInfo = getContextInfo();
    for (var index = 0; index < contextInfo.getMethods().length; index++) {
      var method = contextInfo.getMethods()[index];
      if (method.getName().equalsIgnoreCase(name)) {
        return index;
      }
    }
    return -1;
  }

  default MethodInfo getMethodById(int methodId) {
    return getContextInfo().getMethods()[methodId];
  }

  default int findProperty(String propertyName) {
    int position = 0;
    for (var property : getContextInfo().getProperties()) {
      if (property.getName().equalsIgnoreCase(propertyName) || property.getAlias().equalsIgnoreCase(propertyName)) {
        return position;
      }
      position++;
    }
    return -1;
  }

  default IValue getPropertyValue(int index) {
    var property = getContextInfo().getProperties()[index];
    var field = property.getField();
    Object result;
    try {
      result = field.get(this);
    } catch (IllegalAccessException exception) {
      throw new MachineException("Ошибка при получении свойства");
    }
    return (IValue) result;
  }

  default void setPropertyValue(int index, IValue value) {
    var property = getContextInfo().getProperties()[index];
    var field = property.getField();
    try {
      field.set(this, value);
    } catch (IllegalAccessException e) {
      throw new MachineException("Ошибка при установке значения свойства");
    }
  }

  default boolean isPropertyReadOnly(int index) {
    var property = getContextInfo().getProperties()[index];
    return property.getAccessMode() == PropertyAccessMode.READ_ONLY;
  }

  default boolean isPropertyWriteOnly(int index) {
    var property = getContextInfo().getProperties()[index];
    return property.getAccessMode() == PropertyAccessMode.WRITE_ONLY;
  }

}
