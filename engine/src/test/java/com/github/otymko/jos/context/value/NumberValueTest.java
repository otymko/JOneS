/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.context.value;

import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberValueTest {

    @Test
    void testEquals() {
        var numberOne = NumberValue.create(BigDecimal.valueOf(12.34));
        var numberTwo = NumberValue.create(BigDecimal.valueOf(12.34));
        var number5 = NumberValue.create(BigDecimal.valueOf(5));
        var number0 = NumberValue.create(BigDecimal.valueOf(0));

        assertThat(numberOne).isEqualTo(numberTwo);
        assertThat(numberOne.equals(numberTwo)).isTrue();
        assertThat(numberOne)
                .isNotEqualTo(number5)
                .isNotEqualTo(null);

        assertThat(number5.asNumber().intValue()).isEqualTo(5);
        assertThat(number0.asNumber().intValue()).isZero();
        assertThat(number5).isNotEqualTo(number0);

        Set<IValue> set = new HashSet<>();
        set.add(numberOne);
        set.add(numberTwo);

        assertThat(set).hasSize(1)
                .allMatch(iValue -> iValue.equals(numberOne));

        set.add(number5);
        assertThat(set).hasSize(2);

        // TODO: еще тесты
    }

}