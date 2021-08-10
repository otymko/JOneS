/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.module;

import com.github.otymko.jos.compiler.ConstantDefinition;
import com.github.otymko.jos.compiler.MethodDescriptor;
import com.github.otymko.jos.compiler.SymbolAddress;
import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.util.List;

/**
 * Скомпилированный модуль скрипта
 */
@Data
@Builder
public class ModuleImage {
  /**
   * Источник модуля
   */
  private ModuleSource source;

  /**
   * Байткоды
   */
  private List<Command> code;
  /**
   * Методы
   */
  private List<MethodDescriptor> methods;
  /**
   * Переменные
   */
  private List<VariableInfo> variables;
  /**
   * Константы
   */
  private List<ConstantDefinition> constants;

  /**
   * Карта ссылок методы
   */
  private List<SymbolAddress> methodRefs;

  /**
   * Карта ссылок на переменные
   */
  private List<SymbolAddress> variableRefs;

  /**
   * Точка входа
   */
  private int entry;

}
