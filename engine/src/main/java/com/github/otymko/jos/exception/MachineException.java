package com.github.otymko.jos.exception;

import com.github.otymko.jos.runtime.machine.StackTraceRecord;
import lombok.Getter;

import java.util.Collections;
import java.util.List;


/**
 * Исключение при выполнении в стековой машине
 */
public class MachineException extends EngineException {
  @Getter
  private final ErrorInfo errorInfo;

  private List<StackTraceRecord> stackTrace;

  public MachineException(String message) {
    super(message);

    errorInfo = new ErrorInfo();
    errorInfo.setLine(-1);
  }

  public MachineException(String message, Throwable cause) {
    super(message, cause);
    errorInfo = new ErrorInfo();
    errorInfo.setLine(-1);
  }

  public void setBslStackTrace(List<StackTraceRecord> stackTrace) {
    this.stackTrace = Collections.unmodifiableList(stackTrace);
  }

  public List<StackTraceRecord> getBslStackTrace() {
    if (stackTrace == null) {
      return Collections.emptyList();
    }
    return stackTrace;
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

  public String getMessageWithoutCodeFragment() {
    return super.getMessage();
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

  public static MachineException wrongStackConditionException() {
    var message = "Внутренняя ошибка - неверное состояние стека. Ожидалась переменная";
    return new MachineException(message);
  }

  public static MachineException getPropertyNotFoundException(String propertyName) {
    var message = String.format("Свойство объекта не обнаружено (%s)", propertyName);
    return new MachineException(message);
  }

  public static MachineException iteratorIsNotDefined() {
    var message = "Итератор для значения не определен";
    return new MachineException(message);
  }

}
