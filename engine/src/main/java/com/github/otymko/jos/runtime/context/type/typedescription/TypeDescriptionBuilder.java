/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.primitive.TypeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Требует рефакторинга. https://github.com/otymko/JOneS/pull/85#discussion_r741636805
final class TypeDescriptionBuilder {

    final List<TypeValue> types = new ArrayList<>();

    NumberQualifiers numberQualifiers = NumberQualifiers.constructor();

    StringQualifiers stringQualifiers = StringQualifiers.constructor();

    DateQualifiers dateQualifiers = DateQualifiers.constructor();

    BinaryDataQualifiers binaryDataQualifiers = BinaryDataQualifiers.constructor();

    void applyBaseDescription(TypeDescription baseDescription) {
        types.clear();
        addTypes(baseDescription.getTypes());
        numberQualifiers = baseDescription.getNumberQualifiers();
        stringQualifiers = baseDescription.getStringQualifiers();
        dateQualifiers = baseDescription.getDateQualifiers();
        binaryDataQualifiers = baseDescription.getBinaryDataQualifiers();
    }

    List<TypeValue> typesFromArray(V8Array array) {
        final var result = new ArrayList<TypeValue>();
        for (final IValue ivalue : array.iterator()) {
            result.add((TypeValue) ivalue);
        }
        return result;
    }

    List<TypeValue> typesFromString(String typesList) {
        final var splittedTypes = typesList.split(",");
        final var result = new ArrayList<TypeValue>();
        for (final var typeName : splittedTypes) {
            final var context = TypeManager.getInstance().getContextInfoByName(typeName);
            if (context.isEmpty()) {
                throw MachineException.typeNotRegisteredException(typeName);
            }
            result.add(new TypeValue(context.get()));
        }
        return result;
    }

    void addTypes(List<TypeValue> typesToAdd) {
        types.addAll(typesToAdd);
    }

    void addTypes(IValue param) {
        if (param == null) {
            return;
        }
        final var rawParam = param.getRawValue();
        if (rawParam.getDataType() == DataType.UNDEFINED) {
            return;
        }

        if (rawParam instanceof V8Array) {
            addTypes(typesFromArray((V8Array) rawParam));
            return;
        }
        if (rawParam.getDataType() == DataType.STRING) {
            addTypes(typesFromString(rawParam.asString()));
            return;
        }

        throw MachineException.invalidArgumentValueException();
    }


    void removeTypes(IValue param) {
        if (param == null) {
            return;
        }
        final var rawParam = param.getRawValue();
        if (rawParam.getDataType() == DataType.UNDEFINED) {
            return;
        }

        if (rawParam instanceof V8Array) {
            removeTypes(typesFromArray((V8Array) rawParam));
            return;
        }
        if (rawParam.getDataType() == DataType.STRING) {
            removeTypes(typesFromString(rawParam.asString()));
            return;
        }

        throw MachineException.invalidArgumentValueException();
    }

    void removeTypes(List<TypeValue> typesToRemove) {
        types.removeIf(typesToRemove::contains);
    }

    List<TypeValue> getTypes() {
        return types.stream().distinct().collect(Collectors.toList());
    }

    void applyNumberQualifiers(IValue param) {
        if (param == null) {
            return;
        }

        final var rawParam = param.getRawValue();
        if (rawParam.getDataType() == DataType.UNDEFINED) {
            return;
        }

        if (!(rawParam instanceof NumberQualifiers)) {
            throw MachineException.invalidArgumentValueException();
        }
        numberQualifiers = (NumberQualifiers) rawParam;
    }

    void applyStringQualifiers(IValue param) {
        if (param == null) {
            return;
        }

        final var rawParam = param.getRawValue();
        if (rawParam.getDataType() == DataType.UNDEFINED) {
            return;
        }

        if (!(rawParam instanceof StringQualifiers)) {
            throw MachineException.invalidArgumentValueException();
        }
        stringQualifiers = (StringQualifiers) rawParam;
    }

    void applyDateQualifiers(IValue param) {
        if (param == null) {
            return;
        }

        final var rawParam = param.getRawValue();
        if (rawParam.getDataType() == DataType.UNDEFINED) {
            return;
        }

        if (!(rawParam instanceof DateQualifiers)) {
            throw MachineException.invalidArgumentValueException();
        }
        dateQualifiers = (DateQualifiers) rawParam;
    }

    void applyBinaryDataQualifiers(IValue param) {
        if (param == null) {
            return;
        }

        final var rawParam = param.getRawValue();
        if (rawParam.getDataType() == DataType.UNDEFINED) {
            return;
        }

        if (!(rawParam instanceof BinaryDataQualifiers)) {
            throw MachineException.invalidArgumentValueException();
        }
        binaryDataQualifiers = (BinaryDataQualifiers) rawParam;
    }

    TypeDescription build() {
        return new TypeDescription(getTypes(),
                numberQualifiers,
                stringQualifiers,
                dateQualifiers,
                binaryDataQualifiers);
    }

}
