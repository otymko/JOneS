/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.localization.Resources;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.ValueParser;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import static com.github.otymko.jos.core.localization.MessageResource.NULL_VALUE_NOT_SUPPORTED;

@ContextClass(name = "Строка", alias = "String")
public final class StringValue extends PrimitiveValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(StringValue.class);

    public static final StringValue EMPTY = new StringValue("");

    private final String value;

    public static StringValue create(String value) {
        if (value.isEmpty()) {
            return EMPTY;
        }
        return new StringValue(value);
    }

    private StringValue(String value) {
        if (value == null) {
            // FIXME
            throw new MachineException(Resources.getResourceString(NULL_VALUE_NOT_SUPPORTED));
        }
        this.value = value;
        setDataType(DataType.STRING);
    }

    @Override
    public String asString() {
        return value;
    }

    public static IValue parse(String view) {
        return ValueFactory.create(view);
    }

    @Override
    public BigDecimal asNumber() {
        return ValueParser.parse(value, DataType.NUMBER).asNumber();
    }

    @Override
    public Date asDate() {
        return ValueParser.parse(value, DataType.DATE).asDate();
    }

    @Override
    public boolean asBoolean() {
        return ValueParser.parse(value, DataType.BOOLEAN).asBoolean();
    }


    @Override
    public int compareTo(IValue other) {
        if (other.getDataType() == getDataType()) {
            return value.compareTo(other.asString());
        } else if (other.getDataType() == DataType.UNDEFINED) {
            return 1;
        }
        throw MachineException.operationNotSupportedException();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IValue)) {
            throw MachineException.operationNotSupportedException();
        }
        var other = (IValue) object;
        if (other.getDataType() == getDataType()) {
            return other.asString().equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
