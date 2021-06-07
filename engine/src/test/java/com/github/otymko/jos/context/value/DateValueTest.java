package com.github.otymko.jos.context.value;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

class DateValueTest {

  @Test
  void test() {
    var date = new GregorianCalendar(2014, Calendar.JANUARY, 1).getTime();
    var value = ValueFactory.create(date);

    assertThat(value.asDate()).isEqualTo(date);
    assertThat(value).isEqualTo(ValueFactory.create(date));
    assertThat(value.asString()).isEqualTo("01.01.2014 00:00:00");

    // TODO: еще тесты
  }

}
