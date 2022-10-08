/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.Data;

import java.util.ArrayDeque;
import java.util.Deque;

@Data
public class ExecutionFrame {
    private int instructionPointer;
    private int lineNumber;
    private IVariable[] localVariables;

    private Deque<IValue> localFrameStack = new ArrayDeque<>();

    private Scope moduleScope;
    private int moduleLoadIndex;
    private ModuleImage image;
    private String methodName;
    private EngineException lastException;

    private boolean discardReturnValue;

    private boolean oneTimeCall;
}
