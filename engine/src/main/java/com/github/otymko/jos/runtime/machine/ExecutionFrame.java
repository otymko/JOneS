package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.Variable;
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

  private boolean discardReturnValue;
}
