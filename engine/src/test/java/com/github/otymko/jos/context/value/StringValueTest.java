package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

class StringValueTest {

  @Test
  void test() {
    var trueValue = ValueFactory.create("ИстИНа");
    assertThat(trueValue.getDataType()).isEqualTo(DataType.STRING);
    assertThat(trueValue.asBoolean()).isTrue();
    assertThat(trueValue.asString()).isEqualToIgnoringCase("ИстИНа");

    var falseValue = ValueFactory.create("лОжЬ");
    assertThat(falseValue.asBoolean()).isFalse();
    assertThat(falseValue.asString()).isEqualToIgnoringCase("лОжЬ");

    var dateString = ValueFactory.create("20140101");
    var dateExample = new GregorianCalendar(2014, Calendar.JANUARY, 1).getTime();
    assertThat(dateString.asDate()).isEqualTo(dateExample);

    var numberString = ValueFactory.create("012.12");
    assertThat(numberString.asNumber()).isEqualTo(12.12f);

    // TODO: date -> object && true -> number
  }

}