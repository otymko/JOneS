/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.compiler.AnnotationDefinition;
import com.github.otymko.jos.core.IVariable;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.VariableReference;
import com.github.otymko.jos.runtime.context.global.*;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.machine.MachineInstance;
import com.github.otymko.jos.runtime.machine.info.ConstructorInfo;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;
import com.github.otymko.jos.runtime.machine.info.PropertyInfo;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Помощник инициализации контекста.
 */
@UtilityClass
public class ContextInitializer {
    /**
     * Инициализировать контекст в стековой машине.
     *
     * @param machineInstance Стековая машина.
     */
    public void initialize(MachineInstance machineInstance) {
        machineInstance.implementContext(new GlobalContext());
        machineInstance.implementContext(new SystemGlobalContext());
        machineInstance.implementContext(new StringOperationGlobalContext());
        machineInstance.implementContext(new FileOperationsGlobalContext());
        machineInstance.implementContext(new NumberOperationGlobalContext());
    }

    /**
     * Получить контекст метода.
     *
     * @param contextClass контекстный класс.
     */
    public MethodInfo[] getContextMethods(Class<? extends RuntimeContext> contextClass) {
        List<MethodInfo> methods = new ArrayList<>();
        for (var method : contextClass.getMethods()) {
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

    /**
     * Получить параметры метода.
     *
     * @param method ссылка на нативный метод.
     */
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

    /**
     * Получить конструкторы.
     *
     * @param contextClass Контекстный класс.
     */
    public ConstructorInfo[] getConstructors(Class<? extends RuntimeContext> contextClass) {
        List<ConstructorInfo> constructors = new ArrayList<>();
        for (var method : contextClass.getMethods()) {
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

    /**
     * Получить свойства.
     *
     * @param contextClass Контекстный класс.
     */
    public PropertyInfo[] getProperties(Class<? extends RuntimeContext> contextClass) {
        List<PropertyInfo> properties = new ArrayList<>();

        // заполняем свойствами, которые определены на полях
        for (var field : contextClass.getDeclaredFields()) {
            var contextProperty = field.getAnnotation(ContextProperty.class);
            if (contextProperty == null) {
                continue;
            }

            // FIXME отказаться от этого, реализовать только через методы
            // проектное решение для доступа через стековую машину к приватным полям
            field.setAccessible(true); // NOSONAR

            var setter = getMethodByName(contextClass, "set" + field.getName());
            var hasSetter = setter != null;

            var getter = getMethodByName(contextClass, "get" + field.getName());
            var hasGetter = getter != null;

            // FIXME: нужен билдер
            var property = new PropertyInfo(contextProperty.name(), contextProperty.alias(),
                    contextProperty.accessMode(), field, hasSetter, setter, hasGetter, getter);

            properties.add(property);
        }

        for (var method : contextClass.getMethods()) {
            var contextProperty = method.getAnnotation(ContextProperty.class);
            if (contextProperty == null) {
                continue;
            }

            Method getter = null;
            if (method.getName().toLowerCase(Locale.ENGLISH).startsWith("get")) {
                getter = method;
            }

            Method setter = null;
            if (method.getName().toLowerCase(Locale.ENGLISH).startsWith("set")) {
                setter = method;
            }

            if (getter != null) {
                setter = getMethodByName(contextClass, "set" + contextProperty.alias());
            } else {
                getter = getMethodByName(contextClass, "get" + contextProperty.alias());
            }

            var property = new PropertyInfo(contextProperty.name(), contextProperty.alias(),
                    contextProperty.accessMode(), null, setter != null, setter, getter != null, getter);

            properties.add(property);
        }

        return properties.toArray(new PropertyInfo[0]);
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
