package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.ContextConstructor;

import java.util.ArrayList;
import java.util.List;

@ContextClass(name = "Массив", alias = "Array")
public class ArrayImpl extends ContextValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(ArrayImpl.class);

  private final List<IValue> values;

  public ArrayImpl() {
    values = new ArrayList<>();
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  // TODO: конструктор

  @ContextMethod(name = "Количество", alias = "Count")
  public int count() {
    return values.size();
  }

  @ContextMethod(name = "Очистить", alias = "Clear")
  public void clear() {
    values.clear();
  }

  @ContextMethod(name = "Добавить", alias = "Add")
  public void add(IValue value) {
    // null
  }

  @ContextMethod(name = "Вставить", alias = "Insert")
  public void insert(int index, IValue value) {
    // null
  }

  @ContextMethod(name = "Найти", alias = "Find")
  public IValue find(IValue value) {
    return null;
  }

  @ContextMethod(name = "Удалить", alias = "Delete")
  public void delete(int index) {

  }

  @ContextMethod(name = "ВГраница", alias = "UBound")
  public int upperBound() {
    return 0;
  }

  @ContextMethod(name = "Получить", alias = "Get")
  public IValue get(int index) {
    return null;
  }

  @ContextMethod(name = "Установить", alias = "Set")
  public void set(int index, IValue value) {

  }

  @ContextConstructor
  public static ArrayImpl constructor() {
    return new ArrayImpl();
  }

}
