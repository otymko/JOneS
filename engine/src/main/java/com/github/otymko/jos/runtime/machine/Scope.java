/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import com.github.otymko.jos.core.IVariable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Область контекста.
 */
@Value
@RequiredArgsConstructor
public class Scope {
    /**
     * Экземпляр объекта.
     */
    RuntimeContext instance;
    /**
     * Переменные.
     */
    IVariable[] variables;
    /**
     * Методы.
     */
    MethodInfo[] methods;
}
