/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;

@UtilityClass
public class TypeFactory {

  // это нужно делать все
  public IValue callConstructor(ContextInfo contextInfo, IValue[] arguments) {
    // TODO: ищем конструктор по аргументам
    var constructor = contextInfo.getConstructors()[0];
    var methodCall = constructor.getMethod();
    Object result;
    try {
      result = methodCall.invoke(null, arguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Не удалось создать экземпляр типа");
    }
    return (IValue) result;
  }

}
