/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.EnumValue;

/**
 * Допустимая длина данных
 */
@EnumClass(name = "ДопустимаяДлина", alias = "AllowedLength")
public enum AllowedLength implements EnumType {

  /**
   * Переменная длина
   */
  @EnumValue(name = "Переменная", alias = "Variable")
  VARIABLE,

  /**
   * Фиксированная длина
   */
  @EnumValue(name = "Фиксированная", alias = "Fixed")
  FIXED

}
