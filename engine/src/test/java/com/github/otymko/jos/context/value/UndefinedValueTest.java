/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UndefinedValueTest {

  @Test
  void test() {
    var value = ValueFactory.create();

    assertThat(value).isEqualTo(ValueFactory.create());
    assertThat(value.asString()).isEmpty();

    // TODO: еще тесты
  }

}
