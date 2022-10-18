/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.IValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Вспомогательный класс сортировки таблицы значений по набору полей и направлений.
 *
 * @see V8ValueTable
 */
class V8ValueTableSorter implements Comparator<IValue> {
    private static final Pattern splitter = Pattern.compile(",");

    private final List<V8ValueTableSortRule> rules;

    /**
     * Создает объект-сортировщик по представлениям полей
     * @param columns - Колонки таблицы значений
     * @param sortColumns - Параметры сортировки в виде "(Колонка [Направление])+"
     * @return объект, позволяющий сравнивать строки по указанным правилам
     */
    public static V8ValueTableSorter create(V8ValueTableColumnCollection columns, String sortColumns) {
        final var rules = parseRules(columns, sortColumns);
        return new V8ValueTableSorter(rules);
    }

    V8ValueTableSorter(List<V8ValueTableSortRule> rules) {
        this.rules = rules;
    }

    private static List<V8ValueTableSortRule> parseRules(V8ValueTableColumnCollection columns, String sortColumns) {

        if (sortColumns == null) {
            throw MachineException.invalidArgumentValueException();
        }

        final var rules = new ArrayList<V8ValueTableSortRule>();

        final var splitted = splitter.split(sortColumns);
        for (final var ruleElement: splitted) {
            final var rule = V8ValueTableSortRule.parse(ruleElement.trim(), columns);
            rules.add(rule);
        }

        return rules;
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
