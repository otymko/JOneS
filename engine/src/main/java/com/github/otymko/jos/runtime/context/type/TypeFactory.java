/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.UndefinedValue;
import com.github.otymko.jos.runtime.machine.info.ConstructorInfo;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static com.github.otymko.jos.localization.MessageResource.ERROR_CALL_CONSTRUCTOR;

@UtilityClass
public class TypeFactory {

    private ConstructorInfo findConstructor(ContextInfo contextInfo, int argumentsCount) {
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

    private Object[] prepareArgumentsWithVarArgs(Method method, IValue[] arguments) {
        final var list = new ArrayList<Object>();
        final var staticArgumentsCount = method.getParameterCount() - 1;
        for (int i = 0; i < staticArgumentsCount; i++) {
            if (i < arguments.length) {
                list.add(arguments[i]);
            } else {
                list.add(UndefinedValue.VALUE);
            }
        }
        final var varArgs = new ArrayList<IValue>();
        for (int i = staticArgumentsCount; i < arguments.length; i++) {
            varArgs.add(arguments[i]);
        }
        list.add(varArgs.toArray(new IValue[0]));
        return list.toArray(new Object[0]);
    }

    private Object[] prepareArguments(Method method, IValue[] arguments) {
        if (method.isVarArgs()) {
            return prepareArgumentsWithVarArgs(method, arguments);
        }
        if (method.getParameterCount() == arguments.length) {
            return arguments;
        }
        final var list = new ArrayList<>(Arrays.asList(arguments));
        while (list.size() < method.getParameterCount()) {
            list.add(ValueFactory.create());
        }
        return list.toArray(new Object[0]);
    }

    public IValue callConstructor(ContextInfo contextInfo, IValue[] arguments) {
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

}
