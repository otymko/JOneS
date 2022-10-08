/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnnotationParameter {
    public static final String UNKNOWN_NAME = "UNKNOWN";
    @Builder.Default
    String name = UNKNOWN_NAME;
    int valueIndex;
    // TODO: нужен ли runtimeValue ?
}
