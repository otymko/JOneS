package com.github.otymko.jos.compiler.image;

import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.compiler.ConstantDefinition;
import com.github.otymko.jos.compiler.MethodDescriptor;
import com.github.otymko.jos.compiler.SymbolAddress;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleImageCache {
  private int entryPoint = -1;
  private List<Command> code = new ArrayList<>();
  private List<ConstantDefinition> constants = new ArrayList<>();
  private List<VariableInfo> variables = new ArrayList<>();
  private List<MethodDescriptor> methods = new ArrayList<>();
  private List<SymbolAddress> methodRefs = new ArrayList<>();
  private List<SymbolAddress> variableRefs = new ArrayList<>();
}
