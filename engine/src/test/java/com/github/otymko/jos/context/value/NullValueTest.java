/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.context.value;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NullValueTest {

    @Test
    void test() {
        var value = ValueFactory.createNullValue();

        assertThat(value).isEqualTo(NullValue.VALUE);
        assertThat(value.asString()).isEmpty();

        Set<IValue> set = new HashSet<>();
        set.add(NullValue.VALUE);
        set.add(NullValue.VALUE);

        assertThat(set).hasSize(1);

        // TODO: еще тесты
    }

    @Test
    void testParse() {
        var stringValue = "nUll";
        var value = NullValue.parse(stringValue);

        assertThat(value).isEqualTo(NullValue.VALUE);
    }

}
