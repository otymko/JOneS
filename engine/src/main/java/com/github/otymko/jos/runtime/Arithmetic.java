package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class Arithmetic {

  public IValue add(IValue one, IValue two) {
    if (one.getDataType() == DataType.STRING) {
      return ValueFactory.create(one.asString() + two.asString());
    }
    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
      var date = one.asDate();
      var newValue = new Date(date.getTime() + (long) two.asNumber());
      return ValueFactory.create(newValue);
    }
    return ValueFactory.create(one.asNumber() + two.asNumber());
  }

  public IValue sub(IValue one, IValue two) {
    if (one.getDataType() == DataType.NUMBER) {
      return ValueFactory.create(one.asNumber() - two.asNumber());
    }

    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
      // TODO
      throw new RuntimeException("SUB not realize");
    }

    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.DATE) {
      // TODO
      throw new RuntimeException("SUB not realize");
    }

    return ValueFactory.create(one.asNumber() - two.asNumber());
  }

  public IValue mul(IValue one, IValue two) {
    return ValueFactory.create(one.asNumber() * two.asNumber());
  }

  public IValue div(IValue one, IValue two) {
    if (two.asNumber() == 0) {
      // TODO
      throw new RuntimeException("деление на 0");
    }
    // TODO тесты
    return ValueFactory.create(one.asNumber() / two.asNumber());
  }

}
