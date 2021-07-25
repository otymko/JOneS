package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.context.IValue;

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

}
