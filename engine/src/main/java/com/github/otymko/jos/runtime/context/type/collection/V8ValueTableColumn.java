/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.type.typedescription.TypeDescription;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Builder;
import lombok.Getter;

/**
 * Колонка таблицы значений.
 * Содержит описание одной колонки Таблицы Значений.
 * @see V8ValueTable
 */
@ContextClass(name = "КолонкаТаблицыЗначений", alias = "ValueTableColumn")
@Builder
public class V8ValueTableColumn extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueTableColumn.class);

    private final V8ValueTable owner;

    private final int id;

    @ContextProperty(name = "Имя", alias = "Name")
    @Getter
    private String name;
    @ContextProperty(name = "Заголовок", alias = "Title")
    @Getter
    private String title;
    @ContextProperty(name = "ТипЗначения", alias = "ValueType")
    @Getter
    private TypeDescription valueType;
    @ContextProperty(name = "Ширина", alias = "Width")
    @Getter
    private int width;
    @Getter
    private IValue defaultValue;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public IValue adjustValue(IValue value) {
        final var rawValue = value == null ? null : value.getRawValue();
        if (valueType == null) {
            return rawValue;
        }
        return valueType.adjustValue(rawValue);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
