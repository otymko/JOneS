package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import lombok.Getter;

/**
 * Абстрактная реализациия объекта скрипта
 */
public abstract class ScriptDrivenObject implements RuntimeContextInstance {
  @Getter
  private final ModuleImage moduleImage;

  protected ScriptDrivenObject(ModuleImage moduleImage) {
    this.moduleImage = moduleImage;
  }

  public void initialize(ScriptEngine engine) {
    engine.getMachine().executeModuleBody(this);
  }

}
