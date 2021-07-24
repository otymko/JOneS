package com.github.otymko.jos.runtime.type;

import com.github.otymko.jos.runtime.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.type.primitive.DateValue;
import com.github.otymko.jos.runtime.type.primitive.NullValue;
import com.github.otymko.jos.runtime.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.type.primitive.StringValue;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValueParser {

  public BaseValue parse(String view, DataType dataType) {
    BaseValue result;
    switch (dataType) {
      case BOOLEAN:
        result = BooleanValue.parse(view);
        break;
      case DATE:
        result = DateValue.parse(view);
        break;
      case NUMBER:
        result = NumberValue.parse(view);
        break;
      case STRING:
        result = StringValue.parse(view);
        break;
      case UNDEFINED:
        result = ValueFactory.create();
        break;
      case GENERIC_VALUE:
        result = NullValue.parse(view);
        break;
      default:
        throw new RuntimeException("constant type is not supported");
    }

    return result;
  }

}
