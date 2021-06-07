package com.github.otymko.jos.context.type;

import com.github.otymko.jos.context.value.Value;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TypeStorage {
  @Getter
  private final Map<String, Class<? extends Value>> types = new HashMap<>();
}
