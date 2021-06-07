package com.github.otymko.jos.context.type;

import com.github.otymko.jos.context.value.Value;

public class TypeManager {
  private final TypeStorage storage = new TypeStorage();

  public void registerType(String name, Class<? extends Value> implementingClass) {
    storage.getTypes().put(name, implementingClass);
  }

}
