package com.github.otymko.jos.runtime;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TypeStorage {
  @Getter
  private final Map<String, Class<? extends IValue>> types = new HashMap<>();
}
