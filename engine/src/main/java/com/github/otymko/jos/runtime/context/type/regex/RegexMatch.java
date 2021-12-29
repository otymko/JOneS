/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.regex;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@ContextClass(name = "СовпадениеРегулярногоВыражения", alias = "RegExMatch")
public class RegexMatch extends ContextValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(RegexMatch.class);

  private final MatchResult result;
  private final Pattern pattern;

  @ContextProperty(name = "Значение", alias = "Value", accessMode = PropertyAccessMode.READ_ONLY)
  private final IValue value;

  @ContextProperty(name = "Индекс", alias = "Index", accessMode = PropertyAccessMode.READ_ONLY)
  private final IValue index;

  @ContextProperty(name = "Длина", alias = "Index", accessMode = PropertyAccessMode.READ_ONLY)
  private final IValue length;

  @ContextProperty(name = "Группы", alias = "Groups", accessMode = PropertyAccessMode.READ_ONLY)
  private final IValue groups;

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  public RegexMatch(MatchResult result, Pattern pattern) {
    this.result = result;
    this.pattern = pattern;

    value = ValueFactory.create(result.group());
    index = ValueFactory.create(result.start());
    length = ValueFactory.create(result.group().length());
    groups = new RegexGroupCollection(result, pattern);
  }

}
