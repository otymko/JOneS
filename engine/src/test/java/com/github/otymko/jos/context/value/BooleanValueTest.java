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

class BooleanValueTest {

    @Test
    void test() {
        assertThat(BooleanValue.TRUE.asBoolean()).isTrue();
        assertThat(BooleanValue.FALSE.asBoolean()).isFalse();

        assertThat(ValueFactory.create(true)).isSameAs(BooleanValue.TRUE);
        assertThat(ValueFactory.create(false)).isSameAs(BooleanValue.FALSE);

        assertThat(BooleanValue.FALSE.asNumber().floatValue()).isEqualTo(0f);
        assertThat(BooleanValue.TRUE.asNumber().floatValue()).isEqualTo(1f);

        assertThat(BooleanValue.TRUE.compareTo(BooleanValue.FALSE) > 0).isTrue();

        Set<IValue> set = new HashSet<>();
        set.add(BooleanValue.TRUE);
        set.add(BooleanValue.TRUE);

        assertThat(set).hasSize(1)
                .allMatch(iValue -> iValue.equals(BooleanValue.TRUE));

        set.add(BooleanValue.FALSE);
        assertThat(set).hasSize(2);

        // TODO: boolean -> date, boolean -> object
    }

}
