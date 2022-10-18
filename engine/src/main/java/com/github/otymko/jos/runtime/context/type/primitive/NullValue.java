/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "Null", alias = "Null")
public class NullValue extends PrimitiveValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(NullValue.class);

    public static final NullValue VALUE = new NullValue();

    private NullValue() {
        setDataType(DataType.GENERIC_VALUE);
    }

    public static IValue parse(String view) {
        IValue result;
        if (view.equalsIgnoreCase("null")) {
            result = ValueFactory.createNullValue();
        } else {
            throw MachineException.operationNotSupportedException();
        }
        return result;
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
