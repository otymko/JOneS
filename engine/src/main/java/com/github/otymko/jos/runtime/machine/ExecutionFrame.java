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
import java.util.Comparator;
import java.util.Deque;
import java.util.NoSuchElementException;

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

    public int getLineNumber() {
        if (lineNumber == 0) {
            return findLineNumberByInstructionPointer(instructionPointer);
        }

        return lineNumber;
    }

    private int findLineNumberByInstructionPointer(int instruction) {
        if (instruction < 0) {
            return 1;
        }

        try {
            return image.getLinesOffset().stream()
                    .filter(pair -> pair.getLeft() <= instruction)
                    .max(Comparator.naturalOrder())
                    .stream().findAny().get().getRight();
        } catch (NoSuchElementException e) {
            throw e;
        }
    }
}
