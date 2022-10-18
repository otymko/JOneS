/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Определение параметра метода.
 */
@Value
@Builder
public class ParameterInfo {
    /**
     * Имя параметра метода.
     */
    String name;

    /**
     * Передача параметра по значению.
     */
    @Builder.Default
    boolean byValue = false;

    /**
     * Имеет значение по умолчанию.
     */
    @Builder.Default
    @Accessors(fluent = true)
    boolean hasDefaultValue = false;

    /**
     * Индекс значения по умолчанию.
     */
    @Builder.Default
    int defaultValueIndex = -1;

    // TODO: Аннотации
}
