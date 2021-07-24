package com.github.otymko.jos.runtime.machine.info;

import lombok.Value;

import java.lang.reflect.Method;

/**
 * Информация о методе для выполнения
 */
@Value
public class MethodInfo {
  String name;
  String alias;
  boolean function;
  ParameterInfo[] parameters;
  Method method;
}
