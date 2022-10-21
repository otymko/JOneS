/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.IValue;

/**
 * Общий интерфейс для коллекций, которые можно индексировать
 */
public interface IndexSourceCollection {

    /**
     * Определяет поле по имени
     * @param name - имя колонки
     * @return список объектов колонок
     *
     * @see V8ValueTableColumnCollection
     */
    IValue getField(String name);

    /**
     * Определяет имя колонки
     * @param field - Колонка
     * @return имя колонки
     */
    String getName(IValue field);

    void indexAdded(V8CollectionIndex index);
}
