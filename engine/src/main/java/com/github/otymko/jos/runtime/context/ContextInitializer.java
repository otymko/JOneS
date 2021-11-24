/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.compiler.AnnotationDefinition;
import com.github.otymko.jos.runtime.IVariable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.VariableReference;
import com.github.otymko.jos.runtime.context.global.GlobalContext;
import com.github.otymko.jos.runtime.context.global.StringOperationGlobalContext;
import com.github.otymko.jos.runtime.context.global.SystemGlobalContext;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.machine.MachineInstance;
import com.github.otymko.jos.runtime.machine.info.ConstructorInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import com.github.otymko.jos.runtime.machine.info.PropertyInfo;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ContextInitializer {

  public void initialize(MachineInstance machineInstance) {
    machineInstance.implementContext(new GlobalContext());
    machineInstance.implementContext(new SystemGlobalContext());
    machineInstance.implementContext(new StringOperationGlobalContext());
  }

  public MethodInfo[] getContextMethods(Class<? extends RuntimeContext> targetClass) {
    List<MethodInfo> methods = new ArrayList<>();
    for (var method : targetClass.getMethods()) {
      var contextMethod = method.getAnnotation(ContextMethod.class);
      if (contextMethod == null) {
        continue;
      }
      var parameters = getMethodParameters(method);
      var info = new MethodInfo(contextMethod.name(), contextMethod.alias(),
        method.getReturnType() != void.class, parameters, new AnnotationDefinition[0], method);

      methods.add(info);
    }
    return methods.toArray(new MethodInfo[0]);
  }

  public ParameterInfo[] getMethodParameters(Method method) {
    var length = method.getParameters().length;
    if (length == 0) {
      return new ParameterInfo[0];
    }
    var parameters = new ParameterInfo[length];
    var index = 0;
    for (var parameter : method.getParameters()) {
      var parameterInfo = ParameterInfo.builder()
        .name(parameter.getName())
        .build();

      parameters[index] = parameterInfo;
      index++;
    }
    return parameters;
  }

  public ConstructorInfo[] getConstructors(Class<? extends RuntimeContext> targetClass) {
    List<ConstructorInfo> constructors = new ArrayList<>();
    for (var method : targetClass.getMethods()) {
      var contextMethod = method.getAnnotation(ContextConstructor.class);
      if (contextMethod == null) {
        continue;
      }

      var parameters = getMethodParameters(method);
      var constructor = new ConstructorInfo(parameters, method);
      constructors.add(constructor);
    }


    return constructors.toArray(new ConstructorInfo[0]);
  }

  private PropertyInfo.PropertyInfoBuilder getBuilder(
          Map<String, PropertyInfo.PropertyInfoBuilder> builders,
          ContextProperty contextProperty) {
    final var name = contextProperty.name().toLowerCase();
    PropertyInfo.PropertyInfoBuilder builder;
    if (builders.containsKey(name)) {
      builder = builders.get(name);
    } else {
      builder = PropertyInfo.builder().name(contextProperty.name());
      builders.put(name, builder);
    }
    builder.alias(contextProperty.alias());
    return builder;
  }

  private void fetchPropertiesFromFields(
          Map<String, PropertyInfo.PropertyInfoBuilder> builders,
          Class<? extends RuntimeContext> targetClass) {

    for (var field : targetClass.getFields()) {
      var contextProperty = field.getAnnotation(ContextProperty.class);
      if (contextProperty == null) {
        continue;
      }
      final var builder = getBuilder(builders, contextProperty);
      builder.field(field);

      if (contextProperty.alias() != null) {
        builder.alias(contextProperty.alias());
      }

      final var setter = getMethodByName(targetClass, "set" + field.getName());
      if (setter != null) {
        builder.setter(setter);
        builder.hasSetter(true);
      }

      final var getter = getMethodByName(targetClass, "get" + field.getName());
      if (getter != null) {
        builder.getter(getter);
        builder.hasGetter(true);
      }
    }
  }

  private void fetchPropertiesFromMethods(
          Map<String, PropertyInfo.PropertyInfoBuilder> builders,
          Class<? extends RuntimeContext> targetClass) {
    for (var method : targetClass.getMethods()) {
      var contextProperty = method.getAnnotation(ContextProperty.class);
      if (contextProperty == null) {
        continue;
      }

      final var builder = getBuilder(builders, contextProperty);

      if (method.getReturnType().equals(void.class)
        && method.getParameterCount() == 1) {
        builder.setter(method);
        builder.hasSetter(true);
      }

      if (method.getParameterCount() == 0
        && !method.getReturnType().equals(void.class)) {
        builder.getter(method);
        builder.hasGetter(true);
      }

    }
  }
  public PropertyInfo[] getProperties(Class<? extends RuntimeContext> targetClass) {

    final var builders = new HashMap<String, PropertyInfo.PropertyInfoBuilder>();

    fetchPropertiesFromFields(builders, targetClass);
    fetchPropertiesFromMethods(builders, targetClass);

    return builders.values().stream()
            .map(PropertyInfo.PropertyInfoBuilder::build)
            .toArray(PropertyInfo[]::new);
  }

  // TODO: удалить?
  public IVariable[] getGlobalContextVariables(AttachableContext context) {
    List<IVariable> variables = new ArrayList<>();
    var contexts = TypeManager.getInstance().getEnumerationContext();
    var index = 0;
    for (var enumContext : contexts) {
      var variable = VariableReference.createContextPropertyReference(context, index, enumContext.getContextInfo().getName());
      variables.add(variable);
      index++;
    }
    return variables.toArray(new IVariable[0]);
  }

  // FIXME: перенести в подходящий класс
  private static Method getMethodByName(Class<? extends RuntimeContext> targetClass, String name) {
    for (var method : targetClass.getMethods()) {
      if (method.getName().equalsIgnoreCase(name)) {
        return method;
      }
    }
    return null;
  }

}
