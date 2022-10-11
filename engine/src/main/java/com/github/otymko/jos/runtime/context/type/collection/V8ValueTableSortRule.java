/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import lombok.Value;

import java.util.regex.Pattern;

@Value
class V8ValueTableSortRule {
    public static int ASC = 1;
    public static int DESC = -1;

    private static Pattern splitter = Pattern.compile(" ");

    private static String DIRECTION_ASC_RU = "ВОЗР";
    private static String DIRECTION_ASC_EN = "ASC";

    private static String DIRECTION_DESC_RU = "УБЫВ";
    private static String DIRECTION_DESC_EN = "DESC";

    V8ValueTableColumn column;
    int order; // +1, -1

    public int apply(V8ValueTableRow r1, V8ValueTableRow r2) {
        final var v1 = r1.get(column);
        final var v2 = r2.get(column);
        return v1.compareTo(v2) * order;
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

    static V8ValueTableSortRule parse(String element, V8ValueTableColumnCollection columns) {
        final var ruleElements = splitter.split(element);
        final var columnName = ruleElements[0].trim();
        final var column = columns.findColumnByNameInternal(columnName);
        if (column == null) {
            throw MachineException.invalidArgumentValueException();
        }
        final var direction = ruleElements.length < 2 ? ASC: getDirection(ruleElements[1]);
        return new V8ValueTableSortRule(column, direction);
    }
}
