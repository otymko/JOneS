package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.runtime.type.BaseValue;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.label.ContextMethod;

public class SystemGlobalContext extends AttachableContext {

  public SystemGlobalContext() {
    // none
  }

  @ContextMethod(name = "Сообщить", alias = "Message")
  public static void message(BaseValue message) {
    System.out.println(message.asString());
  }

}
