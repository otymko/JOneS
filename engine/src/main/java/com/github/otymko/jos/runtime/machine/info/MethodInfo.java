/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.compiler.AnnotationDefinition;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * Информация о методе для выполнения
 */
@Value
@RequiredArgsConstructor
public class MethodInfo {
  String name;
  String alias;
  boolean function;
  ParameterInfo[] parameters;
  AnnotationDefinition[] annotations;
  Method method;

  public MethodInfo(String name, String alias, boolean function, ParameterInfo[] parameters,
                    AnnotationDefinition[] annotations) {

    this.name = name;
    this.alias = alias;
    this.function = function;
    this.parameters = parameters;
    this.annotations = annotations;
    this.method = null;

  }

  /**
   * Проверяет, соответствует ли метод переданному имени
   * @param requestedName Имя метода для сравнения
   * @return true - Переданная строка является именем или алиасом метода
   *         false - Переданная строка не является именем или алиасом метода
   */
  public boolean equalsByName(String requestedName) {
    return requestedName != null
            && (requestedName.equalsIgnoreCase(name)
              || requestedName.equalsIgnoreCase(alias));
  }

}
