package com.github.otymko.jos.runtime.machine.context;

import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import org.junit.jupiter.api.Test;

import java.util.Date;

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

        assertThat(convertValue(ValueFactory.create(0), byte.class)).isEqualTo((byte)0);
        assertThat(convertValue(ValueFactory.create(0), Byte.class)).isEqualTo((byte)0);

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

        var array = new V8Array();
        assertThat(convertValue(array, V8Array.class)).isEqualTo(array);
    }

}
