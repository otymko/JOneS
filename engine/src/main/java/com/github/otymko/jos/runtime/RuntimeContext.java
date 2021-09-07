/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;

import java.lang.reflect.InvocationTargetException;

import static com.github.otymko.jos.localization.MessageResource.*;

public interface RuntimeContext {

  ContextInfo getContextInfo();

  default void callAsProcedure(int methodId, IValue[] arguments) {
    var methodInfo = getContextInfo().getMethods()[methodId];
    var callMethod = methodInfo.getMethod();
    try {
      callMethod.invoke(this, arguments);
    } catch (MachineException exception) {
      throw exception;
    } catch (IllegalAccessException | InvocationTargetException exception) {
      throw new MachineException(Resources.getResourceString(ERROR_CALL_METHOD), exception);
    }
  }

  default IValue callAsFunction(int methodId, IValue[] arguments) {
    var methodInfo = getContextInfo().getMethods()[methodId];
    var callMethod = methodInfo.getMethod();
    Object result;
    try {
      result = callMethod.invoke(this, arguments);
    } catch (MachineException exception) {
      throw exception;
    } catch (IllegalAccessException | InvocationTargetException exception) {
      throw new MachineException(Resources.getResourceString(ERROR_CALL_METHOD), exception);
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

  // FIXME: переписать на более низкий уровень
  default IValue getPropertyValue(int index) {
    var property = getContextInfo().getProperties()[index];
    Object result;
    if (property.hasGetter()) {
      var getter = property.getGetter();
      try {
        result = getter.invoke(this);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new MachineException(Resources.getResourceString(ERROR_GET_PROPERTY_VALUE));
      }
    } else {
      var field = property.getField();
      try {
        result = field.get(this);
      } catch (IllegalAccessException exception) {
        throw new MachineException(Resources.getResourceString(ERROR_GET_PROPERTY_VALUE));
      }
    }
    return (IValue) result;
  }

  // FIXME: переписать на более низкий уровень
  default void setPropertyValue(int index, IValue value) {
    var property = getContextInfo().getProperties()[index];
    if (property.hasSetter()) {
      var setter = property.getSetter();
      try {
        setter.invoke(this, value);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new MachineException(Resources.getResourceString(ERROR_SET_PROPERTY_VALUE));
      }
    } else {
      var field = property.getField();
      try {
        field.set(this, value);
      } catch (IllegalAccessException e) {
        throw new MachineException(Resources.getResourceString(ERROR_SET_PROPERTY_VALUE));
      }
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
