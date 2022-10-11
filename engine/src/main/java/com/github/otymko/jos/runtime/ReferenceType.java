/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

/**
 * Тип ссылки на значение
 */
public enum ReferenceType {
    /**
     * Ссылка на локальную переменную
     */
    SIMPLE,
    /**
     * Ссылка на свойство типа
     */
    CONTEXT_PROPERTY,
    /**
     * Ссылка на динамическое свойство типа
     */
    DYNAMIC_PROPERTY,
    /**
     * Ссылка на свойство по индексу
     */
    INDEXED_PROPERTY
}
