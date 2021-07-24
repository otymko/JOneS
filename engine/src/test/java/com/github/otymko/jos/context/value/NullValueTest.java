package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.ValueFactory;
import com.github.otymko.jos.runtime.type.primitive.NullValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NullValueTest {

  @Test
  void test() {
    var value = ValueFactory.createNullValue();

    assertThat(value).isEqualTo(NullValue.VALUE);
    assertThat(value.asString()).isEmpty();

    // TODO: еще тесты
  }

  @Test
  void testParse() {
    var stringValue = "nUll";
    var value = NullValue.parse(stringValue);

    assertThat(value).isEqualTo(NullValue.VALUE);
  }

}
