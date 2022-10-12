/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import com.github.otymko.jos.runtime.machine.context.ContextValueConverter;
import com.github.otymko.jos.runtime.machine.info.ConstructorInfo;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.otymko.jos.localization.MessageResource.ERROR_CALL_CONSTRUCTOR;
import static com.github.otymko.jos.localization.MessageResource.ERROR_CALL_METHOD;

/**
 * Утилитный класс для упрощения вызова методов контекста
 */
public final class ContextMethodCall {
    /**
     * Вызвать метод-конструктор. Поиск конструкторов ведется по количеству аргументов.
     *
     * @param contextInfo Информация о контексте.
     * @param arguments Аргументы метода.
     *
     * @return Результат вызова метода-конструктора
     */
    public static IValue callConstructor(ContextInfo contextInfo, IValue[] arguments) {
        final var constructorInfo = findConstructor(contextInfo, arguments.length);

        var methodCall = constructorInfo.getMethod();
        Object result;
        try {
            result = methodCall.invoke(null, prepareArguments(methodCall, arguments));
        } catch (InvocationTargetException exception) {
            throw new MachineException(exception.getTargetException().getMessage());
        } catch (IllegalAccessException exception) {
            throw new MachineException(Resources.getResourceString(ERROR_CALL_CONSTRUCTOR));
        }
        return (IValue) result;
    }

    /**
     * Вызвать метод контекста как функцию.
     *
     * @param context Контекст.
     * @param method Метод контекста.
     * @param arguments Аргументы метода контекста.
     *
     * @return Результат вызова метода контекста.
     */
    public static IValue callAsFunction(RuntimeContext context, Method method, IValue[] arguments) {
        Object result;
        try {
            result = method.invoke(context, ContextMethodCall.prepareArguments(method, arguments));
        } catch (MachineException exception) {
            throw exception;
        } catch (IllegalAccessException exception) {
            throw new MachineException(Resources.getResourceString(ERROR_CALL_METHOD), exception);
        } catch (InvocationTargetException exception) {
            if (exception.getTargetException() instanceof MachineException) {
                throw (MachineException) exception.getTargetException();
            }
            throw new MachineException(Resources.getResourceString(ERROR_CALL_METHOD), exception);
        }
        return (IValue) result;
    }

    /**
     * Вызвать метод контекста как процедуру.
     *
     * @param context Контекст.
     * @param method Метод контекста.
     * @param arguments Аргументы метода контекста.
     */
    public static void callAsProcedure(RuntimeContext context, Method method, IValue[] arguments) {
        try {
            method.invoke(context, ContextMethodCall.prepareArguments(method, arguments));
        } catch (MachineException exception) {
            throw exception;
        } catch (IllegalAccessException exception) {
            throw new MachineException(Resources.getResourceString(ERROR_CALL_METHOD), exception);
        } catch (InvocationTargetException exception) {
            if (exception.getTargetException() instanceof MachineException) {
                throw (MachineException) exception.getTargetException();
            }
            throw new MachineException(Resources.getResourceString(ERROR_CALL_METHOD), exception);
        }
    }

    private static Object[] prepareArgumentsWithVarArgs(Method method, IValue[] arguments) {
        final var list = new ArrayList<Object>();
        final var staticArgumentsCount = method.getParameterCount() - 1;
        var parameters = method.getParameters();
        for (int i = 0; i < staticArgumentsCount; i++) {
            if (i < arguments.length) {
                var parameter = parameters[i];
                var value = arguments[i];
                list.add(ContextValueConverter.convertValue(value, parameter.getType()));
            } else {
                list.add(UndefinedValue.VALUE);
            }
        }
        final var varArgs = new ArrayList<>(Arrays.asList(arguments).subList(staticArgumentsCount, arguments.length));
        list.add(varArgs.toArray(new IValue[0]));
        return list.toArray(new Object[0]);
    }

    /**
     * Подготовить аргументы для передачи в вызов метода.
     *
     * @param method Метод.
     * @param arguments Аргументы метода.
     *
     * @return Подготовленные аргументы метода, конвертированы в запрашиваемые типы.
     */
    private static Object[] prepareArguments(Method method, IValue[] arguments) {
        if (method.isVarArgs()) {
            return prepareArgumentsWithVarArgs(method, arguments);
        }
        List<Object> newArguments = new ArrayList<>(Arrays.asList(arguments));
        if (method.getParameterCount() != arguments.length) {
            while (newArguments.size() < method.getParameterCount()) {
                newArguments.add(ValueFactory.create());
            }
        }

        var convertedArguments = newArguments.toArray(new Object[0]);
        var parameters = method.getParameters();
        for (var index = 0; index < method.getParameterCount(); index++) {
            var parameter = parameters[index];
            var value = (IValue) convertedArguments[index];
            convertedArguments[index] = ContextValueConverter.convertValue(value, parameter.getType());
        }

        return convertedArguments;
    }

    private static ConstructorInfo findConstructor(ContextInfo contextInfo, int argumentsCount) {
        // TODO: ищем конструктор по количеству аргументов
        for (var constructor : contextInfo.getConstructors()) {
            if (constructor.getParameters().length == argumentsCount) {
                return constructor;
            }
        }

        // Ищем первый с бОльшим количеством параметров
        for (var constructor : contextInfo.getConstructors()) {
            if (constructor.getParameters().length > argumentsCount) {
                return constructor;
            }
        }

        // Ищем конструктор с varargs
        for (var constructor : contextInfo.getConstructors()) {
            if (constructor.getMethod().isVarArgs()) {
                return constructor;
            }
        }

        throw MachineException.constructorNotFoundException(contextInfo.getName());
    }

    private ContextMethodCall() {
        // None
    }
}
