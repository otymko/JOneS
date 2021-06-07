package com.github.otymko.jos.vm;

import com.github.otymko.jos.context.RuntimeContextInstance;
import com.github.otymko.jos.context.value.Variable;
import com.github.otymko.jos.vm.info.MethodInfo;
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
