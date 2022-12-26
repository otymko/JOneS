/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.runtime.context.ContextInitializer;
import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.core.annotation.EnumValue;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Информация о контексте.
 */
@Value
@Builder
@EqualsAndHashCode
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

    /**
     * Создать информацию о контексте по его классу контекста.
     *
     * @param typeClass Класс контекста.
     */
    public static ContextInfo createByClass(Class<? extends RuntimeContext> typeClass) {
        if (typeClass.getAnnotation(GlobalContextClass.class) != null) {
            return createByGlobalContextClass(typeClass);
        } else {
            return createByContextClass(typeClass);
        }
    }

    /**
     * Создать информацию о контексте по значению перечисления.
     *
     * @param enumValue Значение перечисления.
     */
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

    /**
     * Создать информацию о контексте по классу перечисления.
     *
     * @param enumClass Класс перечисления.
     */
    public static ContextInfo createByEnumClass(EnumClass enumClass) {
        var builder = ContextInfo.builder();
        builder.name(enumClass.name());
        builder.alias(enumClass.alias());
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
        var properties = ContextInitializer.getProperties(typeClass);

        var builder = ContextInfo.builder();
        builder.name(typeClass.getSimpleName());
        builder.alias(typeClass.getSimpleName());
        builder.typeClass(typeClass);
        builder.methods(methods);
        builder.properties(properties);
        builder.constructors(new ConstructorInfo[0]);

        return builder.build();
    }

    private static ContextInfo createEmptyInfo() {
        var builder = ContextInfo.builder();
        builder.name("");
        builder.alias("");
        builder.typeClass(null); // FIXME: ??
        builder.methods(new MethodInfo[0]);
        builder.properties(new PropertyInfo[0]);
        builder.constructors(new ConstructorInfo[0]);

        return builder.build();
    }

}
