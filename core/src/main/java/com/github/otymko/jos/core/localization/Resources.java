/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.core.localization;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Работа с ресурсными сообщениями.
 */
@UtilityClass
public class Resources {
    private static final String LANGUAGE_RU = "ru";
    private static final String LANGUAGE_EN = "en";
    private static final Locale DEFAULT_LOCALE;

    static {
        if (System.getProperty("user.language").contains(LANGUAGE_RU)) {
            DEFAULT_LOCALE = new Locale(LANGUAGE_RU);
        } else {
            DEFAULT_LOCALE = new Locale(LANGUAGE_EN);
        }
    }

    private final String PROPERTIES_NAME = "Message";

    /**
     * Получить ресурсную строку на указанном языке.
     *
     * @param locale Язык.
     * @param key Ключ ресурсной строки.
     */
    public String getResourceString(Locale locale, String key) {
        return ResourceBundle.getBundle(PROPERTIES_NAME, locale).getString(key).intern();
    }

    /**
     * Получить ресурсную стороку на языке по умолчанию.
     *
     * @param key Ключ ресурсной строки.
     */
    public String getResourceString(String key) {
        return ResourceBundle.getBundle(PROPERTIES_NAME, DEFAULT_LOCALE).getString(key).intern();
    }

    /**
     * Получить ресурсную строка на языке по умолчанию с аргументами.
     *
     * @param key Ключ ресурсной строки.
     * @param args Аргументы, для формирования ресурсной строки.
     */
    public String getResourceString(String key, Object... args) {
        var template = ResourceBundle.getBundle(PROPERTIES_NAME, DEFAULT_LOCALE).getString(key).intern();
        return String.format(template, args);
    }
}
