/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.core.IValue;

/**
 * Доступ по индексу.
 */
public interface IndexAccessor {
    // FIXME: (#50) перейти на конвертацию значений

    /**
     * Получить значене по индексу.
     *
     * @param index Индекс.
     */
    IValue getIndexedValue(IValue index);

    /**
     * Установить значение по индексу.
     *
     * @param index Индекс.
     * @param value Значение.
     */
    void setIndexedValue(IValue index, IValue value);
}
