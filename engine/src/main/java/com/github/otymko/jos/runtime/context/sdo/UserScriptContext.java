package com.github.otymko.jos.runtime.context.sdo;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "Скрипт", alias = "Script")
public class UserScriptContext extends ScriptDrivenObject {
  public static final ContextInfo INFO = ContextInfo.createByClass(UserScriptContext.class);

  public UserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

}
