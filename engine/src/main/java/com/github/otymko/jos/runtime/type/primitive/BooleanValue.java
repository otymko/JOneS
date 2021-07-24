package com.github.otymko.jos.runtime.type.primitive;

import com.github.otymko.jos.runtime.DataType;
import com.github.otymko.jos.runtime.GenericIValue;
import com.github.otymko.jos.runtime.IValue;
import com.github.otymko.jos.runtime.ValueFactory;

import java.util.function.Predicate;

public class BooleanValue extends GenericIValue {
  public static final BooleanValue TRUE = new BooleanValue(true);
  public static final BooleanValue FALSE = new BooleanValue(false);

  private static final Predicate<String> IS_TRUE = view -> view.equalsIgnoreCase("истина")
    || view.equalsIgnoreCase("true") || view.equalsIgnoreCase("да");
  private static final Predicate<String> IS_FALSE = view -> view.equalsIgnoreCase("ложь")
    || view.equalsIgnoreCase("false") || view.equalsIgnoreCase("нет");

  private final boolean value;

  private BooleanValue(boolean value) {
    this.value = value;
    setDataType(DataType.BOOLEAN);
  }

  public static IValue parse(String view) {
    IValue result;
    if (IS_TRUE.test(view)) {
      result = ValueFactory.create(true);
    } else if (IS_FALSE.test(view)) {
      result = ValueFactory.create(false);
    } else {
      throw new RuntimeException("Преобразование к типу 'Булево' не поддерживается");
    }
    return result;
  }

  @Override
  public boolean asBoolean() {
    return value;
  }

  @Override
  public float asNumber() {
    if (value) {
      return 1;
    }
    return 0;
  }

  @Override
  public String asString() {
    return value ? "Да" : "Нет";
  }

  // FIXME
  @Override
  public int compareTo(IValue other) {
    if (other.getDataType() == DataType.BOOLEAN || other.getDataType() == DataType.NUMBER) {
      return Float.compare(asNumber(), other.asNumber());
    }
    return super.compareTo(other);
  }

}
