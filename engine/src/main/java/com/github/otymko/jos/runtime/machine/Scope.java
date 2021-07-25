package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import lombok.Value;

/**
 * Слой контекста
 */
@Value
public class Scope {
  RuntimeContext instance;
  Variable[] variables;
  MethodInfo[] methods;
}
