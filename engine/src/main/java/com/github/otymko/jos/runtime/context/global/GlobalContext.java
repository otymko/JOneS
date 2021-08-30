/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.compiler.ContextDiscovery;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.VariableReference;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.GlobalContextClass;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@GlobalContextClass
public class GlobalContext implements AttachableContext {
  private final ContextInfo info;
  @Getter
  private final List<IVariable> variables = new ArrayList<>();
  private final List<EnumerationContext> enumerationContexts;

  public GlobalContext() {
    this.info = ContextInfo.EMPTY;

    var contexts = ContextDiscovery.getEnumerationContext();
    this.enumerationContexts = contexts;
    var index = 0;
    for (var enumContext : contexts) {
      var variable = VariableReference.createContextPropertyReference(this, index,
        enumContext.getContextInfo().getName());
      variables.add(variable);
      index++;
    }
  }

  @Override
  public ContextInfo getContextInfo() {
    return info;
  }

  @Override
  public int findProperty(String propertyName) {
    return AttachableContext.super.findProperty(propertyName);
  }

  @Override
  public IValue getPropertyValue(int index) {
    return enumerationContexts.get(index);
  }

  @Override
  public void setPropertyValue(int index, IValue value) {
    throw MachineException.getPropertyIsNotWritableException("");
  }

  @Override
  public boolean isPropertyReadOnly(int index) {
    return true;
  }

  @Override
  public boolean isPropertyWriteOnly(int index) {
    return false;
  }
}
