/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Map;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.Map;
import java.util.TreeMap;

@GlobalContextClass
public class SystemEnvironmentGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(SystemEnvironmentGlobalContext.class);
    private static final Map<String, String> environmentVariables = getEnvironmentVariablesSnapshot();

    private static Map<String, String> getEnvironmentVariablesSnapshot() {
        final var result = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        result.putAll(System.getenv());
        return result;
    }

    @ContextMethod(name = "ПеременныеСреды", alias = "EnvironmentVariables")
    public V8Map environmentVariables() {
        var result = V8Map.constructor();
        for (var envEntry: environmentVariables.entrySet()) {
            result.insert(ValueFactory.create(envEntry.getKey()),
                    ValueFactory.create(envEntry.getValue()));
        }
        return result;
    }

    @ContextMethod(name = "ПолучитьПеременнуюСреды", alias = "getEnvironmentVariable")
    public String getEnvironmentVariable(String name) {
        return environmentVariables.getOrDefault(name, "");
    }

    @ContextMethod(name = "УстановитьПеременнуюСреды", alias = "SetEnvironmentVariable")
    public void setEnvironmentVariable(String name, String value) {
        environmentVariables.put(name, value);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
