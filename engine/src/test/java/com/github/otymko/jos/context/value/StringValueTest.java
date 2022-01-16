/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

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
    assertThat(numberString.asNumber().floatValue()).isEqualTo(12.12f);

    var oneValue = ValueFactory.create("Истина");
    Set<IValue> set = new HashSet<>();
    set.add(oneValue);
    set.add(ValueFactory.create("Истина"));

    assertThat(set).hasSize(1)
      .allMatch(iValue -> iValue.equals(oneValue));

    set.add(ValueFactory.create("Ложь"));

    assertThat(set).hasSize(2);

    // TODO: date -> object && true -> number
  }

}