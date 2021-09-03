/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.runtime.context.PropertyAccessMode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Value
public class PropertyInfo {
  String name;
  String alias;
  PropertyAccessMode accessMode;
  Field field;

  @Accessors(fluent = true)
  boolean hasSetter;
  Method setter;

  @Accessors(fluent = true)
  boolean hasGetter;
  Method getter;
}
