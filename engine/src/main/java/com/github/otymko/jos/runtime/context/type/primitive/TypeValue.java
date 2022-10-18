/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.Locale;
import java.util.Objects;

@ContextClass(name = "Тип", alias = "Type")
public class TypeValue extends PrimitiveValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(TypeValue.class);
    @Getter
    private final ContextInfo value;

    public TypeValue(ContextInfo value) {
        this.value = value;
        setDataType(DataType.TYPE);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public String asString() {
        // FIXME: для перечисления выводится без `Перечисление`
        return value.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof IValue)) {
            return false;
        }
        var baseValue = (TypeValue) obj;
        return baseValue.getDataType() == DataType.TYPE && value.getName().equalsIgnoreCase(baseValue.getValue().getName());
    }

    @Override
    public int hashCode() {
        var key = value.getName().toLowerCase(Locale.ENGLISH);
        return Objects.hashCode(key);
    }

    @Override
    public int compareTo(IValue object) {
        if (object.getDataType() != DataType.TYPE) {
            return 1;
        }
        var inputValue = (TypeValue) object;
        return getValue().equals(inputValue.getValue()) ? 0 : 1;
    }
}
