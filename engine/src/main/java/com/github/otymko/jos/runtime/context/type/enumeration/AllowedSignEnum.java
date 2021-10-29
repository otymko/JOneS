/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.EnumValue;

@EnumClass(name = "ДопустимыйЗнак", alias = "AllowedSign")
public enum AllowedSignEnum implements EnumType {

  @EnumValue(name = "Любой", alias = "Any")
  ANY,

  @EnumValue(name = "Неотрицательный", alias = "NonNegative")
  NON_NEGATIVE

}
