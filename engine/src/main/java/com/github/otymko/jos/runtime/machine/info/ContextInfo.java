/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextInitializer;
import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumValue;
import com.github.otymko.jos.runtime.context.GlobalContextClass;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContextInfo {
  public static final ContextInfo EMPTY = createEmptyInfo();
  /**
   * Имя тип на русском
   */
  String name;
  /**
   * Имя типа на английском
   */
  String alias;
  /**
   * Класс контекста
   */
  Class<? extends RuntimeContext> typeClass;
  /**
   * Список методов
   */
  MethodInfo[] methods;
  /**
   * Список свойств
   */
  PropertyInfo[] properties;
  /**
   * Список конструкторов
   */
  ConstructorInfo[] constructors;

  public static ContextInfo createByClass(Class<? extends RuntimeContext> typeClass) {
    if (typeClass.getAnnotation(GlobalContextClass.class) != null) {
      return createByGlobalContextClass(typeClass);
    } else {
      return createByContextClass(typeClass);
    }
  }

  public static ContextInfo createByEnumValue(EnumValue enumValue) {
    var builder = ContextInfo.builder();
    builder.name(enumValue.name());
    builder.alias(enumValue.alias());
    builder.typeClass(null);
    builder.methods(new MethodInfo[0]);
    builder.properties(new PropertyInfo[0]);
    builder.constructors(new ConstructorInfo[0]);
    return builder.build();
  }

  public static ContextInfo createByEnumClass(EnumClass enumValue) {
    var builder = ContextInfo.builder();
    builder.name(enumValue.name());
    builder.alias(enumValue.alias());
    builder.typeClass(null);
    builder.methods(new MethodInfo[0]);
    builder.properties(new PropertyInfo[0]);
    builder.constructors(new ConstructorInfo[0]);
    return builder.build();
  }

  private static ContextInfo createByContextClass(Class<? extends RuntimeContext> typeClass) {
    var contextType = typeClass.getAnnotation(ContextClass.class);
    var methods = ContextInitializer.getContextMethods(typeClass);
    var constructors = ContextInitializer.getConstructors(typeClass);
    var properties = ContextInitializer.getProperties(typeClass);

    var builder = ContextInfo.builder();
    builder.name(contextType.name());
    builder.alias(contextType.alias());
    builder.typeClass(typeClass);
    builder.methods(methods);
    builder.properties(properties);
    builder.constructors(constructors);

    return builder.build();
  }

  private static ContextInfo createByGlobalContextClass(Class<? extends RuntimeContext> typeClass) {
    var methods = ContextInitializer.getContextMethods(typeClass);

    var builder = ContextInfo.builder();
    builder.name(typeClass.getSimpleName());
    builder.alias(typeClass.getSimpleName());
    builder.typeClass(typeClass);
    builder.methods(methods);
    builder.properties(new PropertyInfo[0]);
    builder.constructors(new ConstructorInfo[0]);

    return builder.build();
  }

  private static ContextInfo createEmptyInfo() {
    var builder = ContextInfo.builder();
    builder.name("");
    builder.alias("");
    builder.typeClass(null); // ????
    builder.methods(new MethodInfo[0]);
    builder.properties(new PropertyInfo[0]);
    builder.constructors(new ConstructorInfo[0]);

    return builder.build();
  }

}
