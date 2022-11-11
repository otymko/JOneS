/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.core.annotation.EnumValue;
import com.github.otymko.jos.runtime.context.EnumType;

@EnumClass(name = "НаправлениеСортировки", alias = "SortDirection")
public enum SortDirection implements EnumType {
    @EnumValue(name = "Возр", alias = "Asc")
    ASC,
    @EnumValue(name = "Убыв", alias = "Desc")
    DESC;

    public static final EnumerationContext INFO = new EnumerationContext(SortDirection.class);

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }

    public int getOrder() {
        if (this == ASC) {
            return 1;
        }

        return -1;
    }
}
