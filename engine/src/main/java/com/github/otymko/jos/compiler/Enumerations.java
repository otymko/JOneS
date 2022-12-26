/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import lombok.experimental.UtilityClass;

/**
 * Утилитный класс по работе с перечислениями.
 */
@UtilityClass
public class Enumerations {
    /**
     * Получить контекст перечисления.
     *
     * @param enumClass Класс перечисления.
     */
    public EnumerationContext getEnumByClass(Class<? extends EnumType> enumClass) {
        return TypeManager.getInstance().getEnumByClass(enumClass);
    }

    /**
     * Получить именя значения перечисления.
     *
     * @param value Значение перечисления.
     */
    public String getValueName(EnumType value) {
        return value.getContextInfo().getEnumValueType(value).getName();
    }
}
