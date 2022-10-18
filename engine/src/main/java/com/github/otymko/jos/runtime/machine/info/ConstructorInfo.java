/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import lombok.Value;

import java.lang.reflect.Method;

/**
 * Информация о конструкторе.
 */
@Value
public class ConstructorInfo {
    /**
     * Параметры конструктора.
     */
    ParameterInfo[] parameters;
    /**
     * Ссылка на нативный метод.
     */
    Method method;
}
