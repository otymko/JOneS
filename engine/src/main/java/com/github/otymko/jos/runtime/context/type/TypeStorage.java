package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.ContextType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TypeStorage {
  @Getter
  private final Map<String, Class<? extends ContextType>> types = new HashMap<>();
}
