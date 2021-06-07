package com.github.otymko.jos.context;

import com.github.otymko.jos.context.value.Value;
import com.github.otymko.jos.vm.info.MethodInfo;

import java.lang.reflect.InvocationTargetException;

public abstract class AttachableContext implements RuntimeContextInstance {
  private final MethodInfo[] methods;

  protected AttachableContext() {
    methods = ContextInitializer.getContextMethods(getClass());
  }

  public void callMethodScript(int methodId, Value[] arguments) {
    var methodInfo = methods[methodId];
    var callMethod = methodInfo.getMethod();

    try {
      callMethod.invoke(this, arguments);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

  }

}
