/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.IValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class V8ValueTableSorter implements Comparator<IValue> {

    private static final Pattern splitter = Pattern.compile(",");

    private final List<V8ValueTableSortRule> rules;

    V8ValueTableSorter(List<V8ValueTableSortRule> rules) {
        this.rules = rules;
    }

    private static List<V8ValueTableSortRule> parseRules(V8ValueTableColumnCollection columns, IValue sortColumns) {

        if (sortColumns == null) {
            throw MachineException.invalidArgumentValueException();
        }

        final var rules = new ArrayList<V8ValueTableSortRule>();

        final var columnsAsString = sortColumns.asString();
        final var splitted = splitter.split(columnsAsString);
        for (final var ruleElement: splitted) {
            final var rule = V8ValueTableSortRule.parse(ruleElement.trim(), columns);
            rules.add(rule);
        }

        return rules;
    }

    public static V8ValueTableSorter create(V8ValueTableColumnCollection columns, IValue sortColumns) {
        final var rules = parseRules(columns, sortColumns);
        return new V8ValueTableSorter(rules);
    }

    @Override
    public int compare(IValue o1, IValue o2) {
        for (final var rule: rules) {
            final var result = rule.apply((V8ValueTableRow) o1, (V8ValueTableRow) o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
