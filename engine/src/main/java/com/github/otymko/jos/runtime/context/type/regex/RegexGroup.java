/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.regex;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "ГруппаРегулярногоВыражения", alias = "RegExGroup")
public class RegexGroup extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(RegexGroup.class);

    @ContextProperty(name = "Значение", alias = "Value", accessMode = PropertyAccessMode.READ_ONLY)
    private final String value;

    @ContextProperty(name = "Индекс", alias = "Index", accessMode = PropertyAccessMode.READ_ONLY)
    private final int index;

    @ContextProperty(name = "Длина", alias = "Length", accessMode = PropertyAccessMode.READ_ONLY)
    private final int length;

    @ContextProperty(name = "Имя", alias = "Name", accessMode = PropertyAccessMode.READ_ONLY)
    private final String name;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public RegexGroup(String value) {
        this.value = value;
        // TODO: не реализовано
        this.index = 0;
        this.length = value.length();
        // TODO: не реализовано
        this.name = null;
    }

}
