/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.typedescription.TypeDescription;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Коллекция колонок таблицы значений.
 * Для объекта доступен обход коллекции посредством оператора Для Каждого.
 * Возможно обращение к элементу коллекции посредством оператора [].
 * В качестве индекса передается индекс элемента или имя колонки.
 *
 * @see V8ValueTableColumn
 * @see V8ValueTable
 */
@ContextClass(name = "КоллекцияКолонокТаблицыЗначений", alias="ValueTableColumnCollection")
public class V8ValueTableColumnCollection extends ContextValue implements IndexAccessor, PropertyNameAccessor,
        CollectionIterable<V8ValueTableColumn> {

    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueTableColumnCollection.class);

    private final List<IValue> columns;
    private final V8ValueTable owner;

    public V8ValueTableColumnCollection(V8ValueTable owner) {
        this.owner = owner;
        columns = new ArrayList<>();
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
            if (castedColumn.getName().equalsIgnoreCase(columnName)) {
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

    V8ValueTableColumn createColumn(String name, IValue typeDescription, IValue title, IValue width) {

        if (name.isEmpty()) {
            throw MachineException.invalidArgumentValueException();
        }

        final var columnBuilder = new V8ValueTableColumn.V8ValueTableColumnBuilder();
        columnBuilder.owner(owner);
        columnBuilder.name(name);

        if (typeDescription instanceof TypeDescription) {
            var casted = (TypeDescription)typeDescription;
            columnBuilder.valueType(casted);
            columnBuilder.defaultValue(casted.adjustValue(null));
        } else {
            columnBuilder.defaultValue(ValueFactory.create());
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
    public V8ValueTableColumn add(String name, IValue typeDescription, IValue title, IValue width) {
        if (hasColumn(name)) {
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
    public V8ValueTableColumn findColumn(String columnName) {
        return findColumnByNameInternal(columnName);
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public int count() {
        return columns.size();
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
        throw MachineException.getPropertyIsNotWritableException("");
    }

    public boolean hasColumn(String columnName) {
        for (final var column: columns) {
            final var castedColumn = (V8ValueTableColumn)column;
            if (castedColumn.getName().equalsIgnoreCase(columnName)) {
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
