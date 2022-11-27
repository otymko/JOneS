/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.env;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;

/**
 * Содержит известные поддерживаемые специальные типы каталогов
 */
@EnumClass(name = "СпециальнаяПапка", alias = "SpecialFolder")
public enum SpecialFolder implements EnumType {
    ;
    public static final EnumerationContext INFO = new EnumerationContext(SpecialFolder.class);
    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
