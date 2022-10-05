/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

/**
 * Операции со строками (утилитный класс)
 */
public final class StringUtils {
    /**
     * Возвращает преобразованную строку: у каждого слова строки первый символ преобразуется к верхнему регистру.
     * Все остальные символы преобразуются к нижнему регистру.
     *
     * @param value Входящее значение
     *
     * @return Преобразованное значение
     */
    public static String toTitleCase(String value) {
        char[] chars = value.toCharArray();
        boolean isWord = false;
        if (chars.length >= 1) {
            if (Character.isLetter(chars[0])) {
                isWord = true;
            }

            if (Character.isLowerCase(chars[0])) {
                chars[0] = Character.toUpperCase(chars[0]);
            }
        }

        for (int i = 1; i < chars.length; i++) {
            if (isWord && Character.isLetter(chars[i])) {
                chars[i] = Character.toLowerCase(chars[i]);
            } else if (charIsSeparator(chars[i]) || charIsPunctuation(chars[i])) {
                isWord = false;
            } else if (!isWord && Character.isLetter(chars[i])) {
                isWord = true;
                if (Character.isLowerCase(chars[i])) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
            }
        }

        return new String(chars);
    }

    private static boolean charIsSeparator(char value) {
        boolean isSeparator = false;
        var type = Character.getType(value);
        if (type == Character.SPACE_SEPARATOR || type == Character.LINE_SEPARATOR
                || type == Character.PARAGRAPH_SEPARATOR) {

            isSeparator = true;
        }

        return isSeparator;
    }

    private static boolean charIsPunctuation(char value) {
        boolean isPunctuation = false;
        var type = Character.getType(value);
        if (type == Character.CONNECTOR_PUNCTUATION || type == Character.DASH_PUNCTUATION
                || type == Character.START_PUNCTUATION || type == Character.END_PUNCTUATION
                || type == Character.INITIAL_QUOTE_PUNCTUATION || type == Character.FINAL_QUOTE_PUNCTUATION
                || type == Character.OTHER_PUNCTUATION) {

            isPunctuation = true;
        }

        return isPunctuation;
    }
}
