package com.github.otymko.jos.compiler;

import com.github.otymko.jos.vm.info.MethodInfo;
import com.github.otymko.jos.vm.info.VariableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolScope {
  @Getter
  private Map<String, Integer> variableNumbers = new HashMap<>();
  @Getter
  private List<VariableInfo> variables = new ArrayList<>();
  @Getter
  private Map<String, Integer> methodNumbers = new HashMap<>();
  @Getter
  private List<MethodInfo> methods = new ArrayList<>();
}
