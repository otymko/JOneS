/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.context.IValue;
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
      throw new RuntimeException("Не удалось выполнить функцию");
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


}
