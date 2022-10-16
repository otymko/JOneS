/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import lombok.experimental.UtilityClass;

/**
 * Утилитный класс для очистки строковых литералов от служебных символов.
 */
@UtilityClass
public class StringLineCleaner {
    private final String QUOTE = "\"";
    private final String SINGLE_QUOTE = "'";

    private final String PIPELINE = "|";

    /**
     * Очистить строку литерала от служебных кавычек.
     *
     * @param source Строка литерала.
     */
    public String clean(String source) {
        return clean(source, QUOTE);
    }

    /**
     * Очистить строку литерала от служебной одинарной кавычки.
     *
     * @param source Строка литерала.
     */
    public String cleanSingleQuote(String source) {
        return clean(source, SINGLE_QUOTE);
    }

    /**
     * Очистить строкой литерал от служебного символа "|".
     *
     * @param source Строковой литерал.
     */
    public String cleanFromPipelineSymbol(String source) {
        if (source.startsWith(PIPELINE)) {
            return source.substring(1);
        }

        return source;
    }

    private String clean(String source, String quote) {
        final String DOUBLE_QUOTE = quote + quote;

        String value;
        if (source.startsWith(quote)) {
            value = source.substring(1, source.length() - 1);
        } else {
            value = source;
        }
        value = value.replaceAll(DOUBLE_QUOTE, quote);

        return value;
    }
}
