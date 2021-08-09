/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.runtime.context.type.DataType;

import java.util.Date;

public interface IValue {

  float asNumber();

  Date asDate();

  boolean asBoolean();

  String asString();

  IValue getRawValue();

  DataType getDataType();

  int compareTo(IValue o);

}
