/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.runtime.SymbolType;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Информация о переменной для исполнения
 */
@Value
@RequiredArgsConstructor
public class VariableInfo {
    /**
     * Имя переменной на русском.
     */
    String name;
    /**
     * Альтернативное имя переменной на английском.
     */
    String alias;
    /**
     * Тип символа.
     */
    SymbolType type;

    public VariableInfo(String name) {
        this.name = name;
        this.alias = name;
        this.type = SymbolType.VARIABLE;
    }
}
