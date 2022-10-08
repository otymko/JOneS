/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringLineCleaner {

    private final String QUOTE = "\"";
    private final String SINGLE_QUOTE = "'";

    public String clean(String inValue) {
        return clean(inValue, QUOTE);
    }

    public String cleanSingleQuote(String inValue) {
        return clean(inValue, SINGLE_QUOTE);
    }

    public String clean(String inValue, String quote) {
        final String DOUBLE_QUOTE = quote + quote;

        String value;
        if (inValue.startsWith(quote)) {
            value = inValue.substring(1, inValue.length() - 1);
        } else {
            value = inValue;
        }
        value = value.replaceAll(DOUBLE_QUOTE, quote);
        return value;
    }

}
