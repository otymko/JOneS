/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Определение метода.
 */
@Data
public class MethodDefinition {
    /**
     * Точка входа.
     */
    private int entry = -1;
    /**
     * Признак, что это метод из тела модуля.
     */
    private boolean bodyMethod;
    /**
     * Сигнатура метода.
     */
    private MethodInfo signature;
    /**
     * Переменные метода.
     */
    private List<VariableInfo> variables = new ArrayList<>();
}
