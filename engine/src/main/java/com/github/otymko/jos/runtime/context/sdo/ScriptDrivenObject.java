/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.sdo;

import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import lombok.Getter;

/**
 * Абстрактная реализация объекта скрипта
 */
public abstract class ScriptDrivenObject extends ContextValue {
  @Getter
  private final ModuleImage moduleImage;
  @Getter
  private IVariable[] state;

  protected ScriptDrivenObject(ModuleImage moduleImage) {
    this.moduleImage = moduleImage;
    init();
  }

  public void initialize(ScriptEngine engine) {
    engine.getMachine().executeModuleBody(this);
  }

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

  private void init() {
    state = new IVariable[moduleImage.getVariables().size()];
    for (var index = 0; index < state.length; index++) {
      state[index] = Variable.create(ValueFactory.create(), moduleImage.getVariables().get(index).getName());
    }
  }

}
