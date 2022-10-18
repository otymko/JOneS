/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Любое значение, используемое в языке.
 */
public interface IValue {
    /**
     * Выразить значение как число.
     */
    BigDecimal asNumber();

    /**
     * Выразить значение как дату.
     */
    Date asDate();

    /**
     * Выразить значение как булево.
     */
    boolean asBoolean();

    /**
     * Выразить значение как строку.
     */
    String asString();

    /**
     * Получить значение без упаковки (например, в IVariable).
     */
    IValue getRawValue();

    /**
     * Получить тип значения.
     */
    DataType getDataType();

    /**
     * Сравнить значения.
     *
     * @param object Сравниваемое значение.
     *
     * @return 0 - если равны, 1 - если исходное значение больше сравниваемого, -1 - если исходное меньше.
     */
    int compareTo(IValue object);
}
