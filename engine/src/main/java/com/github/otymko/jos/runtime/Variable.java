/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.core.IVariable;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Переменная.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends ContextValue implements IVariable {
    /**
     * Имя переменной.
     */
    private String name;
    /**
     * Значение переменной.
     */
    private IValue value;
    /**
     * Тип значения.
     */
    private DataType dataType;

    /**
     * Создать переменную.
     *
     * @param value Значение переменной.
     * @param name Имя переменной.
     */
    public static Variable create(IValue value, String name) {
        var variable = new Variable();
        variable.setName(name);
        variable.setValue(value);
        return variable;
    }

    /**
     * Установить значение.
     */
    public void setValue(IValue value) {
        this.value = value.getRawValue();
        setDataType(value.getDataType());
    }

    @Override
    public ContextInfo getContextInfo() {
        if (getValue() instanceof ContextValue) {
            return ((ContextValue) getValue()).getContextInfo();
        }
        throw MachineException.operationNotImplementedException();
    }

    @Override
    public String asString() {
        return value.asString();
    }

    @Override
    public BigDecimal asNumber() {
        return value.asNumber();
    }

    @Override
    public Date asDate() {
        return value.asDate();
    }

    @Override
    public boolean asBoolean() {
        return value.asBoolean();
    }

    @Override
    public IValue getRawValue() {
        return value;
    }

    @Override
    public int compareTo(IValue o) {
        return getValue().compareTo(o);
    }
}
