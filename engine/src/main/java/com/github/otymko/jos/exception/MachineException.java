package com.github.otymko.jos.exception;

import lombok.Getter;

/**
 * Исключение при выполнении в стековой машине
 */
public class MachineException extends EngineException {
  @Getter
  private final ErrorInfo errorInfo;

  public MachineException(String message) {
    super(message);

    errorInfo = new ErrorInfo();
    errorInfo.setLine(-1);
  }

  @Override
  public String getMessage() {
    var message = String.format(
      "{Модуль: %s / Ошибка в строке: %d / %s}",
      errorInfo.getSource(),
      errorInfo.getLine(),
      super.getMessage()
    );
    if (!errorInfo.getCode().isEmpty()) {
      message += "\n" + errorInfo.getCode();
    }
    return message;
  }

  public static MachineException typeNotSupportedException(String typeName) {
    var message = String.format("Тип не поддерживается (%s)", typeName);
    return new MachineException(message);
  }

  public static MachineException typeNotRegisteredException(String typeName) {
    var message = String.format("Тип не зарегистрирован (%s)", typeName);
    return new MachineException(message);
  }

  public static MachineException objectNotSupportAccessByIndexException() {
    var message = "Объект не поддерживает доступ по индексу";
    return new MachineException(message);
  }

  public static MachineException constructorNotFoundException(String typeName) {
    var message = String.format("Конструктор не найден (%s)", typeName);
    return new MachineException(message);
  }

  public static MachineException objectIsNotContextTypeException() {
    var message = "Объект не является ContextType";
    return new MachineException(message);
  }

  public static MachineException operationNotSupportedException() {
    var message = "Операция не поддерживается";
    return new MachineException(message);
  }

  public static MachineException operationNotImplementedException() {
    var message = "Операция не реализована";
    return new MachineException(message);
  }

  public static MachineException divideByZeroException() {
    var message = "Деление на ноль";
    return new MachineException(message);
  }

  public static MachineException indexValueOutOfRangeException() {
    var message = "Значение индекса выходит за пределы диапазона";
    return new MachineException(message);
  }

  public static MachineException convertToNumberException() {
    var message = "Преобразование к типу 'Число' не поддерживается";
    return new MachineException(message);
  }

  public static MachineException convertToBooleanException() {
    var message = "Преобразование к типу 'Булево' не поддерживается";
    return new MachineException(message);
  }

  public static MachineException convertToDateException() {
    var message = "Преобразование к типу 'Дата' не поддерживается";
    return new MachineException(message);
  }

  public static MachineException invalidPropertyNameStructureException(String propertyName) {
    var message = String.format("Задано неправильное имя атрибута структуры `%s`", propertyName);
    return new MachineException(message);
  }

}
