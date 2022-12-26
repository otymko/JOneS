/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.CollectionIterable;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.IteratorValue;
import com.github.otymko.jos.runtime.context.type.common.V8CompareValues;
import com.github.otymko.jos.runtime.context.type.enumeration.SortDirection;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация типа коллекции "СписокЗначений"
 */
@ContextClass(name = "СписокЗначений", alias = "ValueList")
public class V8ValueList extends ContextValue implements IndexAccessor, CollectionIterable {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueList.class);

    private final static V8CompareValues compareValues = V8CompareValues.create(true);
    private final List<IValue> values;

    @ContextConstructor
    public static V8ValueList constructor() {
        return new V8ValueList();
    }

    V8ValueList() {
        values = new ArrayList<>();
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
    public V8ValueListItem add(IValue value, String presentation, Boolean check, IValue picture) {
        V8ValueListItem newItem = new V8ValueListItem(this, value, presentation, check, picture);
        values.add(newItem);

        return newItem;
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public V8ValueListItem insert(int index, IValue value, String presentation, Boolean check, IValue picture) {
        if (index < 0) {
            throw MachineException.indexValueOutOfRangeException();
        }
        V8ValueListItem newItem = new V8ValueListItem(this, value, presentation, check, picture);
        values.add(index, newItem);

        return newItem;
    }

    @ContextMethod(name = "Получить", alias = "Get")
    public IValue get(int index) {
        return values.get(index);
    }

    @ContextMethod(name = "Удалить", alias = "Delete")
    public void remove(IValue item) {
        final var index = indexOfItem(item);
        if (index == -1) {
            throw MachineException.invalidArgumentValueException();
        }

        values.remove(index);
    }

    @ContextMethod(name = "Индекс", alias = "Index")
    public int index(V8ValueListItem item) {
        return indexOfItem(item);
    }

    @ContextMethod(name = "ЗагрузитьЗначения", alias = "LoadValues")
    public void loadValues(V8Array newValues) {
        final var newValuesIterator = newValues.iterator().iterator();

        values.clear();

        while (newValuesIterator.hasNext() ) {
            final var value = newValuesIterator.next();
            V8ValueListItem newItem = new V8ValueListItem(this, value);
            values.add(newItem);
        }
    }

    @ContextMethod(name = "ВыгрузитьЗначения", alias = "UnloadValues")
    public V8Array unloadValues() {
        final var result = V8Array.create();

        for (final var item: values) {
            final var castedItem = (V8ValueListItem) item;
            result.add(castedItem.getValue());
        }

        return result;
    }

    @ContextMethod(name = "ЗаполнитьПометки", alias = "FillChecks")
    public void fillChecks(boolean check) {

        for (IValue value : values) {
            final var castedValue = (V8ValueListItem) value.getRawValue();
            castedValue.setCheck(check);
        }
    }

    @ContextMethod(name = "Скопировать", alias = "Copy")
    public V8ValueList copy() {
        final var result = new V8ValueList();

        for(final var value: values) {
            final var castedItem = (V8ValueListItem)value.getRawValue();
            result.add(
                    castedItem.getValue(),
                    castedItem.getPresentation(),
                    castedItem.isCheck(),
                    castedItem.getPicture()
            );
        }

        return result;
    }

    @ContextMethod(name = "Сдвинуть", alias = "Move")
    public void move(IValue item, int offset) {
        final var sourceIndex = indexByValue(item);
        final var newIndex = evalIndex(sourceIndex, offset);

        final var tmp = values.get(sourceIndex);
        if (sourceIndex < newIndex) {
            values.add(newIndex + 1, tmp);
            values.remove(sourceIndex);
        } else {
            values.remove(sourceIndex);
            values.add(newIndex, tmp);
        }
    }

    @ContextMethod(name = "НайтиПоЗначению", alias = "FindByValue")
    public V8ValueListItem findByValue(IValue inValue) {
        var index = 0;
        while (index < values.size()) {
            var item = values.get(index);
            var castedItem = (V8ValueListItem) item;
            var value =  castedItem.getValue();
            if (value.equals(inValue)) {
                return castedItem;
            }
            index++;
        }

        return null;
    }

    @ContextMethod(name = "СортироватьПоЗначению", alias = "SortByValue")
    public void sortByValue(SortDirection direction) {
        sortByProperty(values, direction, "value");
    }

    @ContextMethod(name = "СортироватьПоПредставлению", alias = "SortByPresentation")
    public void sortByPresentation(SortDirection direction) {
        sortByProperty(values, direction, "presentation");
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
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
        throw MachineException.indexedValueIsReadOnly();
    }

    private int indexByValue(IValue param) {
        final var index = param.getRawValue();

        if (index instanceof V8ValueListItem) {
            final var castedItem = (V8ValueListItem) index;
            if (castedItem.getOwner() != this) {
                throw MachineException.invalidArgumentValueException();
            }
            return values.indexOf(castedItem);
        }

        final var intIndex = index.asNumber().intValueExact();
        if (intIndex < 0 || intIndex >= values.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        return intIndex;
    }

    private int indexOfItem(IValue item) {
        if (item == null) {
            throw MachineException.invalidArgumentValueException();
        }

        final var castedItem = item.getRawValue();

        if (castedItem.getDataType() == DataType.NUMBER) {
            final var itemIndex = castedItem.asNumber().intValue();
            if (itemIndex >= 0 && itemIndex < values.size()) {
                return itemIndex;
            }
            throw MachineException.invalidArgumentValueException();
        }

        if (castedItem.getDataType() == DataType.STRING) {
            final var itemIndex = castedItem.asNumber().intValue();
            if (itemIndex >= 0 && itemIndex < values.size()) {
                return itemIndex;
            }
            throw MachineException.invalidArgumentValueException();
        }

        if (castedItem instanceof V8ValueListItem) {
            final var itemIndex = values.indexOf(item.getRawValue());
            if (itemIndex >= 0 && itemIndex < values.size()) {
                return itemIndex;
            }
            return -1;
        }
        throw MachineException.invalidArgumentValueException();
    }

    private int evalIndex(int sourceIndex, int offset) {
        var destIndex = sourceIndex + offset;
        if (destIndex < 0 || destIndex >= values.size()) {
            throw MachineException.indexValueOutOfRangeException();
        }
        return destIndex;
    }

    private void sortByProperty(List<IValue> values, SortDirection direction, String property) {

        if (direction == null) {
            direction = SortDirection.ASC;
        }

        SortDirection finalDirection = direction;

        values.sort((value1, value2) -> {
            final var castedValue1 = (V8ValueListItem) value1.getRawValue();
            final var castedValue2 = (V8ValueListItem) value2.getRawValue();

            final var propertyValue1 = castedValue1.getPropertyValue(
                    castedValue1.findProperty(property)
            );

            final var propertyValue2 = castedValue2.getPropertyValue(
                    castedValue2.findProperty(property)
            );

            int result = compareValues.compare(propertyValue1, propertyValue2);

            return result * finalDirection.getOrder();
        });
    }
}
