package com.github.otymko.jos.runtime.machine;

import lombok.Getter;

import java.nio.file.Path;

public class StackTraceRecord {

  @Getter
  private String methodName;
  @Getter
  private int lineNumber;
  @Getter
  private Path source;

  StackTraceRecord(ExecutionFrame frame) {
    methodName = frame.getMethodName();
    source = frame.getImage().getSource().getPath();
    lineNumber = frame.getLineNumber();
  }
}
