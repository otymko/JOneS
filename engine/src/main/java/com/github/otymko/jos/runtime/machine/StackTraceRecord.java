/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import lombok.Value;

import java.nio.file.Path;

@Value
public class StackTraceRecord {
    String methodName;
    int lineNumber;
    Path source;

    StackTraceRecord(ExecutionFrame frame) {
        methodName = frame.getMethodName();
        source = frame.getImage().getSource().getPath();
        lineNumber = frame.getLineNumber();
    }

}
