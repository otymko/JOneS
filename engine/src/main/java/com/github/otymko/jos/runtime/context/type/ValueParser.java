package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValueParser {

  public IValue parse(String view, DataType dataType) {
    IValue result;
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
