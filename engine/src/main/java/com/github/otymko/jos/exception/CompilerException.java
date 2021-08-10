package com.github.otymko.jos.exception;

/**
 * Исключение при компиляции
 */
public class CompilerException extends EngineException {

  public CompilerException(String message) {
    super(message);
  }

  public static CompilerException symbolNotFoundException(String identifier) {
    var message = String.format("Неизвестный символ: %s", identifier);
    return new CompilerException(message);
  }

  public static CompilerException tooManyMethodArgumentsException() {
    var message = "Слишком много фактических параметров";
    return new CompilerException(message);
  }

  public static CompilerException tooFewMethodArgumentsException() {
    var message = "Недостаточно фактических параметров";
    return new CompilerException(message);
  }

}
