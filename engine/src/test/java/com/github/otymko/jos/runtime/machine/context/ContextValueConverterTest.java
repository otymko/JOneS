/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.context;

import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.enumeration.MessageStatus;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static com.github.otymko.jos.runtime.machine.context.ContextValueConverter.convertReturnValue;
import static com.github.otymko.jos.runtime.machine.context.ContextValueConverter.convertValue;
import static org.assertj.core.api.Assertions.assertThat;

class ContextValueConverterTest {
    @Test
    void testConvertValue() {
        assertThat(convertValue(null, String.class)).isNull();
        assertThat(convertValue(UndefinedValue.VALUE, String.class)).isNull();

        assertThat(convertValue(ValueFactory.create("Value"), IValue.class)).isEqualTo(ValueFactory.create("Value"));

        var variable = Variable.create(ValueFactory.create(true), "variable");
        assertThat(convertValue(variable, IVariable.class)).isEqualTo(variable);

        assertThat(convertValue(ValueFactory.create("String"), String.class)).isEqualTo("String");

        assertThat(convertValue(ValueFactory.create(11), Integer.class)).isEqualTo(11);
        assertThat(convertValue(ValueFactory.create(11), int.class)).isEqualTo(11);

        assertThat(convertValue(ValueFactory.create(11.0f), double.class)).isEqualTo(11.0d);
        assertThat(convertValue(ValueFactory.create(11.0f), Double.class)).isEqualTo(11.0d);

        assertThat(convertValue(ValueFactory.create(0), byte.class)).isEqualTo((byte) 0);
        assertThat(convertValue(ValueFactory.create(0), Byte.class)).isEqualTo((byte) 0);

        assertThat(convertValue(ValueFactory.create(11.0f), float.class)).isEqualTo(11.0f);
        assertThat(convertValue(ValueFactory.create(11.0f), Float.class)).isEqualTo(11.0f);

        assertThat(convertValue(ValueFactory.create(11), long.class)).isEqualTo(11);
        assertThat(convertValue(ValueFactory.create(11), Long.class)).isEqualTo(11);

        assertThat(convertValue(ValueFactory.create(11), short.class)).isEqualTo((short) 11);
        assertThat(convertValue(ValueFactory.create(11), Short.class)).isEqualTo((short) 11);

        var date = new Date();
        assertThat(convertValue(ValueFactory.create(date), Date.class)).isEqualTo(date);

        assertThat(convertValue(ValueFactory.create(true), boolean.class)).isEqualTo(true);
        assertThat(convertValue(ValueFactory.create(true), Boolean.class)).isEqualTo(true);

        var array = V8Array.create();
        assertThat(convertValue(array, V8Array.class)).isEqualTo(array);

        var value = MessageStatus.INFORMATION;
        assertThat(convertValue(value.getEnumerationValue(), MessageStatus.class)).isEqualTo(value);
    }

    @Test
    void testConvertReturnValue() {
        assertThat(convertReturnValue(null, String.class)).isEqualTo(ValueFactory.create());
        assertThat(convertReturnValue(ValueFactory.create("String"), IValue.class))
                .isEqualTo(ValueFactory.create("String"));
        assertThat(convertReturnValue("String", String.class)).isEqualTo(ValueFactory.create("String"));
        assertThat(convertReturnValue(BigDecimal.valueOf(101), BigDecimal.class))
                .isEqualTo(ValueFactory.create(BigDecimal.valueOf(101)));


        assertThat(convertReturnValue(999, int.class)).isEqualTo(ValueFactory.create(999));
        assertThat(convertReturnValue(999, Integer.class)).isEqualTo(ValueFactory.create(999));

        assertThat(convertReturnValue((double) 999, double.class)).isEqualTo(ValueFactory.create(BigDecimal.valueOf((double) 999)));
        assertThat(convertReturnValue(999.0, Double.class))
                .isEqualTo(ValueFactory.create(BigDecimal.valueOf(999.0)));

        assertThat(convertReturnValue(1, byte.class)).isEqualTo(ValueFactory.create(1));
        assertThat(convertReturnValue(1, Byte.class)).isEqualTo(ValueFactory.create(1));

        assertThat(convertReturnValue(999f, float.class)).isEqualTo(ValueFactory.create(999f));
        assertThat(convertReturnValue(999f, Float.class)).isEqualTo(ValueFactory.create(999f));

        assertThat(convertReturnValue(999L, long.class)).isEqualTo(ValueFactory.create(999L));
        assertThat(convertReturnValue(999L, Long.class)).isEqualTo(ValueFactory.create(999L));

        assertThat(convertReturnValue(999, short.class)).isEqualTo(ValueFactory.create(999));
        assertThat(convertReturnValue(999, Short.class)).isEqualTo(ValueFactory.create(999));

        var date = new Date();
        assertThat(convertReturnValue(date, Date.class)).isEqualTo(ValueFactory.create(date));

        assertThat(convertReturnValue(true, boolean.class)).isEqualTo(ValueFactory.create(true));
        assertThat(convertReturnValue(true, Boolean.class)).isEqualTo(ValueFactory.create(true));

        assertThat(convertReturnValue(MessageStatus.INFORMATION, MessageStatus.class))
                .isEqualTo(MessageStatus.INFORMATION.getEnumerationValue());

        var array = V8Array.create();
        assertThat(convertReturnValue(array, V8Array.class)).isEqualTo(array);
    }
}
