/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * Информация о методе для выполнения
 */
@Value
@AllArgsConstructor
public class MethodInfo {
  String name;
  String alias;
  boolean function;
  ParameterInfo[] parameters;
  Method method;

  public MethodInfo(String name, String alias, boolean function, ParameterInfo[] parameters) {
    this.name = name;
    this.alias = alias;
    this.function = function;
    this.parameters = parameters;
    this.method = null;
  }

}
