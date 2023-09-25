/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.core.annotation.EnumValue;
import com.github.otymko.jos.runtime.context.EnumType;

/**
 * Режим округления
 */
@EnumClass(name = "РежимОкругления", alias = "RoundMode")
public enum RoundMode implements EnumType {

    /**
     * Округлять 0.5 вниз
     */
    @EnumValue(name = "Окр15как10", alias = "HalfDown")
    HALF_DOWN,

    /**
     * Округлять 0.5 вверх
     */
    @EnumValue(name = "Окр15как20", alias = "HalfUp")
    HALF_UP;

    public static final EnumerationContext INFO = new EnumerationContext(RoundMode.class);

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
