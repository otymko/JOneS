package com.github.otymko.jos.exception;

public class WrappedJavaException extends MachineException {
  public WrappedJavaException(Exception exception) {
    super("Внешнее системное исключение", exception.getCause());
  }
}
