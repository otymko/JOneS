/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Базовая арифметика примитивных типов.
 */
@UtilityClass
public class Arithmetic {

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    /**
     * Сложить.
     */
    public IValue add(IValue one, IValue two) {
        if (one.getDataType() == DataType.STRING) {
            return ValueFactory.create(one.asString() + two.asString());
        }
        if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
            var date = one.asDate();
            var delta = two.asNumber().multiply(THOUSAND).longValue();
            var newValue = new Date(date.getTime() + delta);
            return ValueFactory.create(newValue);
        }
        return ValueFactory.create(one.asNumber().add(two.asNumber()));
    }

    /**
     * Вычесть.
     */
    public IValue sub(IValue one, IValue two) {
        if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
            var dateAsTime = one.asDate().getTime();
            var delta = two.asNumber().multiply(THOUSAND).longValue();
            var newValue = new Date(dateAsTime - delta);
            return ValueFactory.create(newValue);
        }

        if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.DATE) {
            // TODO реализовать сложение дат
            throw MachineException.operationNotImplementedException();
        }

        return ValueFactory.create(one.asNumber().subtract(two.asNumber()));
    }

    /**
     * Умножить.
     */
    public IValue mul(IValue one, IValue two) {
        return ValueFactory.create(one.asNumber().multiply(two.asNumber()));
    }

    /**
     * Разделить.
     */
    public IValue div(IValue one, IValue two) {
        if (two.asNumber().equals(BigDecimal.ZERO)) {
            throw MachineException.divideByZeroException();
        }
        // TODO тесты
        return ValueFactory.create(one.asNumber().divide(two.asNumber()));
    }

    /**
     * Остаток.
     */
    public IValue mod(IValue one, IValue two) {
        if (two.asNumber().equals(BigDecimal.ZERO)) {
            throw MachineException.divideByZeroException();
        }
        return ValueFactory.create(one.asNumber().remainder(two.asNumber()));
    }

    /**
     * Инвертировать знак числа.
     */
    public IValue negative(IValue value) {
        return ValueFactory.create(value.asNumber().negate());
    }
}
