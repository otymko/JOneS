/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Вспомогательный класс, отвечающий за разбор строки параметров в соответствие @{code FormatParametersList}.
 *
 * @see FormatParametersList
 */
final class FormatParametersListBuilder {

    private static final char SINGLE_QUOTE = '\'';
    private static final char DOUBLE_QUOTE = '\"';
    private static final char PARAM_DELIMITER = ';';
    private static final char NAME_VALUE_DELIMITER = '=';

    private final String format;
    private int index;

    FormatParametersListBuilder(String format) {
        this.format = format;
    }

    public static FormatParametersList build(String params) {
        final var builder = new FormatParametersListBuilder(params);
        return new FormatParametersList(builder.parseParams());
    }

    private Map<String, String> parseParams() {
        final Map<String, String> paramList = new HashMap<>();
        index = 0;
        while (canReadParam()) {

            final var paramName = readParameterName();
            if (paramName.isEmpty()) {
                continue;
            }

            final var value = readParameterValue();
            if (value.isEmpty()) {
                continue;
            }

            paramList.put(paramName.get().toUpperCase(), value.get());
        }
        return paramList;
    }

    private boolean canReadParam() {
        // пропускаем пробелы и разделители
        while (!outOfText()) {
            if (currentChar() == PARAM_DELIMITER) {
                moveNext();
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean outOfText() {
        skipWhitespace();
        return !canRead();
    }

    private Optional<String> readParameterName() {
        if (!isNameFirstCharacter(currentChar())) {
            return Optional.empty();
        }

        final var nameBuilder = new StringBuilder();

        while (canRead()) {
            final var currentChar = currentChar();
            if (currentChar == PARAM_DELIMITER) {
                break;
            }
            if (currentChar == NAME_VALUE_DELIMITER) {
                moveNext();
                return Optional.of(nameBuilder.toString().trim());
            }
            nameBuilder.append(currentChar);
            moveNext();
        }
        return Optional.empty();
    }

    private static boolean isNameFirstCharacter(char ch) {
        return !Character.isWhitespace(ch) && ch != PARAM_DELIMITER && ch != NAME_VALUE_DELIMITER;
    }

    private static boolean isQuote(char c) {
        return c == DOUBLE_QUOTE
                || c == SINGLE_QUOTE;
    }

    private Optional<String> readParameterValue() {

        if (outOfText()) {
            return Optional.of("");
        }

        final var valueBuilder = new StringBuilder();

        if (isQuote(currentChar())) {
            return readQuotedValue();
        }

        while (canRead()) {

            if (Character.isWhitespace(currentChar()) || currentChar() == PARAM_DELIMITER) {
                break;
            }

            valueBuilder.append(currentChar());
            moveNext();
        }

        return Optional.of(valueBuilder.toString());
    }

    private Optional<String> readQuotedValue() {
        final var valueBuilder = new StringBuilder();

        final var quote = currentChar();
        moveNext();

        while (canRead()) {
            if (currentChar() == quote) {
                moveNext();
                if (!canRead() || currentChar() != quote) {
                    return Optional.of(valueBuilder.toString());
                }
            }
            valueBuilder.append(currentChar());
            moveNext();
        }

        return Optional.empty();
    }

    private void skipWhitespace() {
        while (canRead()) {
            if (!Character.isWhitespace(currentChar())) {
                break;
            }
            moveNext();
        }
    }

    private void moveNext() {
        index++;
    }

    private boolean canRead() {
        return index < format.length();
    }

    private char currentChar() {
        return format.charAt(index);
    }

}
