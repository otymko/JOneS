package com.github.otymko.jos.runtime.context.sdo;

import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.Getter;

/**
 * Абстрактная реализация объекта скрипта
 */
public abstract class ScriptDrivenObject extends ContextValue {
  @Getter
  private final ModuleImage moduleImage;

  protected ScriptDrivenObject(ModuleImage moduleImage) {
    this.moduleImage = moduleImage;
  }

  public void initialize(ScriptEngine engine) {
    engine.getMachine().executeModuleBody(this);
  }

//  @Override
//  public void callAsProcedure(int methodId, IValue[] arguments) {
//    throw new RuntimeException("Не реализовано");
//  }
//
//  @Override
//  public IValue callAsFunction(int methodId, IValue[] arguments) {
//    throw new RuntimeException("Не реализовано");
//  }

  public int getScriptMethod(String name) {
    for (var index = 0; index < moduleImage.getMethods().size(); index++) {
      var methodDescription = moduleImage.getMethods().get(index);
      if (methodDescription.getSignature().getName().equalsIgnoreCase(name)) {
        return index;
      }
    }
    return -1;
  }

  public IValue callScriptMethod(ScriptEngine engine, int methodId, IValue[] parameters) {
    return engine.getMachine().executeMethod(this, methodId, parameters);
  }

  @Override
  public int compareTo(IValue o) {
    return 0;
  }

}
