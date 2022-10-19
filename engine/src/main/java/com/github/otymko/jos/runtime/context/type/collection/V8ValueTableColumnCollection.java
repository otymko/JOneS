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
        CollectionIterable<V8ValueTableColumn>, CollectionNamesResolver {

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

    V8ValueTableColumn createColumn(String name, IValue typeDescription, String title, Integer width) {

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
            columnBuilder.title(title);
        }

        if (width != null) {
            columnBuilder.width(width);
        }

        return columnBuilder.build();
    }

    @ContextMethod(name = "Добавить", alias = "Add")
    public V8ValueTableColumn add(String name, IValue typeDescription, String title, Integer width) {
        if (hasColumn(name)) {
            throw MachineException.invalidArgumentValueException();
        }
        final var column = createColumn(name, typeDescription, title, width);
        columns.add(column);

        return column;
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public V8ValueTableColumn insert(int index, String name, IValue typeDescription, String title, Integer width) {
        if (hasColumn(name)) {
            throw MachineException.invalidArgumentValueException();
        }
        final var column = createColumn(name, typeDescription, title, width);
        columns.add(index, column);

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

    @ContextMethod(name = "Получить", alias = "Get")
    public IValue get(int index) {
        return columns.get(index);
    }

    @ContextMethod(name = "Очистить", alias = "Clear")
    public void clear() {
        for (var column: columns) {
            owner.columnRemoved((V8ValueTableColumn) column);
        }
        columns.clear();
    }

    @ContextMethod(name = "Индекс", alias = "IndexOf")
    public int indexOf(V8ValueTableColumn column) {
        return columns.indexOf(column);
    }

    @ContextMethod(name = "Сдвинуть", alias = "Move")
    public void move(IValue row, int offset) {
        final var sourceIndex = indexByValue(row);
        final var newIndex = evalIndex(sourceIndex, offset);

        final var tmp = columns.get(sourceIndex);
        if (sourceIndex < newIndex) {
            columns.add(newIndex + 1, tmp);
            columns.remove(sourceIndex);
        } else {
            columns.remove(sourceIndex);
            columns.add(newIndex, tmp);
        }
    }

    private int indexByValue(IValue param) {
        final var index = param.getRawValue();
        if (index instanceof V8ValueTableColumn) {
            final var castedColumn = (V8ValueTableColumn) index;
            int columnIndex = columns.indexOf(castedColumn);
            if (columnIndex == -1) {
                throw MachineException.invalidArgumentValueException();
            }
            return columnIndex;
        }
        final var intIndex = index.asNumber().intValueExact();
        if (intIndex < 0 || intIndex >= columns.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        return intIndex;
    }

    private int evalIndex(int sourceIndex, int offset) {
        var destIndex = sourceIndex + offset;
        if (destIndex < 0 || destIndex >= columns.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        return destIndex;
    }
    private List<V8ValueTableColumn> parseColumnListParameter(String columnList) {
        final var result = new ArrayList<V8ValueTableColumn>();
        if (columnList == null) {
            return result;
        }

        final var columnNames = columnList.split(",");
        for (final var columnName : columnNames) {

            if (!columnName.isBlank()) {
                final var column = findColumnByNameInternal(columnName.trim());
                if (column == null) {
                    throw MachineException.invalidArgumentValueException();
                }
                result.add(column);
            }
        }

        return result;
    }

    List<V8ValueTableColumn> parseColumnList(String columnList, boolean emptyIfNotDefined) {
        final var result = parseColumnListParameter(columnList);
        if (result.isEmpty() && !emptyIfNotDefined) {
            for (IValue iValue : iterator()) {
                result.add((V8ValueTableColumn) iValue);
            }
        }
        return result;
    }

    @Override
    public List<IValue> parseNames(String names) {
        return new ArrayList<IValue>(parseColumnList(names, false));
    }

    @Override
    public String getName(IValue field) {
        return getColumnInternal(field).getName();
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
