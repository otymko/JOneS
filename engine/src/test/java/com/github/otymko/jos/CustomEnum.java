/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos;

import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.EnumValue;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.context.type.enumeration.SearchDirection;

@EnumClass(name = "МоеПеречисление", alias = "MyEnum")
public enum CustomEnum implements EnumType {
    @EnumValue(name = "Значение1", alias = "Value1")
    VALUE1,
    @EnumValue(name = "Значение2", alias = "Value2")
    VALUE2;

    public static final EnumerationContext INFO = new EnumerationContext(CustomEnum.class);

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
