package com.github.otymko.jos.runtime.type.primitive;

import com.github.otymko.jos.runtime.DataType;
import com.github.otymko.jos.runtime.GenericIValue;
import com.github.otymko.jos.runtime.IValue;
import com.github.otymko.jos.runtime.ValueFactory;
import com.github.otymko.jos.runtime.ValueParser;

import java.util.Date;

public final class StringValue extends GenericIValue {
  public static final StringValue EMPTY = new StringValue("");

  private final String value;

  public static StringValue create(String value) {
    if (value.isEmpty()) {
      return EMPTY;
    }
    return new StringValue(value);
  }

  private StringValue(String value) {
    if (value == null) {
      throw new RuntimeException("Dont use null");
    }
    this.value = value;
    setDataType(DataType.STRING);
  }

  @Override
  public String asString() {
    return value;
  }
  public static IValue parse(String view) {
    return ValueFactory.create(view);
  }

  @Override
  public float asNumber() {
    return ValueParser.parse(value, DataType.NUMBER).asNumber();
  }

  @Override
  public Date asDate() {
    return ValueParser.parse(value, DataType.DATE).asDate();
  }

  @Override
  public boolean asBoolean() {
    return ValueParser.parse(value, DataType.BOOLEAN).asBoolean();
  }


  @Override
  public int compareTo(IValue other) {
    if (other.getDataType() == getDataType()) {
      return value.compareTo(other.asString());
    }
    throw new RuntimeException("Not supported");
  }

  // FIXME
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof IValue)) {
      throw new RuntimeException("Not supported");
    }
    var other = (IValue) object;
    if (other.getDataType() == getDataType()) {
      return other.asString().equals(value);
    }
    return false;
  }

  // FIXME
  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
