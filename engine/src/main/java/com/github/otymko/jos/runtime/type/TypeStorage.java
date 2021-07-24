package com.github.otymko.jos.runtime.type;

import com.github.otymko.jos.runtime.type.BaseValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TypeStorage {
  @Getter
  private final Map<String, Class<? extends BaseValue>> types = new HashMap<>();
}
