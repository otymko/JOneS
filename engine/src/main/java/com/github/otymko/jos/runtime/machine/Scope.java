package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.RuntimeContextInstance;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import lombok.Value;

/**
 * Слой контекста
 */
@Value
public class Scope {
  RuntimeContextInstance instance;
  Variable[] variables;
  MethodInfo[] methods;
}
