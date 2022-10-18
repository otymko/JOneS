/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.module;

import com.github.otymko.jos.compiler.ConstantDefinition;
import com.github.otymko.jos.compiler.MethodDefinition;
import com.github.otymko.jos.compiler.SymbolAddress;
import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;

import java.util.List;

/**
 * Скомпилированный модуль.
 */
public interface IModuleImage {
    /**
     * Получить источник модуля.
     */
    ModuleSource getSource();
    /**
     * Получить точку входа модуля.
     */
    int getEntryPoint();
    /**
     * Получить команды (байткоды) модуля.
     */
    List<Command> getCode();
    /**
     * Получить методы модуля.
     */
    List<MethodDefinition> getMethods();
    /**
     * Получить переменные модуля.
     */
    List<VariableInfo> getVariables();
    /**
     * Получить константы модуля.
     */
    List<ConstantDefinition> getConstants();
    /**
     * Получить список ссылок на методы методы.
     */
    List<SymbolAddress> getMethodRefs();
    /**
     * Получить список ссылок на перменные модуля.
     */
    List<SymbolAddress> getVariableRefs();
}
