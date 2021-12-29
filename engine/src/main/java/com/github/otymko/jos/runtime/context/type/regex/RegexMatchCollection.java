/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.regex;

import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContextClass(name = "КоллекцияСовпаденийРегулярногоВыражения", alias = "RegExMatchCollection")
public class RegexMatchCollection extends ContextValue implements CollectionIterable<RegexMatch> {
  public static final ContextInfo INFO = ContextInfo.createByClass(RegexMatchCollection.class);

  private final Matcher matcher;
  private final Pattern pattern;

  public RegexMatchCollection(Matcher matcher, Pattern pattern) {
    this.matcher = matcher;
    this.pattern = pattern;
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  @Override
  public IteratorValue iterator() {
    var iterator = matcher.results()
      .map(result -> new RegexMatch(result, pattern))
      .map(IValue.class::cast)
      .iterator();
    return new IteratorValue(iterator);
  }

  @ContextMethod(name = "Количество", alias = "Count")
  public IValue getCount() {
    return ValueFactory.create(BigDecimal.valueOf(matcher.results().count()));
  }

}
