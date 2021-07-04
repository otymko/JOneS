package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class Arithmetic {

  public Value add(Value one, Value two) {
    if (one.getDataType() == DataType.TYPE) {
      return ValueFactory.create(one.asString() + two.asString());
    }
    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
      var date = one.asDate();
      var newValue = new Date(date.getTime() + (long) two.asNumber());
      return ValueFactory.create(newValue);
    }
    return ValueFactory.create(one.asNumber() + two.asNumber());
  }

}
