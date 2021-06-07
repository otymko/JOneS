package com.github.otymko.jos.compiler;

import com.github.otymko.jos.vm.info.ParameterInfo;
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
