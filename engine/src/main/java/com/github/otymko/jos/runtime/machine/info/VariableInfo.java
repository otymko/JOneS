/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import lombok.Value;

/**
 * Информация о переменной для исполнения
 */
@Value
public class VariableInfo {
  String name;
  // тип переменной: переменная / свойство типа
}
