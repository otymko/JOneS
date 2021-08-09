/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Определение метода
 */
@Value
public class MethodDefinition {
  /**
   * Имя метода, например `ПриСозданииОбъекта`
   */
  String name;
  /**
   * Это функция
   */
  boolean function;
  /**
   * Список параметров метода
   */
  List<ParameterInfo> params = new ArrayList<>();
}
