package com.github.otymko.jos.exception;

/**
 * Общее исключение в движке
 */
public class EngineException extends RuntimeException{

  public EngineException(String message) {
    super(message);
  }
}
