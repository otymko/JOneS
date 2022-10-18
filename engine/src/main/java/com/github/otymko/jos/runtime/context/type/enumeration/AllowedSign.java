/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.core.annotation.EnumValue;

/**
 * Допустимые знаки в Описании типов
 */
@EnumClass(name = "ДопустимыйЗнак", alias = "AllowedSign")
public enum AllowedSign implements EnumType {

    /**
     * Знак может быть любым
     */
    @EnumValue(name = "Любой", alias = "Any")
    ANY,

    /**
     * Не может содерэать отрицательные значения
     */
    @EnumValue(name = "Неотрицательный", alias = "NonNegative")
    NON_NEGATIVE;

    public static final EnumerationContext INFO = new EnumerationContext(AllowedSign.class);

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
