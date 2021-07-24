package com.github.otymko.jos.runtime;

public class TypeManager {
  private final TypeStorage storage = new TypeStorage();

  public void registerType(String name, Class<? extends IValue> implementingClass) {
    storage.getTypes().put(name, implementingClass);
  }

}
