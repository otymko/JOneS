/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Определение константы.
 */
@Value
@EqualsAndHashCode
public class ConstantDefinition {
    /**
     * Тип значения константы.
     */
    DataType dataType;
    /**
     * Значение.
     */
    IValue value;

    public ConstantDefinition(IValue value) {
        this.value = value;
        this.dataType = this.value.getDataType();
    }

    public String toString() {
        return String.format("%s:%s", value.getDataType(), value.asString());
    }
}
