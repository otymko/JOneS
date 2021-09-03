/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.machine.info.ConstructorInfo;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;

import static com.github.otymko.jos.localization.MessageResource.ERROR_CALL_CONSTRUCTOR;

@UtilityClass
public class TypeFactory {

  public IValue callConstructor(ContextInfo contextInfo, IValue[] arguments) {
    // TODO: ищем конструктор по количеству аргументов
    ConstructorInfo constructorInfo = null;
    for (var constructor : contextInfo.getConstructors()) {
      if (constructor.getParameters().length == arguments.length) {
        constructorInfo = constructor;
      }
    }
    if (constructorInfo == null) {
      // TODO: exception нужно локализовать
      throw MachineException.constructorNotFoundException(contextInfo.getName());
    }
    var methodCall = constructorInfo.getMethod();
    Object result;
    try {
      result = methodCall.invoke(null, arguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MachineException(Resources.getResourceString(ERROR_CALL_CONSTRUCTOR));
    }
    return (IValue) result;
  }

}
