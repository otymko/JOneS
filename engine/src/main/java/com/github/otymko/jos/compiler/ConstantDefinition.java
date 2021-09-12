/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class ConstantDefinition {
  DataType dataType;
  IValue value;

  public ConstantDefinition(IValue pValue) {
    value = pValue;
    dataType = value.getDataType();
  }

  public String toString() {
    return String.format("%s:%s", value.getDataType(), value.asString());
  }
}
