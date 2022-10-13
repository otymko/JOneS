/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import lombok.Value;

import java.util.regex.Pattern;

/**
 * Элемент правил сортировки таблицы значений.
 *
 * @see V8ValueTable
 * @see V8ValueTableSorter
 */
@Value
class V8ValueTableSortRule {
    public static int ASC = 1;
    public static int DESC = -1;

    private static Pattern splitter = Pattern.compile(" ");

    private static String DIRECTION_ASC_RU = "ВОЗР";
    private static String DIRECTION_ASC_EN = "ASC";

    private static String DIRECTION_DESC_RU = "УБЫВ";
    private static String DIRECTION_DESC_EN = "DESC";

    private static int COLUMN_NAME_INDEX = 0;
    private static int SORT_DIRECTION_INDEX = 1;

    V8ValueTableColumn column;
    int order; // +1, -1

    static V8ValueTableSortRule parse(String element, V8ValueTableColumnCollection columns) {
        final var ruleElements = splitter.split(element);
        final var columnName = ruleElements[COLUMN_NAME_INDEX].trim();
        final var column = columns.findColumnByNameInternal(columnName);
        if (column == null) {
            throw MachineException.invalidArgumentValueException();
        }
        int direction = ASC;
        if (ruleElements.length > SORT_DIRECTION_INDEX) {
            direction = getDirection(ruleElements[SORT_DIRECTION_INDEX]);
        }
        return new V8ValueTableSortRule(column, direction);
    }

    private static int getDirection(String direction) {
        if (direction.equalsIgnoreCase(DIRECTION_ASC_EN)
                || direction.equalsIgnoreCase(DIRECTION_ASC_RU)) {
            return ASC;
        }
        if (direction.equalsIgnoreCase(DIRECTION_DESC_EN)
                || direction.equalsIgnoreCase(DIRECTION_DESC_RU)) {
            return DESC;
        }
        throw MachineException.invalidArgumentValueException();
    }

    public int apply(V8ValueTableRow r1, V8ValueTableRow r2) {
        final var v1 = r1.get(column);
        final var v2 = r2.get(column);
        return v1.compareTo(v2) * order;
    }

}
