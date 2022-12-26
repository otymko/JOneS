/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.exception.CompilerException;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.enumeration.SearchDirection;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Глобальные методы по работе со строками.
 */
@GlobalContextClass
@NoArgsConstructor
public class StringOperationGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(StringOperationGlobalContext.class);

    private static final Pattern STR_TEMPLATE_PATTERN =
            Pattern.compile("(%%)|%(\\d+)|%\\((\\d+)\\)|%", Pattern.MULTILINE);

    @ContextMethod(name = "СтрНайти", alias = "StrFind")
    public static int find(String where, String what, SearchDirection sourceDirection, Integer sourceStart,
                           Integer sourceOccurrence) {

        var direction = sourceDirection == null ? SearchDirection.FROM_BEGIN : sourceDirection;
        var start = sourceStart == null ? 0 : sourceStart;
        var occurrence = sourceOccurrence == null ? 1 : sourceOccurrence;

        var length = where.length();
        if (length == 0 || what.length() == 0) {
            return 0;
        }

        var fromBegin = direction == SearchDirection.FROM_BEGIN;
        if (start == 0) {
            start = fromBegin ? 1 : length;
        }

        if (start < 1 || start > length) {
            throw MachineException.invalidArgumentValueException();
        }

        if (occurrence == 0) {
            occurrence = 1;
        }

        var startIndex = start - 1;
        var foundTimes = 0;
        var index = length + 1;

        if (fromBegin) {
            while (foundTimes < occurrence && index >= 0) {
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
            while (foundTimes < occurrence && index >= 0) {
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

        if (foundTimes == occurrence) {
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
    public static String strConcat(V8Array array, String sourceSeparator) {
        var separator = sourceSeparator == null ? "" : sourceSeparator;

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

    @ContextMethod(name = "СтрШаблон", alias = "StrTemplate")
    public static String strTemplate(String template, IValue p1, IValue p2, IValue p3, IValue p4, IValue p5,
                                     IValue p6, IValue p7, IValue p8, IValue p9, IValue p10) {
        String result = getStringArgument(template);
        var arguments = new IValue[] { p10, p9, p8, p7, p6, p5, p4, p3, p2, p1 };

        AtomicInteger maxNumber = new AtomicInteger(0);
        AtomicInteger indexMatcher = new AtomicInteger(0);

        var matcher = STR_TEMPLATE_PATTERN.matcher(result);
        if (matcher.find()) {
            result = matcher.replaceAll(matchResult -> {
                indexMatcher.set(indexMatcher.get() + 1);

                if (matchResult.group(1) != null) {
                    return "%";
                }

                if (matchResult.group(2) != null || matchResult.group(3) != null) {
                    var number = Integer.parseInt(matchResult.group(2) != null
                            ? matchResult.group(2)
                            : matchResult.group(3));

                    if (number < 1 || number > 10) {
                        throw MachineException.templateSyntaxErrorAtPositionInvalidSubstitutionNumber(
                                indexMatcher.get() + 1);
                    }

                    if (number > maxNumber.get()) {
                        maxNumber.set(number);
                    }

                    var argument = arguments[10 - number];
                    if (argument != null && argument.getDataType() != DataType.UNDEFINED) {
                        return argument.asString();
                    }
                    else {
                        return "";
                    }
                }

                throw MachineException.templateSyntaxErrorAtPosition(indexMatcher.get() + 1);
            });
        }

        var passedArgsCount = (int)Arrays.stream(arguments)
                .filter(Predicate.not(iValue -> iValue == null || iValue.getDataType() == DataType.UNDEFINED))
                .count();
        if (passedArgsCount > maxNumber.get()) {
            throw CompilerException.tooManyMethodArgumentsException();
        }

        return result;
    }

    private static String getStringArgument(String argument) {
        return argument == null ? "" : argument;
    }

    //region AttachableContext

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    //endregion
}
