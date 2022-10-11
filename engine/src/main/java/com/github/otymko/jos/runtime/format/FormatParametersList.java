/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.format;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Вспомогательный класс, представляющий параметры форматирования для функции Формат.
 *
 * @see ValueFormatter
 */
final class FormatParametersList {

    private static final Pattern intListSeparatorPattern = Pattern.compile("\\D");

    private final Map<String, String> paramList;

    /**
     * Создает экземпляр класса по соответствию
     *
     * @param paramList - Соответствие параметров форматирования
     */
    public FormatParametersList(Map<String, String> paramList) {
        this.paramList = paramList;
    }

    /**
     * Получает значение параметра по одному из его имен.
     *
     * @param names Список возможных имен параметра
     * @return Возвращает {@code Optional}, который пуст, если ни по одному имени не нашлось значения,
     * и заполненное строковое значение по одному из найденных имен
     */
    public Optional<String> get(List<String> names) {
        return names.stream()
                .map(name -> name.toUpperCase(Locale.ENGLISH))
                .map(paramList::get)
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Возвращает локаль форматирования в соответствии с именами параметров
     *
     * @param names Список возможных имен параметра
     * @return Возвращает {@code Locale}, определенное по значению параметра по одному из имен, или {@code Locale.getDefault()}
     */
    public Locale getLocale(List<String> names) {
        return get(names)
                .map(FormatParametersList::getLocale)
                .orElse(Locale.getDefault());
    }

    private static Locale getLocale(String localeParamValue) {
        return Locale.forLanguageTag(localeParamValue.replace('_', '-'));
    }

    /**
     * Возвращает значение параметра, приведенное к целому. Если значение не может быть приведено к целому, возвращает 0.
     *
     * @param names Список возможных имен параметра
     * @return Значение параметра, приведенное к {@code Integer} или 0, если приведение невозможно
     */
    public Optional<Integer> getInt(List<String> names) {
        return get(names)
                .map(FormatParametersList::parseInt);
    }

    /**
     * Проверяет наличие параметра в списке
     *
     * @param names Список возможных имен параметра
     * @return @{code true}, если параметр был установлен, и @{code false} в противном случае
     */
    public boolean containsKey(List<String> names) {
        return names.stream()
                .map(name -> name.toUpperCase(Locale.ENGLISH))
                .anyMatch(paramList::containsKey);
    }

    /**
     * Получает значение параметра, преобразованное в список целых значений.
     *
     * @param names Список возможных имен параметра
     * @return Значение параметра, преобразованное в список целых значений.
     */
    public List<Integer> getIntList(List<String> names) {
        return get(names)
                .stream()
                .flatMap(stringValue -> Arrays.stream(intListSeparatorPattern.split(stringValue)))
                .filter(Predicate.not(String::isBlank))
                .map(FormatParametersList::parseInt)
                .collect(Collectors.toList());
    }

    private static int parseInt(String value) {
        String result = value.chars()
                .filter(ch -> Character.isDigit(ch) || ch == '-')
                .mapToObj(ch -> (char) ch)
                .map(String::valueOf)
                .collect(Collectors.joining());

        return result.isBlank() ? 0 : Integer.parseInt(result);
    }
}
