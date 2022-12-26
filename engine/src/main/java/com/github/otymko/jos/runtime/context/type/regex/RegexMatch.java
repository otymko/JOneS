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

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@ContextClass(name = "СовпадениеРегулярногоВыражения", alias = "RegExMatch")
public class RegexMatch extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(RegexMatch.class);

    private final MatchResult result;
    private final Pattern pattern;

    @ContextProperty(name = "Значение", alias = "Value", accessMode = PropertyAccessMode.READ_ONLY)
    private final String value;

    @ContextProperty(name = "Индекс", alias = "Index", accessMode = PropertyAccessMode.READ_ONLY)
    private final int index;

    @ContextProperty(name = "Длина", alias = "Index", accessMode = PropertyAccessMode.READ_ONLY)
    private final int length;

    @ContextProperty(name = "Группы", alias = "Groups", accessMode = PropertyAccessMode.READ_ONLY)
    private final RegexGroupCollection groups;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public RegexMatch(MatchResult result, Pattern pattern) {
        this.result = result;
        this.pattern = pattern;

        value = result.group();
        index = result.start();
        length = result.group().length();
        groups = new RegexGroupCollection(result, pattern);
    }

}
