package com.github.otymko.jos.runtime.type;

public class TypeManager {
  private final TypeStorage storage = new TypeStorage();

  public void registerType(String name, Class<? extends BaseValue> implementingClass) {
    storage.getTypes().put(name, implementingClass);
  }

}
