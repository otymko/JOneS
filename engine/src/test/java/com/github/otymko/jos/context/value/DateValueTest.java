/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class DateValueTest {

  @Test
  void test() {
    var date = new GregorianCalendar(2014, Calendar.JANUARY, 1).getTime();
    var value = ValueFactory.create(date);

    assertThat(value.asDate()).isEqualTo(date);
    assertThat(value).isEqualTo(ValueFactory.create(date));

    if (Locale.getDefault().getLanguage().equals("ru")) {
      assertThat(value.asString()).isEqualTo("01.01.2014 0:00:00");
    } else if (Locale.getDefault().getLanguage().equals("en")) {
      assertThat(value.asString()).isEqualTo("1/1/2014 12:00:00 AM");
    }

    // TODO: еще тесты
  }

}
