/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "Неопределено", alias = "Undefined")
public class UndefinedValue extends PrimitiveValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(UndefinedValue.class);

    public static final UndefinedValue VALUE = new UndefinedValue();

    private UndefinedValue() {
        setDataType(DataType.UNDEFINED);
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public int compareTo(IValue object) {
        if (object.getDataType() == getDataType()) {
            return 0;
        }
        return 1;
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
