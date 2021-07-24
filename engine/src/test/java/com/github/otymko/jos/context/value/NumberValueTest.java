package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.type.primitive.NumberValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberValueTest {

  @Test
  void testEquals() {
    var numberOne = NumberValue.create((float) 12.34);
    var numberTwo = NumberValue.create((float) 12.34);
    var number5 = NumberValue.create(5);
    var number0 = NumberValue.create(0);

    assertThat(numberOne).isEqualTo(numberTwo);
    assertThat(numberOne.equals(numberTwo)).isTrue();
    assertThat(numberOne).isNotEqualTo(number5);
    assertThat(numberOne).isNotEqualTo(null);
    assertThat(number5.asNumber()).isEqualTo(5);
    assertThat(number0.asNumber()).isEqualTo(0);
    assertThat(number5).isNotEqualTo(number0);

    // TODO: еще тесты
  }

}