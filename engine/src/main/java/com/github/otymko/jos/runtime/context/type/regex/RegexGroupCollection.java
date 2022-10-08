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

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@ContextClass(name = "КоллекцияГруппРегулярногоВыражения", alias = "RegExGroupCollection")
public class RegexGroupCollection extends ContextValue implements CollectionIterable<IValue>, IndexAccessor {
    public static final ContextInfo INFO = ContextInfo.createByClass(RegexGroupCollection.class);

    private final MatchResult result;
    private final Pattern pattern;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public RegexGroupCollection(MatchResult result, Pattern pattern) {
        this.result = result;
        this.pattern = pattern;
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public IValue getCount() {
        return ValueFactory.create(result.groupCount() + 1);
    }

    public IValue getByName(IValue inputName) {
        // TODO: не реализовано
        return ValueFactory.create();
    }


    @Override
    public IteratorValue iterator() {
        var groups = new ArrayList<IValue>();
        for (var position = 0; position < result.groupCount() + 1; position++) {
            groups.add(new RegexGroup(result.group(position)));
        }
        return new IteratorValue(groups.iterator());
    }

    @Override
    public IValue getIndexedValue(IValue inputIndex) {
        var index = inputIndex.getRawValue().asNumber().intValue();
        return new RegexGroup(result.group(index));
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException(index.asString());
    }
}
