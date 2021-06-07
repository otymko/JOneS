package com.github.otymko.jos.vm;

import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.context.value.Variable;
import lombok.Data;

@Data
public class ExecutionFrame {
  private int instructionPointer;
  private int lineNumber;
  private Variable[] localVariables;

  private Scope moduleScope;
  private int moduleLoadIndex;
  private ModuleImage image;
  private String methodName;
}
