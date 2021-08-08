package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.runtime.Variable;
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
  Variable[] variables;
  MethodInfo[] methods;

  public Scope(RuntimeContext instance) {
    this.instance = instance;
    variables = new Variable[0];
    methods = new MethodInfo[0];
  }
}
