/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.core.PropertyAccessMode;
import lombok.Value;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Информация о свойстве.
 */
@Value
public class PropertyInfo {
    /**
     * Имя свойства на русском.
     */
    String name;
    /**
     * Альтернативное имя свойства на английском.
     */
    String alias;
    /**
     * Режим доступа к свойству.
     */
    PropertyAccessMode accessMode;
    /**
     * Ссылка на нативное поле.
     */
    Field field;
    /**
     * Ссылка на нативный метод установки значения.
     */
    @Accessors(fluent = true)
    boolean hasSetter;
    Method setter;
    /**
     * Ссылка на нативный метод получения значения.
     */
    @Accessors(fluent = true)
    boolean hasGetter;
    Method getter;
}
