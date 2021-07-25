package com.github.otymko.jos.runtime.machine.info;

import lombok.Value;

import java.lang.reflect.Method;

@Value
public class ConstructorInfo {
  ParameterInfo[] parameters;
  Method method;
}
