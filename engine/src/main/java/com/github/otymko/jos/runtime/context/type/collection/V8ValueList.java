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
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация типа коллекции "СписокЗначений"
 */
@ContextClass(name = "СписокЗначений", alias = "ValueList")
public class V8ValueList extends ContextValue implements IndexAccessor, CollectionIterable {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8ValueList.class);
    private final List<IValue> values;

    @ContextConstructor
    public static V8ValueList constructor() {
        return new V8ValueList();
    }

    public V8ValueList() {
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
    public V8ValueListItem add(IValue value, String presentation, BooleanValue check, IValue picture) {
        V8ValueListItem newItem = new V8ValueListItem(this, value, presentation, check, picture);
        values.add(newItem);
//        addRowToIndexes(newRow);

        return newItem;
    }

    @ContextMethod(name = "Вставить", alias = "Insert")
    public V8ValueListItem insert(int index, IValue value, String presentation, BooleanValue check, IValue picture) {
        if (index < 0) {
            throw MachineException.indexValueOutOfRangeException();
        }
        V8ValueListItem newItem = new V8ValueListItem(this, value, presentation, check, picture);
        values.add(index, newItem);
//        addRowToIndexes(newRow);

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

//        removeRowFromIndexes((V8ValueTableRow) values.get(index));
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

    @ContextMethod(name = "ЗаполнитьПометки", alias = "FillChecks")
    public void fillChecks(BooleanValue check) {

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
            final var newItem = (V8ValueListItem)result.add(
                    castedItem.getValue(),
                    castedItem.getPresentation(),
                    castedItem.getCheck(),
                    castedItem.getPicture()
            );
        }

        return result;
    }

    @ContextMethod(name = "Сдвинуть", alias = "Move")
    public void move(IValue item, IValue offset) {
        final var intOffset = offset.getRawValue().asNumber().intValue();
        final var sourceIndex = indexByValue(item);
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
            try {
                final var itemIndex = Integer.parseInt(castedItem.asString());
                if (itemIndex >= 0 && itemIndex < values.size()) {
                    return itemIndex;
                }
            }catch (NumberFormatException ex) {
                throw MachineException.convertToNumberException();
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

}
