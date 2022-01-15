/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.regex;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ContextClass(name = "КоллекцияСовпаденийРегулярногоВыражения", alias = "RegExMatchCollection")
public class RegexMatchCollection extends ContextValue implements CollectionIterable<RegexMatch>, IndexAccessor {
  public static final ContextInfo INFO = ContextInfo.createByClass(RegexMatchCollection.class);

  private final Matcher matcher;
  private final Pattern pattern;
  private final List<MatchResult> results;

  public RegexMatchCollection(Matcher matcher, Pattern pattern) {
    this.matcher = matcher;
    this.pattern = pattern;

    this.results = matcher.results().collect(Collectors.toList());
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  @Override
  public IteratorValue iterator() {
    var iterator = results.stream()
      .map(result -> new RegexMatch(result, pattern))
      .map(IValue.class::cast)
      .iterator();
    return new IteratorValue(iterator);
  }

  @ContextMethod(name = "Количество", alias = "Count")
  public IValue getCount() {
    return ValueFactory.create(BigDecimal.valueOf(results.size()));
  }

  @Override
  public IValue getIndexedValue(IValue inputIndex) {
    var index = inputIndex.getRawValue().asNumber().intValue();
    var result = (MatchResult) results.toArray()[index];
    return new RegexMatch(result, pattern);
  }

  @Override
  public void setIndexedValue(IValue index, IValue value) {
    throw MachineException.getPropertyIsNotWritableException(index.asString());
  }

}
