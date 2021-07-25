package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "ГлобальныйКонтекст", alias = "GlobalContext")
public class SystemGlobalContext implements AttachableContext {
  public static final ContextInfo INFO = ContextInfo.createByClass(SystemGlobalContext.class);

  public SystemGlobalContext() {
    // none
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  @ContextMethod(name = "Сообщить", alias = "Message")
  public static void message(IValue message) {
    System.out.println(message.asString());
  }

}
