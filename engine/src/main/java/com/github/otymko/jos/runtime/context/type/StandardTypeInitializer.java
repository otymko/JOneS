/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.context.type.collection.ArrayImpl;
import com.github.otymko.jos.runtime.context.type.collection.StructureImpl;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.context.type.primitive.TypeValue;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StandardTypeInitializer {

  public void initialize(TypeManager typeManager) {
    initPrimitives(typeManager);
    initCollections(typeManager);
  }

  private void initPrimitives(TypeManager typeManager) {
    for (var type : DataType.values()) {

      if (type == DataType.UNDEFINED) {
        implementTypeByInfo(typeManager, UndefinedValue.INFO, UndefinedValue.class);
      } else if (type == DataType.GENERIC_VALUE) {
        implementTypeByInfo(typeManager, NullValue.INFO, NullValue.class);
      } else if (type == DataType.BOOLEAN) {
        implementTypeByInfo(typeManager, BooleanValue.INFO, BooleanValue.class);
      } else if (type == DataType.STRING) {
        implementTypeByInfo(typeManager, StringValue.INFO, StringValue.class);
      } else if (type == DataType.NUMBER) {
        implementTypeByInfo(typeManager, NumberValue.INFO, NumberValue.class);
      } else if (type == DataType.DATE) {
        implementTypeByInfo(typeManager, DateValue.INFO, DateValue.class);
      } else if (type == DataType.TYPE) {
        implementTypeByInfo(typeManager, TypeValue.INFO, TypeValue.class);
      }
      // object
      // + алиас?
    }
  }

  private void implementTypeByInfo(TypeManager typeManager, ContextInfo info,
                                   Class<? extends ContextType> implementClass) {
    typeManager.registerType(info.getName(), implementClass);
    typeManager.registerType(info.getAlias(), implementClass);
  }

  private void initCollections(TypeManager typeManager) {
    typeManager.registerType("Массив", ArrayImpl.class);
    typeManager.registerType("Array", ArrayImpl.class);

    typeManager.registerType("Структура", StructureImpl.class);
    typeManager.registerType("Structure", StructureImpl.class);
  }

}
