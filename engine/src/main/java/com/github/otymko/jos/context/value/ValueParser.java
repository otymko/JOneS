package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValueParser {

  public Value parse(String view, DataType dataType) {
    Value result;
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
