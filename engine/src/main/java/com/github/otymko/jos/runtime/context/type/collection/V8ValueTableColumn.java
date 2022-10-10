/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.runtime.context.*;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.typedescription.TypeDescription;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Builder;

import java.util.Objects;

@ContextClass(name = "КолонкаТаблицыЗначений", alias = "ValueTableColumn")
@Builder
public class V8ValueTableColumn extends ContextValue {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueTableColumn.class);

    private final V8ValueTable owner;

    private String name;
    private String title;
    private TypeDescription valueType;
    private int width;

    @ContextProperty(name = "Имя", alias = "Name")
    public IValue getName() {
        return ValueFactory.create(name);
    }

    public String getNameInternal() {
        return name;
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public IValue getDefaultValue() {
        return adjustValue(ValueFactory.create());
    }

    public IValue adjustValue(IValue value) {
        final var rawValue = value == null ? null : value.getRawValue();
        if (valueType == null) {
            return rawValue;
        }
        return valueType.adjustValue(rawValue);
    }

    public V8ValueTableColumn copyTo(V8ValueTable newOwner) {
        return new V8ValueTableColumn(newOwner, name, title, valueType, width);
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
