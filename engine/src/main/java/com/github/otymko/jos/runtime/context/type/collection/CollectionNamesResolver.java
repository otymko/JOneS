/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.IValue;

import java.util.List;

/**
 * Общий интерфейс для коллекций колонок
 */
public interface CollectionNamesResolver {

    /**
     * Возвращает разобранный список колонок
     * @param names - имена колонок через запятую
     * @return список объектов колонок
     *
     * @see V8ValueTableColumnCollection
     */
    List<IValue> parseNames(String names);

    /**
     * Определяет имя колонки
     * @param field - Колонка
     * @return имя колонки
     */
    String getName(IValue field);
}
