package com.github.otymko.jos.compiler;

import com.github.otymko.jos.vm.info.MethodInfo;
import com.github.otymko.jos.vm.info.VariableInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MethodDescriptor {
  private int entry = -1;
  private MethodInfo signature;
  private List<VariableInfo> variables = new ArrayList<>();
}
