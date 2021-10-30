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
 * Части даты
 */
@EnumClass(name = "ЧастиДаты", alias = "DateFractions")
public enum DateFractionsEnum implements EnumType {

  /**
   * Дата
   */
  @EnumValue(name = "Дата", alias = "Date")
  DATE,

  /**
   * Дата и время
   */
  @EnumValue(name = "ДатаВремя", alias = "DateTime")
  DATE_TIME,

  /**
   * Время
   */
  @EnumValue(name = "Время", alias = "DatTimeTime")
  TIME
}
