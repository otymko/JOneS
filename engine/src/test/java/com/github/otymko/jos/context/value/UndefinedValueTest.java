/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.context.value;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UndefinedValueTest {

    @Test
    void test() {
        var value = ValueFactory.create();

        assertThat(value).isEqualTo(ValueFactory.create());
        assertThat(value.asString()).isEmpty();

        Set<IValue> set = new HashSet<>();
        set.add(ValueFactory.create());
        set.add(ValueFactory.create());

        assertThat(set).hasSize(1);

        set.add(BooleanValue.TRUE);

        assertThat(set).hasSize(2);

        // TODO: еще тесты
    }

}
