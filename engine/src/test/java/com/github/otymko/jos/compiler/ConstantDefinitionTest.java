/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConstantDefinitionTest {

    @Test
    void testEquals() {
        List<ConstantDefinition> list = new ArrayList<>();

        var value1 = new ConstantDefinition(ValueFactory.create(100));
        var value2 = new ConstantDefinition(ValueFactory.create(100));

        list.add(value1);

        assertThat(value1).isEqualTo(value2);
        assertThat(list.contains(value2)).isTrue();
    }

    @Test
    void testDiffers_false_zero() {
        List<ConstantDefinition> list = new ArrayList<>();

        var value1 = new ConstantDefinition(ValueFactory.create(false));
        var value2 = new ConstantDefinition(ValueFactory.create(0));

        list.add(value1);

        assertThat(value1).isNotEqualTo(value2);
        assertThat(list.contains(value2)).isFalse();
    }

    @Test
    void testDiffers_zero_false() {
        List<ConstantDefinition> list = new ArrayList<>();

        var value1 = new ConstantDefinition(ValueFactory.create(0));
        var value2 = new ConstantDefinition(ValueFactory.create(false));

        list.add(value1);

        assertThat(value1).isNotEqualTo(value2);
        assertThat(list.contains(value2)).isFalse();
    }

}
