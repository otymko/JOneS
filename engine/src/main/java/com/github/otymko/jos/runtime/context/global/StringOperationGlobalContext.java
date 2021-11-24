/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.compiler.EnumerationHelper;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.GlobalContextClass;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.enumeration.SearchDirection;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@GlobalContextClass
@NoArgsConstructor
public class StringOperationGlobalContext implements AttachableContext {
  public static final ContextInfo INFO = ContextInfo.createByClass(StringOperationGlobalContext.class);

  //region AttachableContext

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  //endregion

  @ContextMethod(name = "СтрНайти", alias = "StrFind")
  public static IValue find(IValue where, IValue what, IValue direction, IValue start, IValue occurrence) {
    var whereValue = where.getRawValue().asString();
    var whatValue = what.getRawValue().asString();
    var directionValue = EnumerationHelper.getEnumValueOrDefault(direction, SearchDirection.FROM_BEGIN);
    var startValue = start == null ? 0 : start.getRawValue().asNumber().intValue();
    var occurrenceValue = occurrence == null ? 1 : occurrence.getRawValue().asNumber().intValue();

    var length = whereValue.length();
    if (length == 0 || whatValue.length() == 0) {
      return ValueFactory.create(0);
    }

    var fromBegin = directionValue.getValue() == SearchDirection.FROM_BEGIN;
    if (startValue == 0) {
      startValue = fromBegin ? 1 : length;
    }

    if (startValue < 1 || startValue > length) {
      throw MachineException.invalidArgumentValueException();
    }

    if (occurrenceValue == 0) {
      occurrenceValue = 1;
    }

    var startIndex = startValue - 1;
    var foundTimes = 0;
    var index = length + 1;

    if (fromBegin) {
      while (foundTimes < occurrenceValue && index >= 0) {
        index = whereValue.indexOf(whatValue);
        if (index >= 0) {
          startIndex = index + 1;
          foundTimes++;
        }
        if (startIndex >= length) {
          break;
        }
      }
    } else {
      while (foundTimes < occurrenceValue && index >= 0) {
        index = whereValue.lastIndexOf(whatValue);
        if (index >= 0) {
          startIndex = index - 1;
          foundTimes++;
        }
        if (startIndex < 0) {
          break;
        }
      }
    }

    if (foundTimes == occurrenceValue) {
      return ValueFactory.create(index + 1);
    }
    return ValueFactory.create(0);
  }

  @ContextMethod(name = "СтрНачинаетсяС", alias = "StrStartsWith")
  public static IValue startsWith(IValue inputString, IValue searchString) {
    var inputValue = getStringArgument(inputString);
    var searchValue = getStringArgument(searchString);

    boolean result;
    if (!inputValue.isEmpty()) {
      if (!searchValue.isEmpty()) {
        result = inputValue.startsWith(searchValue);
      } else {
        throw MachineException.methodCallWrongArgValue("СтрНачинаетсяС", 2);
      }
    } else {
      result = false;
    }
    return ValueFactory.create(result);
  }

  @ContextMethod(name = "СтрЗаканчиваетсяНа", alias = "StrEndsWith")
  public static IValue endsWith(IValue inputString, IValue searchString) {
    var inputValue = getStringArgument(inputString);
    var searchValue = getStringArgument(searchString);

    boolean result;
    if (!inputValue.isEmpty()) {
      if (!searchValue.isEmpty()) {
        result = inputValue.endsWith(searchValue);
      } else {
        throw MachineException.methodCallWrongArgValue("СтрЗаканчиваетсяНа", 2);
      }
    } else {
      result = false;
    }
    return ValueFactory.create(result);
  }

  @ContextMethod(name = "СтрРазделить", alias = "StrSplit")
  public static IValue strSplit(IValue source, IValue delimiter, IValue includeEmpty) {
    final var sourceString = getStringArgument(source);
    final var delimiterString = getStringArgument(delimiter);
    final var includeEmptyFlag = includeEmpty == null || includeEmpty.asBoolean();

    final var result = V8Array.constructor();
    if (delimiterString.isEmpty()) {
      result.add(ValueFactory.create(sourceString));
    } else {
      final var substrings = sourceString.split(Pattern.quote(delimiterString));
      for (final var element : substrings) {
        if (!includeEmptyFlag && element.isEmpty()) {
          continue;
        }
        result.add(ValueFactory.create(element));
      }
    }

    return result;
  }

  private static String getStringArgument(IValue argument) {
    return argument == null ? "" : argument.asString();
  }

}
