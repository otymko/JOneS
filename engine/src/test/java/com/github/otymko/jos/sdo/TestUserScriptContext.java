package com.github.otymko.jos.sdo;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

public class TestUserScriptContext extends ScriptDrivenObject {

  protected TestUserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public ContextInfo getContextInfo() {
    return null;
  }

}
