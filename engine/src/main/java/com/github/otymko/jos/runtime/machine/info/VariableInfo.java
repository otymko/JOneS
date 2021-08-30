/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.runtime.SymbolType;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Информация о переменной для исполнения
 */
@Value
@RequiredArgsConstructor
public class VariableInfo {
  String name;
  String alias;
  SymbolType type;
  // тип переменной: переменная / свойство типа

  public VariableInfo(String name) {
    this.name = name;
    this.alias = "";
    this.type = SymbolType.VARIABLE;
  }

}
