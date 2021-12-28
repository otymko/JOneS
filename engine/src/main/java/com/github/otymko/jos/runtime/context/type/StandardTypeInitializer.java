/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.collection.V8KeyAndValue;
import com.github.otymko.jos.runtime.context.type.collection.V8Structure;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.context.type.primitive.TypeValue;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import com.github.otymko.jos.runtime.context.type.typedescription.BinaryDataQualifiers;
import com.github.otymko.jos.runtime.context.type.typedescription.DateQualifiers;
import com.github.otymko.jos.runtime.context.type.typedescription.NumberQualifiers;
import com.github.otymko.jos.runtime.context.type.typedescription.StringQualifiers;
import com.github.otymko.jos.runtime.context.type.typedescription.TypeDescription;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StandardTypeInitializer {

  public void initialize(TypeManager typeManager) {
    initPrimitives(typeManager);
    initSystemEnumerations(typeManager);
    initCollections(typeManager);
  }

  private void initPrimitives(TypeManager typeManager) {
    for (var type : DataType.values()) {

      if (type == DataType.UNDEFINED) {
        implementTypeByInfo(typeManager, UndefinedValue.INFO);
      } else if (type == DataType.GENERIC_VALUE) {
        implementTypeByInfo(typeManager, NullValue.INFO);
      } else if (type == DataType.BOOLEAN) {
        implementTypeByInfo(typeManager, BooleanValue.INFO);
      } else if (type == DataType.STRING) {
        implementTypeByInfo(typeManager, StringValue.INFO);
      } else if (type == DataType.NUMBER) {
        implementTypeByInfo(typeManager, NumberValue.INFO);
      } else if (type == DataType.DATE) {
        implementTypeByInfo(typeManager, DateValue.INFO);
      } else if (type == DataType.TYPE) {
        implementTypeByInfo(typeManager, TypeValue.INFO);
      }
      // object
      // + алиас?
    }
  }

  private void implementTypeByInfo(TypeManager typeManager, ContextInfo info) {
    typeManager.registerType(info.getName(), info);
    typeManager.registerType(info.getAlias(), info);
  }

  private void initCollections(TypeManager typeManager) {
    // TODO: name и alias из contextType
    implementTypeByInfo(typeManager, V8Array.INFO);
    implementTypeByInfo(typeManager, V8KeyAndValue.INFO);
    implementTypeByInfo(typeManager, V8Structure.INFO);

    implementTypeByInfo(typeManager, BinaryDataQualifiers.INFO);
    implementTypeByInfo(typeManager, DateQualifiers.INFO);
    implementTypeByInfo(typeManager, NumberQualifiers.INFO);
    implementTypeByInfo(typeManager, StringQualifiers.INFO);
    implementTypeByInfo(typeManager, TypeDescription.INFO);
    implementTypeByInfo(typeManager, V8Regex.INFO);
  }

  private void initSystemEnumerations(TypeManager typeManager) {
    typeManager.getEnumerationContext().forEach(context -> implementTypeByInfo(typeManager, context.getContextInfo()));
  }

}
