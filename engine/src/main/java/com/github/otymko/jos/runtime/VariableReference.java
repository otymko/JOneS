/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.IndexAccessor;
import com.github.otymko.jos.runtime.context.PropertyNameAccessor;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class VariableReference extends ContextValue implements IVariable {
  private String name;
  private ReferenceType referenceType;
  private IVariable referencedValue;
  private RuntimeContext context;
  private int contextPropertyNumber;
  private IValue index;

  public static VariableReference create(IVariable variable, String referenceName) {
    var reference = new VariableReference();
    reference.setReferenceType(ReferenceType.SIMPLE);
    reference.setReferencedValue(variable);
    reference.setName(referenceName);
    return reference;
  }

  public static VariableReference createIndexedPropertyReference(RuntimeContext context, IValue index, String referenceName) {
    var reference = new VariableReference();
    reference.setReferenceType(ReferenceType.INDEXED_PROPERTY);
    reference.setContext(context);
    reference.setIndex(index);
    reference.setName(referenceName);
    return reference;
  }

  public static VariableReference createDynamicPropertyNameReference(RuntimeContext context, IValue index, String referenceName) {
    var reference = new VariableReference();
    reference.setReferenceType(ReferenceType.DYNAMIC_PROPERTY);
    reference.setContext(context);
    reference.setIndex(index);
    reference.setName(referenceName);
    return reference;
  }

  public static VariableReference createContextPropertyReference(RuntimeContext context, int propertyNumber, String referenceName) {
    var reference = new VariableReference();
    reference.setReferenceType(ReferenceType.CONTEXT_PROPERTY);
    reference.setContext(context);
    reference.setContextPropertyNumber(propertyNumber);
    reference.setName(referenceName);
    return reference;
  }

  @Override
  public DataType getDataType() {
    return getValue().getDataType();
  }

  @Override
  public IValue getValue() {
    if (referenceType == ReferenceType.SIMPLE) {
      return referencedValue.getValue();
    } else if (referenceType == ReferenceType.INDEXED_PROPERTY) {
      var accessor = (IndexAccessor) context;
      return accessor.getIndexedValue(index);
    } else if (referenceType == ReferenceType.DYNAMIC_PROPERTY) {
      var accessor = (PropertyNameAccessor) context;
      return accessor.getPropertyValue(index);
    } else if (referenceType == ReferenceType.CONTEXT_PROPERTY) {
      if (context.isPropertyWriteOnly(contextPropertyNumber)) {
        throw MachineException.getPropertyIsNotReadableException("");
      }
      return context.getPropertyValue(contextPropertyNumber);
    } else {
      throw MachineException.operationNotImplementedException();
    }
  }

  @Override
  public void setValue(IValue value) {
    if (referenceType == ReferenceType.SIMPLE) {
      referencedValue.setValue(value);
    } else if (referenceType == ReferenceType.INDEXED_PROPERTY) {
      var accessor = (IndexAccessor) context;
      accessor.setIndexedValue(index, value);
    } else if (referenceType == ReferenceType.DYNAMIC_PROPERTY) {
      var accessor = (PropertyNameAccessor) context;
      accessor.setPropertyValue(index, value);
    } else if (referenceType == ReferenceType.CONTEXT_PROPERTY) {
      if (context.isPropertyReadOnly(contextPropertyNumber)) {
        throw MachineException.getPropertyIsNotWritableException("");
      }
      context.setPropertyValue(contextPropertyNumber, value);
    } else {
      throw MachineException.operationNotImplementedException();
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ContextInfo getContextInfo() {
    if (getValue() instanceof ContextValue) {
      return ((ContextValue) getValue()).getContextInfo();
    }
    throw MachineException.operationNotImplementedException();
  }

  @Override
  public float asNumber() {
    return getValue().asNumber();
  }

  @Override
  public Date asDate() {
    return getValue().asDate();
  }

  @Override
  public boolean asBoolean() {
    return getValue().asBoolean();
  }

  @Override
  public String asString() {
    return getValue().asString();
  }

  @Override
  public IValue getRawValue() {
    return getValue().getRawValue();
  }

  @Override
  public int compareTo(IValue o) {
    return getValue().compareTo(o);
  }

}
