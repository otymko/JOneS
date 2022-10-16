/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.typedescription.TypeDescription;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

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

    @ContextProperty(name = "Имя", alias = "Name")
    @Getter
    private String name;
    @ContextProperty(name = "Заголовок", alias = "Title")
    private String title;
    @ContextProperty(name = "ТипЗначения", alias = "ValueType")
    private TypeDescription valueType;
    @ContextProperty(name = "Ширина", alias = "Width")
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

    public V8ValueTableColumn copyTo(V8ValueTable newOwner) {
        var newBuilder = new V8ValueTableColumnBuilder();
        return newBuilder.owner(newOwner)
                .name(name)
                .title(title)
                .valueType(valueType)
                .width(width)
                .defaultValue(defaultValue)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof V8ValueTableColumn) {
            final var castedObject = (V8ValueTableColumn)obj;
            return castedObject.name.equalsIgnoreCase(name)
                    && castedObject.owner.equals(owner);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name);
    }
}
