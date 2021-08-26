/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.Variable;
import lombok.Data;

@Data
public class ExecutionFrame {
  private int instructionPointer;
  private int lineNumber;
  private IVariable[] localVariables;

  private Scope moduleScope;
  private int moduleLoadIndex;
  private ModuleImage image;
  private String methodName;

  private boolean discardReturnValue;

  private boolean oneTimeCall;
}
