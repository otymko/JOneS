/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Слой контекста
 */
@Value
@RequiredArgsConstructor
public class Scope {
  RuntimeContext instance;
  IVariable[] variables;
  MethodInfo[] methods;

  public Scope(RuntimeContext instance) {
    this.instance = instance;
    variables = new IVariable[0];
    methods = new MethodInfo[0];
  }
}
