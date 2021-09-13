/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.regex.Pattern;

@ContextClass(name = "РегулярноеВыражение", alias = "Regex")
public class V8Regex extends ContextValue implements PropertyNameAccessor {
  public static final ContextInfo INFO = ContextInfo.createByClass(V8Regex.class);

  private final String pattern;
  private Pattern regex;

  private V8Regex(String pattern) {
    this.pattern = pattern;
    this.regex = Pattern.compile(this.pattern, Pattern.UNICODE_CASE);
  }

  //region ContextValue

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  //endregion

  //region PropertyNameAccessor

  @Override
  public IValue getPropertyValue(IValue index) {
    return null;
  }

  @Override
  public void setPropertyValue(IValue index, IValue value) {

  }

  @Override
  public boolean hasProperty(IValue index) {
    return false;
  }

  //endregion

  @Getter
  @ContextProperty(name = "ИгнорироватьРегистр", alias = "IgnoreCase")
  public BooleanValue ignoreCase = (BooleanValue) ValueFactory.create(false);

  @Getter
  @ContextProperty(name = "Многострочный", alias = "Multiline")
  public BooleanValue multiline = (BooleanValue) ValueFactory.create(false);

  public void setIgnoreCase(IValue inputIgnoreCase) {
    if (!ignoreCase.equals(inputIgnoreCase.getRawValue())) {
      ignoreCase = (BooleanValue) inputIgnoreCase.getRawValue();
      int flags = getPatternFlags();
      regex = Pattern.compile(pattern, flags);
    }
  }

  public void setMultiline(IValue inputMultiline) {
    if (!multiline.equals(inputMultiline.getRawValue())) {
      multiline = (BooleanValue) inputMultiline.getRawValue();
      int flags = getPatternFlags();
      regex = Pattern.compile(pattern, flags);
    }
  }

  @ContextConstructor
  public static V8Regex constructor(IValue pattern) {
    return new V8Regex(pattern.asString());
  }

  @ContextMethod(name = "Совпадает", alias = "IsMatch")
  public IValue isMatch(IValue inputString, IValue startAt) {
    var value = inputString.asString();

    var startAtValue = startAt == null ? 0 : startAt.asNumber().intValue();
    if (startAtValue > 0) {
      return ValueFactory.create(regex.matcher(value).find(startAtValue));
    }
    return ValueFactory.create(regex.matcher(value).find());
  }

  @ContextMethod(name = "НайтиСовпадения", alias = "Matches")
  public IValue matches() {
    // TODO: реализовать
    return ValueFactory.create();
  }

  @ContextMethod(name = "Разделить", alias = "Split")
  public IValue split(IValue inputString, IValue count, IValue startAt) {
    var inputValue = inputString.getRawValue().asString();
    var countValue = count == null ? 0 : count.getRawValue().asNumber().intValue();

    // TODO: startAt не используется

    var values = regex.split(inputValue, countValue);
    var array = new V8Array();
    for (var value : values) {
      array.add(ValueFactory.create(value));
    }
    return array;
  }

  @ContextMethod(name = "Заменить", alias = "Replace")
  public IValue replace(IValue inputString, IValue replacement) {
    var value = inputString.asString();
    var replacementValue = replacement.asString();
    return ValueFactory.create(value.replace(pattern, replacementValue));
  }

  private int getPatternFlags() {
    int flags = Pattern.UNICODE_CASE;
    if (multiline.asBoolean()) {
      flags = flags | Pattern.MULTILINE;
    }
    if (ignoreCase.asBoolean()) {
      flags = flags | Pattern.CASE_INSENSITIVE;
    }
    return flags;
  }

}
