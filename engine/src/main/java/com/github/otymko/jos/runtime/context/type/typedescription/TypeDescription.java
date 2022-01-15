/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.context.type.primitive.TypeValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Value;

import java.util.List;

@ContextClass(name = "ОписаниеТипов", alias = "TypeDescription")
@Value
public class TypeDescription extends ContextValue {

  public static final ContextInfo INFO = ContextInfo.createByClass(TypeDescription.class);

  List<TypeValue> types;

  @ContextProperty(name = "КвалификаторыЧисла", alias = "NumberQualifiers", accessMode = PropertyAccessMode.READ_ONLY)
  NumberQualifiers numberQualifiers;

  @ContextProperty(name = "КвалификаторыСтроки", alias = "StringQualifiers", accessMode = PropertyAccessMode.READ_ONLY)
  StringQualifiers stringQualifiers;

  @ContextProperty(name = "КвалификаторыДаты", alias = "DateQualifiers", accessMode = PropertyAccessMode.READ_ONLY)
  DateQualifiers dateQualifiers;

  @ContextProperty(name = "КвалификаторыДвоичныхДанных", alias = "BinaryDataQualifiers", accessMode = PropertyAccessMode.READ_ONLY)
  BinaryDataQualifiers binaryDataQualifiers;

  @ContextMethod(name = "Типы", alias = "Types")
  public V8Array types() {
    final var result = new V8Array();
    for (final var type : types) {
      result.add(type);
    }
    return result;
  }

  private boolean containsTypeInternal(TypeValue typeValue) {
    return types.stream().anyMatch(tv -> tv.equals(typeValue));
  }

  @ContextMethod(name = "СодержитТип", alias = "ContainsType")
  public IValue containsType(IValue type) {
    if (type == null) {
      throw MachineException.invalidArgumentValueException();
    }
    final var typeRaw = type.getRawValue();
    if (!(typeRaw instanceof TypeValue)) {
      throw MachineException.invalidArgumentValueException();
    }
    return ValueFactory.create(containsTypeInternal((TypeValue) typeRaw));
  }

  private boolean adjustAsBoolean(IValue value) {
    try {
      return value.asBoolean();
    } catch (Exception e) {
      return false;
    }
  }

  @ContextMethod(name = "ПривестиЗначение", alias = "AdjustValue")
  public IValue adjustValue(IValue value) {

    if (value == null) {
      return adjustValue(ValueFactory.create());
    }

    final var rawValue = value.getRawValue();
    final var contextType = (RuntimeContext) rawValue;
    final var valueType = new TypeValue((contextType.getContextInfo()));

    TypeValue targetType;

    if (containsTypeInternal(valueType)) {
      targetType = valueType;
    } else {
      if (types.isEmpty()) {
        return rawValue;
      }
      if (types.size() != 1) {
        return ValueFactory.create();
      }
      targetType = types.get(0);
    }

    // TODO: Сравнение типов https://github.com/otymko/JOneS/issues/86
    if (NumberValue.INFO.equals(targetType.getValue())) {
      return numberQualifiers.adjustValue(rawValue);
    }

    // TODO: Сравнение типов https://github.com/otymko/JOneS/issues/86
    if (StringValue.INFO.equals(targetType.getValue())) {
      return stringQualifiers.adjustValue(rawValue);
    }

    // TODO: Сравнение типов https://github.com/otymko/JOneS/issues/86
    if (DateValue.INFO.equals(targetType.getValue())) {
      return dateQualifiers.adjustValue(rawValue);
    }

    // TODO: Сравнение типов https://github.com/otymko/JOneS/issues/86
    if (BooleanValue.INFO.equals(targetType.getValue())) {
      return ValueFactory.create(adjustAsBoolean(rawValue));
    }

    if (types.size() == 1
      && types.get(0).equals(targetType)) {
      // Единственный тип, совпадающий с типом параметра
      return rawValue;
    }

    throw MachineException.operationNotImplementedException();
  }

  @ContextConstructor
  public static TypeDescription constructor7(IValue p1,
                                             IValue p2,
                                             IValue p3,
                                             IValue p4,
                                             IValue p5,
                                             IValue p6,
                                             IValue p7) {
    if (p1 == null) {
      throw MachineException.invalidArgumentValueException();
    }
    final var p1raw = p1.getRawValue();
    final var builder = new TypeDescriptionBuilder();
    if (p1raw instanceof TypeDescription) {
      // Описание типов + корректировки

      builder.applyBaseDescription((TypeDescription) p1raw);
      builder.addTypes(p2);
      builder.removeTypes(p3);
      builder.applyNumberQualifiers(p4);
      builder.applyStringQualifiers(p5);
      builder.applyDateQualifiers(p6);
      builder.applyBinaryDataQualifiers(p7);

    } else if (p1raw instanceof V8Array || p1raw.getDataType() == DataType.STRING) {
      // Типы + Квалификаторы

      builder.addTypes(p1raw);
      builder.applyNumberQualifiers(p2);
      builder.applyStringQualifiers(p3);
      builder.applyDateQualifiers(p4);
      builder.applyBinaryDataQualifiers(p5);

    } else {
      throw MachineException.invalidArgumentValueException();
    }

    return builder.build();
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }
}
