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

import java.util.StringJoiner;
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
    public static int find(String where, String what, IValue direction, IValue start, IValue occurrence) {
        var directionValue = EnumerationHelper.getEnumValueOrDefault(direction, SearchDirection.FROM_BEGIN);
        var startValue = start == null ? 0 : start.getRawValue().asNumber().intValue();
        var occurrenceValue = occurrence == null ? 1 : occurrence.getRawValue().asNumber().intValue();

        var length = where.length();
        if (length == 0 || what.length() == 0) {
            return 0;
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
                index = where.indexOf(what);
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
                index = where.lastIndexOf(what);
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
            return index + 1;
        }

        return 0;
    }

    @ContextMethod(name = "СтрНачинаетсяС", alias = "StrStartsWith")
    public static boolean startsWith(String inputString, String searchString) {
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

        return result;
    }

    @ContextMethod(name = "СтрЗаканчиваетсяНа", alias = "StrEndsWith")
    public static boolean endsWith(String inputString, String searchString) {
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

        return result;
    }

    @ContextMethod(name = "СтрРазделить", alias = "StrSplit")
    public static V8Array strSplit(String source, String delimiter, Boolean includeEmpty) {
        final var sourceString = getStringArgument(source);
        final var delimiterString = getStringArgument(delimiter);
        final var includeEmptyFlag = includeEmpty == null || includeEmpty;

        final var result = V8Array.create();
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

    @ContextMethod(name = "СтрСравнить", alias = "StrCompare")
    public static int strCompare(String left, String right) {
        var result = left.compareToIgnoreCase(right);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @ContextMethod(name = "СтрСоединить", alias = "StrConcat")
    public static String strConcat(V8Array array, IValue inputSeparator) {
        var separator = inputSeparator == null ? "" : inputSeparator.getRawValue().asString();

        var joiner = new StringJoiner(separator);
        for (var value : array.iterator()) {
            joiner.add(value.getRawValue().asString());
        }

        return joiner.toString();
    }

    @ContextMethod(name = "СтрЧислоСтрок", alias = "StrLineCount")
    public static int strLineCount(String value) {
        int pos = 0;
        int lineCount = 1;
        while (pos >= 0 && pos < value.length()) {
            pos = value.indexOf('\n', pos);
            if (pos >= 0) {
                lineCount++;
                pos++;
            }
        }

        return lineCount;
    }

    @ContextMethod(name = "СтрЧислоВхождений", alias = "StrOccurrenceCount")
    public static int strOccurrenceCount(String where, String what) {
        var pos = where.indexOf(what);
        var occurrenceCount = 0;
        while(pos >= 0) {
            occurrenceCount++;
            var nextIndex = pos + what.length();
            if (nextIndex >= where.length()) {
                break;
            }

            pos = where.indexOf(what, nextIndex);
        }

        return occurrenceCount;
    }

    @ContextMethod(name = "СтрПолучитьСтроку", alias = "StrGetLine")
    public static String strGetLine(String source, int lineNumber)
    {
        String result = "";
        if (lineNumber >= 1) {
            String[] lines = source.split("\n", lineNumber + 1);
            result = lines[lineNumber - 1];
        }

        return result;
    }

    private static String getStringArgument(String argument) {
        return argument == null ? "" : argument;
    }
}
