/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import lombok.Value;

/**
 * Адрес символа.
 */
@Value
public class SymbolAddress {
    /**
     * Индекс символа внутри контекста.
     */
    int symbolId;
    /**
     * Индекс контекста.
     */
    int contextId;
}
