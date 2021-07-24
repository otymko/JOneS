package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.runtime.IValue;

public class UserScriptContext extends ScriptDrivenObject {

  public UserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public void callMethodScript(int methodId, IValue[] arguments) {

  }

}
