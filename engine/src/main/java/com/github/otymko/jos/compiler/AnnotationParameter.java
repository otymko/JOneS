/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import lombok.Builder;
import lombok.Value;

/**
 * Определение параметра аннотации.
 */
@Value
@Builder
public class AnnotationParameter {
    public static final String UNKNOWN_NAME = "UNKNOWN";

    /**
     * Имя параметра аннотации.
     */
    @Builder.Default
    String name = UNKNOWN_NAME;
    /**
     * Индекс значения с контексте модуля.
     */
    int valueIndex;
}
