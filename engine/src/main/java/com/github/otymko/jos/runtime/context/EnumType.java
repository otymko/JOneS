/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.runtime.context.type.EnumerationValue;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;

/**
 * Базовый интерфейс типов перечислений.
 */
public interface EnumType {
    /**
     * Получить контекст перечисления.
     */
    EnumerationContext getContextInfo();

    /**
     * Получить контекстное значение перечисления.
     */
    default EnumerationValue getEnumerationValue() {
        return getContextInfo().getEnumValueType(this);
    }
}
