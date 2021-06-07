package com.github.otymko.jos.context.value;

import com.github.otymko.jos.context.DataType;

import java.util.Date;

public final class StringValue extends GenericValue {
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
  public static Value parse(String view) {
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
  public int compareTo(Value other) {
    if (other.getDataType() == getDataType()) {
      return value.compareTo(other.asString());
    }
    throw new RuntimeException("Not supported");
  }

  // FIXME
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Value)) {
      throw new RuntimeException("Not supported");
    }
    var other = (Value) object;
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
