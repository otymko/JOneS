package com.github.otymko.jos.runtime.context.sdo;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

public class UserScriptContext extends ScriptDrivenObject {

  public UserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public ContextInfo getContextInfo() {
    throw new RuntimeException("Не поддерживается");
  }

}
