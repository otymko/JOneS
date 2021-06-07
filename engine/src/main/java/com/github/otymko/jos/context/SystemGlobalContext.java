package com.github.otymko.jos.context;

import com.github.otymko.jos.context.value.Value;
import com.github.otymko.jos.label.ContextMethod;

public class SystemGlobalContext extends AttachableContext {

  public SystemGlobalContext() {
    // none
  }

  @ContextMethod(name = "Сообщить", alias = "Message")
  public static void message(Value message) {
    System.out.println(message.asString());
  }

}
