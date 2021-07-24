package com.github.otymko.jos.runtime;

import com.github.otymko.jos.runtime.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.type.primitive.DateValue;
import com.github.otymko.jos.runtime.type.primitive.NullValue;
import com.github.otymko.jos.runtime.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.type.primitive.StringValue;
import com.github.otymko.jos.runtime.type.primitive.UndefinedValue;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StandardTypeInitializer {

  public void initialize(TypeManager typeManager) {

    for (var type : DataType.values()) {

      if (type == DataType.UNDEFINED) {
        typeManager.registerType("Неопределено", UndefinedValue.class);
      } else if (type == DataType.GENERIC_VALUE) {
        typeManager.registerType("Null", NullValue.class);
      } else if (type == DataType.BOOLEAN) {
        typeManager.registerType("Булево", BooleanValue.class);
      } else if (type == DataType.STRING) {
        typeManager.registerType("Строка", StringValue.class);
      } else if (type == DataType.NUMBER) {
        typeManager.registerType("Число", NumberValue.class);
      } else if (type == DataType.DATE) {
        typeManager.registerType("Дата", DateValue.class);
      }
      // type
      // object
      // + алиас?

    }

  }

}
