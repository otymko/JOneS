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
