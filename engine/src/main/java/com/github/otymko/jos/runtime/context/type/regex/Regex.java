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
import lombok.Getter;

import java.util.regex.Pattern;

@ContextClass(name = "РегулярноеВыражение", alias = "Regex")
public class Regex extends ContextValue implements IndexAccessor {
    public static final ContextInfo INFO = ContextInfo.createByClass(Regex.class);

    private final String pattern;
    private Pattern regularExpression;

    @Getter
    @ContextProperty(name = "ИгнорироватьРегистр", alias = "IgnoreCase")
    public IValue ignoreCase = ValueFactory.create(true);

    @Getter
    @ContextProperty(name = "Многострочный", alias = "Multiline")
    public IValue multiline = ValueFactory.create(true);

    private Regex(String pattern) {
        this.pattern = pattern;
        this.regularExpression = Pattern.compile(this.pattern, getPatternFlags());
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

    public void setIgnoreCase(IValue inputIgnoreCase) {
        if (!ignoreCase.equals(inputIgnoreCase.getRawValue())) {
            ignoreCase = inputIgnoreCase.getRawValue();
            int flags = getPatternFlags();
            regularExpression = Pattern.compile(pattern, flags);
        }
    }

    public void setMultiline(IValue inputMultiline) {
        if (!multiline.equals(inputMultiline.getRawValue())) {
            multiline = inputMultiline.getRawValue();
            int flags = getPatternFlags();
            regularExpression = Pattern.compile(pattern, flags);
        }
    }

    @ContextConstructor
    public static Regex constructor(IValue pattern) {
        return new Regex(pattern.asString());
    }

    @ContextMethod(name = "Совпадает", alias = "IsMatch")
    public IValue isMatch(IValue inputString, IValue startAt) {
        var value = inputString.getRawValue().asString();

        var startAtValue = startAt == null ? 0 : startAt.getRawValue().asNumber().intValue();
        if (startAtValue > 0) {
            return ValueFactory.create(regularExpression.matcher(value).find(startAtValue));
        }
        return ValueFactory.create(regularExpression.matcher(value).find());
    }

    @ContextMethod(name = "НайтиСовпадения", alias = "Matches")
    public IValue matches(IValue inputString, IValue startAt) {
        var value = inputString.getRawValue().asString();

        // TODO: учесть поиск с определенной позиции
        //var startAtValue = startAt == null ? 0 : startAt.getRawValue().asNumber().intValue();

        return new RegexMatchCollection(regularExpression.matcher(value), regularExpression);
    }

    @ContextMethod(name = "Разделить", alias = "Split")
    public IValue split(IValue inputString, IValue count, IValue startAt) {
        var inputValue = inputString.getRawValue().asString();
        var countValue = count == null ? 0 : count.getRawValue().asNumber().intValue();

        // TODO: startAt не используется

        var values = regularExpression.split(inputValue, countValue);
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
        return ValueFactory.create(value.replaceAll(pattern, replacementValue));
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
