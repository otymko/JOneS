/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.regex;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.regex.Pattern;

/**
 * Реализация типа "РегулярноеВыражение"
 */
@ContextClass(name = "РегулярноеВыражение", alias = "Regex")
public class Regex extends ContextValue implements IndexAccessor {
    public static final ContextInfo INFO = ContextInfo.createByClass(Regex.class);

    private final String pattern;
    private Pattern regularExpression;
    public boolean ignoreCase = true;
    public boolean multiline = true;

    private Regex(String pattern) {
        this.pattern = pattern;
        recompileRegular();
    }

    //region ContextValue

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    //endregion

    //region IndexAccessor

    @Override
    public IValue getIndexedValue(IValue index) {
        var rawValue = index.getRawValue();
        if (rawValue.getDataType() != DataType.STRING) {
            throw MachineException.invalidArgumentValueException();
        }

        var name = rawValue.asString();
        var position = findProperty(name);
        return getPropertyValue(position);
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        var rawValue = index.getRawValue();
        if (rawValue.getDataType() != DataType.STRING) {
            throw MachineException.invalidArgumentValueException();
        }

        var name = rawValue.asString();
        var position = findProperty(name);
        setPropertyValue(position, value);
    }

    //endregion

    @ContextProperty(name = "ИгнорироватьРегистр", alias = "IgnoreCase")
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;

        recompileRegular();
    }

    public boolean getIgnoreCase() {
        return ignoreCase;
    }

    @ContextProperty(name = "Многострочный", alias = "Multiline")
    public void setMultiline(boolean multiline) {
        this.multiline = multiline;

        recompileRegular();
    }

    public boolean getMultiline() {
        return multiline;
    }

    @ContextConstructor
    public static Regex constructor(IValue pattern) {
        return new Regex(pattern.asString());
    }

    @ContextMethod(name = "Совпадает", alias = "IsMatch")
    public boolean isMatch(String value, Integer startAt) {
        if (startAt != null && startAt > 0) {
            return regularExpression.matcher(value).find(startAt);
        }

        return regularExpression.matcher(value).find();
    }

    @ContextMethod(name = "НайтиСовпадения", alias = "Matches")
    public RegexMatchCollection matches(String value, Integer startAt) {
        // TODO: учесть поиск с определенной позиции
        return new RegexMatchCollection(regularExpression.matcher(value), regularExpression);
    }

    @ContextMethod(name = "Разделить", alias = "Split")
    public V8Array split(String value, Integer count, IValue startAt) {
        var countValue = count == null ? 0 : count;

        // TODO: startAt не используется
        var result = regularExpression.split(value, countValue);
        var array = V8Array.create();
        for (var item : result) {
            array.add(ValueFactory.create(item));
        }

        return array;
    }

    @ContextMethod(name = "Заменить", alias = "Replace")
    public String replace(String value, String replacement) {
        return value.replaceAll(pattern, replacement);
    }

    private int getPatternFlags() {
        int flags = Pattern.UNICODE_CASE;
        if (multiline) {
            flags = flags | Pattern.MULTILINE;
        }
        if (ignoreCase) {
            flags = flags | Pattern.CASE_INSENSITIVE;
        }
        return flags;
    }

    private void recompileRegular() {
        int flags = getPatternFlags();
        regularExpression = Pattern.compile(pattern, flags);
    }
}
