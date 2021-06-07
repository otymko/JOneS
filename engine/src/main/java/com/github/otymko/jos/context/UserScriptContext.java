package com.github.otymko.jos.context;

import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.context.value.Value;

public class UserScriptContext extends ScriptDrivenObject {

  public UserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public void callMethodScript(int methodId, Value[] arguments) {

  }

}
