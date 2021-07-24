package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.type.BaseValue;

public class UserScriptContext extends ScriptDrivenObject {

  public UserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public void callMethodScript(int methodId, BaseValue[] arguments) {

  }

}
