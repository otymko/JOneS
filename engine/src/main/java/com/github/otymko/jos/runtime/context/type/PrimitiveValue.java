/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.context.IValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public abstract class PrimitiveValue implements IValue, ContextType, Comparable<IValue> {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private DataType dataType;

    public BigDecimal asNumber() {
        throw MachineException.operationNotSupportedException();
    }

    public Date asDate() {
        throw MachineException.operationNotSupportedException();
    }

    public boolean asBoolean() {
        throw MachineException.operationNotSupportedException();
    }

    public String asString() {
        return dataType.toString();
    }

    // TODO: asObject

    public IValue getRawValue() {
        return this;
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public int compareTo(IValue o) {
        throw MachineException.operationNotSupportedException();
    }

    public static IValue parse(String view) {
        throw MachineException.operationNotSupportedException();
    }

}
