/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.core.annotation.EnumValue;

@EnumClass(name = "НаправлениеПоиска", alias = "SearchDirection")
public enum SearchDirection implements EnumType {
    @EnumValue(name = "СНачала", alias = "FromBegin")
    FROM_BEGIN,
    @EnumValue(name = "СКонца", alias = "FromEnd")
    FROM_END;

    public static final EnumerationContext INFO = new EnumerationContext(SearchDirection.class);

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
