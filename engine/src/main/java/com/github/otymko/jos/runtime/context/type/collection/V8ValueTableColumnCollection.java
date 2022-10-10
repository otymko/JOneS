/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.*;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.typedescription.TypeDescription;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;

@ContextClass(name = "КоллекцияКолонокТаблицыЗначений", alias="ValueTableColumnCollection")
public class V8ValueTableColumnCollection extends ContextValue implements IndexAccessor, PropertyNameAccessor, CollectionIterable<V8ValueTableColumn> {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueTableColumnCollection.class);

    private final List<IValue> columns;
    private final V8ValueTable owner;

    public V8ValueTableColumnCollection(V8ValueTable owner) {
        this.owner = owner;
        columns = new ArrayList<>();
    }

    private void setInternal(IValue index, IValue newValue) {

    }

    V8ValueTableColumn findColumnInternal(IValue index) {
        final var rawIndex = index.getRawValue();
        if (rawIndex.getDataType() == DataType.STRING) {
            return findColumnByNameInternal(rawIndex.asString());
        }
        if (rawIndex.getDataType() == DataType.NUMBER) {
            final var numericIndex = rawIndex.asNumber().intValue();
            return (V8ValueTableColumn)columns.get(numericIndex);

        }
        if (rawIndex instanceof V8ValueTableColumn) {
            if (columns.contains(rawIndex)) {
                return (V8ValueTableColumn)rawIndex;
            }
        }
        throw MachineException.invalidArgumentValueException();
    }

    V8ValueTableColumn findColumnByNameInternal(String columnName) {
        for (final var column: columns) {
            final var castedColumn = (V8ValueTableColumn)column;
            if (castedColumn.getNameInternal().equalsIgnoreCase(columnName)) {
                return castedColumn;
            }
        }
        return null;
    }

    V8ValueTableColumn getColumnInternal(IValue index) {
        final var column = findColumnInternal(index);
        if (column == null) {
            throw MachineException.invalidArgumentValueException();
        }
        return column;
    }

    V8ValueTableColumn createColumn(IValue name, IValue typeDescription, IValue title, IValue width) {

        if (name.asString().isEmpty()) {
            throw MachineException.invalidArgumentValueException();
        }

        final var columnBuilder = new V8ValueTableColumn.V8ValueTableColumnBuilder();
        columnBuilder.owner(owner);
        columnBuilder.name(name.asString());

        if (typeDescription instanceof TypeDescription) {
            columnBuilder.valueType((TypeDescription)typeDescription);
        }

        if (title != null) {
            columnBuilder.title(title.asString());
        }

        if (width != null) {
            columnBuilder.width(width.asNumber().intValue());
        }

        return columnBuilder.build();
    }

    @ContextMethod(name = "Добавить", alias = "Add")
    public IValue add(IValue name, IValue typeDescription, IValue title, IValue width) {
        if (hasColumn(name.asString())) {
            throw MachineException.invalidArgumentValueException();
        }
        final var column = createColumn(name, typeDescription, title, width);
        columns.add(column);
        return column;
    }

    V8ValueTableColumn copy(V8ValueTableColumn column) {
        final var newColumn = column.copyTo(owner);
        columns.add(newColumn);
        return newColumn;
    }


    @ContextMethod(name = "Удалить", alias = "Delete")
    public void delete(IValue index) {
        final var column = getColumnInternal(index);
        columns.remove(column);
        owner.columnRemoved(column);
    }

    @ContextMethod(name = "Найти", alias = "Find")
    public IValue findColumn(IValue columnName) {
        final var column = findColumnByNameInternal(columnName.asString());
        if (column == null) {
            return ValueFactory.create();
        }
        return column;
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public IValue count() {
        return ValueFactory.create(columns.size());
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(columns.iterator());
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return getColumnInternal(index);
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        setInternal(index, value);
    }

    public boolean hasColumn(String columnName) {
        for (final var column: columns) {
            final var castedColumn = (V8ValueTableColumn)column;
            if (castedColumn.getNameInternal().equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IValue getPropertyValue(IValue index) {
        return getColumnInternal(index);
    }

    @Override
    public void setPropertyValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException(index.asString());
    }

    @Override
    public boolean hasProperty(IValue index) {
        final var column = findColumnInternal(index);
        return (column != null);
    }
}
