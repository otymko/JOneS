/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.Arithmetic;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.common.V8CompareValues;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Объект для работы с данными в табличном виде.
 * Представляет из себя коллекцию строк с заранее заданной структурой.
 */
@ContextClass(name = "ТаблицаЗначений", alias = "ValueTable")
public class V8ValueTable extends ContextValue implements IndexAccessor, CollectionIterable, IndexSourceCollection {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueTable.class);

    private final List<IValue> values;
    private final V8ValueTableColumnCollection columns;

    private final V8CollectionIndexes indexes;

    @ContextConstructor
    public static V8ValueTable constructor() {
        return new V8ValueTable();
    }

    public V8ValueTable() {
        values = new ArrayList<>();
        columns = new V8ValueTableColumnCollection(this);
        indexes = new V8CollectionIndexes(this);
    }

    @ContextProperty(name = "Колонки", alias = "Columns", accessMode = PropertyAccessMode.READ_ONLY)
    public V8ValueTableColumnCollection getColumns() {
        return columns;
    }

    @ContextProperty(name = "Индексы", alias = "Indexes", accessMode = PropertyAccessMode.READ_ONLY)
    public V8CollectionIndexes getIndexes() {
        return indexes;
    }

    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextMethod(name = "Количество", alias = "Count")
    public int count() {
        return values.size();
    }

    @ContextMethod(name = "Очистить", alias = "Clear")
    public void clear() {
        values.clear();
    }

    @ContextMethod(name = "Добавить", alias = "Add")
    public V8ValueTableRow add() {
        V8ValueTableRow newRow = new V8ValueTableRow(this);
        values.add(newRow);
        addRowToIndexes(newRow);

        return newRow;
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public V8ValueTableRow insert(int index) {
        if (index < 0) {
            throw MachineException.indexValueOutOfRangeException();
        }
        V8ValueTableRow newRow = new V8ValueTableRow(this);
        values.add(index, newRow);
        addRowToIndexes(newRow);

        return newRow;
    }

    @ContextMethod(name = "Получить", alias = "Get")
    public IValue get(int index) {
        return values.get(index);
    }

    @ContextMethod(name = "Удалить", alias = "Delete")
    public void remove(IValue row) {
        final var index = indexOfRow(row);
        if (index == -1) {
            throw MachineException.invalidArgumentValueException();
        }
        removeRowFromIndexes((V8ValueTableRow) values.get(index));
        values.remove(index);
    }

    @ContextMethod(name = "Сортировать", alias = "Sort")
    public void sort(String sortColumns, IValue valueComparer) {
        final var compareValues = getCompareValues(valueComparer);
        final var sorter = V8ValueTableSorter.create(columns, sortColumns, compareValues);
        values.sort(sorter);
    }

    private V8CompareValues getCompareValues(IValue param) {
        var rawParam = ValueFactory.rawValueOrUndefined(param);
        if (rawParam instanceof V8CompareValues) {
            return (V8CompareValues) rawParam;
        }
        return (V8CompareValues) V8CompareValues.create();
    }

    private int indexOfRow(IValue row) {
        if (row == null) {
            throw MachineException.invalidArgumentValueException();
        }

        final var castedRow = row.getRawValue();

        if (castedRow.getDataType() == DataType.NUMBER) {
            final var rowIndex = castedRow.asNumber().intValue();
            if (rowIndex >= 0 && rowIndex < values.size()) {
                return rowIndex;
            }
            throw MachineException.invalidArgumentValueException();
        }

        if (castedRow instanceof V8ValueTableRow) {
            final var rowIndex = values.indexOf(row.getRawValue());
            if (rowIndex >= 0 && rowIndex < values.size()) {
                return rowIndex;
            }
            return -1;
        }
        throw MachineException.invalidArgumentValueException();
    }

    @ContextMethod(name = "Индекс", alias = "Index")
    public int index(V8ValueTableRow row) {
        return indexOfRow(row);
    }

    @ContextMethod(name = "ЗагрузитьКолонку", alias = "LoadColumn")
    public void loadColumn(IValue columnValues, IValue columnIndex) {
        final var vit = ((CollectionIterable)columnValues.getRawValue()).iterator().iterator();
        final var rit = values.iterator();

        while (vit.hasNext() && rit.hasNext()) {
            final var row = (V8ValueTableRow)rit.next();
            final var value = vit.next();
            row.setIndexedValue(columnIndex, value);
        }
    }

    private V8Array unloadColumnInternal(IValue columnIndex) {
        var column = columns.getColumnInternal(columnIndex);
        final var result = V8Array.create();
        for (final var row: values) {
            final var castedRow = (V8ValueTableRow) row;
            final var rowValue = castedRow.getIndexedValueInternal(column);
            result.add(rowValue);
        }
        return result;
    }

    @ContextMethod(name = "ВыгрузитьКолонку", alias = "UnloadColumn")
    public IValue unloadColumn(IValue columnIndex) {
        return unloadColumnInternal(columnIndex);
    }

    @ContextMethod(name = "Найти", alias = "Find")
    public V8ValueTableRow find(IValue searchValue, String columnNames) {
        final var rawValue = searchValue.getRawValue();
        final var searchColumns = columns.parseColumnList(columnNames, false);
        for (final var row : values) {
            final var castedRow = (V8ValueTableRow) row;
            for (final var column: searchColumns) {
                final var value = castedRow.get(column);
                if (rawValue.equals(value)) {
                    return castedRow;
                }
            }
        }

        return null;
    }

    @ContextMethod(name = "ЗаполнитьЗначения", alias = "FillValues")
    public void FillValues(IValue value, String columnNames) {
        final var rawValue = ValueFactory.rawValueOrUndefined(value);
        final var searchColumns = columns.parseColumnList(columnNames, false);
        for (final var row : values) {
            final var castedRow = (V8ValueTableRow) row;
            for (final var column : searchColumns) {
                castedRow.setIndexedValueInternal(column, rawValue);
            }
        }
    }

    @ContextMethod(name = "Сдвинуть", alias = "Move")
    public void move(IValue row, IValue offset) {
        final var intOffset = offset.getRawValue().asNumber().intValue();
        final var sourceIndex = indexByValue(row);
        final var newIndex = evalIndex(sourceIndex, intOffset);

        final var tmp = values.get(sourceIndex);
        if (sourceIndex < newIndex) {
            values.add(newIndex + 1, tmp);
            values.remove(sourceIndex);
        } else {
            values.remove(sourceIndex);
            values.add(newIndex, tmp);
        }
    }

    private int indexByValue(IValue param) {
        final var index = param.getRawValue();
        if (index instanceof V8ValueTableRow) {
            final var castedRow = (V8ValueTableRow) index;
            if (castedRow.owner() != this) {
                throw MachineException.invalidArgumentValueException();
            }
            return values.indexOf(castedRow);
        }
        final var intIndex = index.asNumber().intValueExact();
        if (intIndex < 0 || intIndex >= values.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        return intIndex;
    }

    private int evalIndex(int sourceIndex, int offset) {
        var destIndex = sourceIndex + offset;
        if (destIndex < 0 || destIndex >= values.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        return destIndex;
    }

    @ContextMethod(name = "НайтиСтроки", alias = "FindRows")
    public V8Array findRows(IValue filterStructure) {
        if (!(filterStructure instanceof CollectionIterable)) {
            throw MachineException.invalidArgumentValueException();
        }
        final var filter = (CollectionIterable)filterStructure.getRawValue();
        final var result = V8Array.create();

        final var filterKey = new V8CollectionKey(extractKeyAndValue(filter));
        final var fields = filterKey.getFields();

        var index = indexes.pickIndex(fields);
        var list = index == null ? values : index.getValues(filterKey);

        for (var row: list) {
            final var rowKey = V8CollectionKey.extract(fields, (V8ValueTableRow) row);
            if (rowKey.equals(filterKey)) {
                result.add(row);
            }
        }

        return result;
    }

    private Map<IValue, IValue> extractKeyAndValue(CollectionIterable<IValue> s) {
        final var result = new HashMap<IValue, IValue>();
        for (final var kv: s.iterator()) {
            final var castedKeyAndValue = (V8KeyAndValue) kv;
            final var columnName = castedKeyAndValue.getKey().asString();
            final var column = columns.findColumnByNameInternal(columnName);
            if (column == null) {
                throw MachineException.invalidPropertyNameStructureException(columnName);
            }
            result.put(column, castedKeyAndValue.getValue());
        }
        return result;
    }


    private IteratorValue rowsIterator(IValue rows) {
        if (rows != null) {
            final var castedRows = rows.getRawValue();
            if (castedRows instanceof CollectionIterable) {
                final var asCollection = (CollectionIterable) castedRows;
                return asCollection.iterator();
            }
        }
        return iterator();
    }

    private V8ValueTable copyInternal(IValue rowsToCopy, String columnsToCopy) {

        final var columnsListToCopy = columns.parseColumnList(columnsToCopy, false);
        final Map<V8ValueTableColumn, V8ValueTableColumn> columnMap = new HashMap<>();

        final var result = new V8ValueTable();

        for (final var sourceColumn: columnsListToCopy) {
            final var targetColumn = result.getColumns().copy(sourceColumn);
            columnMap.put(sourceColumn, targetColumn);
        }

        for (final var row: rowsIterator(rowsToCopy)) {
            final var castedRow = (V8ValueTableRow)row.getRawValue();
            final var newRow = (V8ValueTableRow)result.add();
            for (final var sourceColumn: columnsListToCopy) {
                final var targetColumn = columnMap.get(sourceColumn);
                final var value = castedRow.getIndexedValueInternal(sourceColumn);
                newRow.setIndexedValueInternal(targetColumn, value);
            }
        }

        return result;
    }

    @ContextMethod(name = "Скопировать", alias = "Copy")
    public V8ValueTable copy(IValue rowsToCopy, String columnsToCopy) {
        return copyInternal(rowsToCopy, columnsToCopy);
    }

    @ContextMethod(name = "СкопироватьКолонки", alias = "CopyColumns")
    public V8ValueTable copyColumns(String columnsToCopy) {
        return copyInternal(V8Array.create(), columnsToCopy);
    }

    @ContextMethod(name = "Свернуть", alias = "GroupBy")
    public void groupBy(String groupingColumnNames, String totalColumnNames) {
        final var groupingColumns = columns.parseColumnList(groupingColumnNames, true);
        final var totalColumns = columns.parseColumnList(totalColumnNames, true);

        removeDeprecatedColumns(groupingColumns, totalColumns);

        final var index = new V8CollectionIndex(this, asFields(groupingColumns));
        for (final var row: values) {
            index.addElement((V8ValueTableRow) row);
        }

        values.clear();

        for (final var key: index.keys()) {
            final var rows = index.data(key).toArray(new IValue[0]);
            final var totalRow = (V8ValueTableRow) rows[0];
            for (int i = 1; i < rows.length; i++) {
                aggregate(totalRow, (V8ValueTableRow) rows[i], totalColumns);
            }
            values.add(totalRow);
        }
        reindex();
    }

    @ContextMethod(name = "ВыбратьСтроку", alias = "ChooseRow")
    public void chooseRow(IValue title, IValue startRow) {
        throw MachineException.operationNotSupportedException();
    }

    private void removeDeprecatedColumns(List<V8ValueTableColumn> groupingColumns, List<V8ValueTableColumn> totalColumns) {
        final var columnsToRemove = new ArrayList<V8ValueTableColumn>();
        for (final var column : columns.iterator()) {
            final var castedColumn = (V8ValueTableColumn) column;
            if (!groupingColumns.contains(castedColumn)
                    && !totalColumns.contains(castedColumn)) {
                columnsToRemove.add(castedColumn);
            }
        }
        for (final var column: columnsToRemove) {
            columns.remove(column);
        }
    }

    private void aggregate(V8ValueTableRow row, V8ValueTableRow row2, List<V8ValueTableColumn> columns) {
        for (final var c: columns) {
            final var v1 = row.getIndexedValue(c).getRawValue();
            final var v2 = row2.getIndexedValue(c).getRawValue();
            if (v2.getDataType() == DataType.NUMBER) {
                if (v1.getDataType() == DataType.NUMBER) {
                    row.setIndexedValue(c, Arithmetic.add(v1, v2));
                } else {
                    row.setIndexedValue(c, v2);
                }
            }
        }
    }

    void columnRemoved(V8ValueTableColumn column) {
        for (final var index: indexes.iterator()) {
            final var castedIndex = (V8CollectionIndex) index;
            castedIndex.columnRemoved(column);
        }
        for (final var row: values) {
            final var castedRow = (V8ValueTableRow) row;
            castedRow.columnRemoved(column);
        }
    }

    private void reindex() {
        for (final var index: indexes.iterator()) {
            buildIndex((V8CollectionIndex) index);
        }
    }

    private void buildIndex(V8CollectionIndex index) {
        index.clear();
        for (var row : values) {
            index.addElement((V8ValueTableRow) row);
        }
    }

    void addRowToIndexes(V8ValueTableRow row) {
        for (final var index: indexes.iterator()) {
            final var castedIndex = (V8CollectionIndex) index;
            castedIndex.addElement(row);
        }
    }

    void removeRowFromIndexes(V8ValueTableRow row) {
        for (final var index: indexes.iterator()) {
            final var castedIndex = (V8CollectionIndex) index;
            castedIndex.removeElement(row);
        }
    }

    private List<IValue> asFields(List<V8ValueTableColumn> columns) {
        return new ArrayList<>(columns);
    }

    @ContextMethod(name = "Итог", alias = "Total")
    public BigDecimal total(IValue column) {
        boolean hasData = false;
        BigDecimal result = BigDecimal.ZERO;
        for (final var row: values) {
            final var castedRow = (V8ValueTableRow)row;
            final var rowValue = castedRow.getIndexedValue(column);
            if (rowValue.getDataType() == DataType.NUMBER) {
                hasData = true;
                result = result.add(rowValue.asNumber());
            }
        }
        if (hasData) {
            return result;
        }

        return null;
    }

    @Override
    public IteratorValue iterator() {
        return new IteratorValue(values.iterator());
    }

    @Override
    public IValue getIndexedValue(IValue index) {
        return get(index.asNumber().intValueExact());
    }

    @Override
    public void setIndexedValue(IValue index, IValue value) {
        throw MachineException.getPropertyIsNotWritableException("");
    }

    @Override
    public IValue getField(String name) {
        return columns.findColumn(name);
    }

    @Override
    public String getName(IValue field) {
        return columns.getColumnInternal(field).getName();
    }

    @Override
    public void indexAdded(V8CollectionIndex index) {
        buildIndex(index);
    }

}
