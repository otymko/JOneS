/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.core.IValue;

/**
 * Контекст с доступом к свойству по имени.
 */
public interface PropertyNameAccessor {
    /**
     * Получить значение свойства по имени.
     *
     * @param index Имя свойства.
     */
    // FIXME: (#50) перейти на конвертацию значений
    IValue getPropertyValue(IValue index);

    /**
     * Установить свойство по имени.
     *
     * @param index Имя свойства.
     * @param value Значение свойства.
     */
    void setPropertyValue(IValue index, IValue value);

    /**
     * Свойство существует.
     *
     * @param index Имя свойства.
     */
    boolean hasProperty(IValue index);
}
