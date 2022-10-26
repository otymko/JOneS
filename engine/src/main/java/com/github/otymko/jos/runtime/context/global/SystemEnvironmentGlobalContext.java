package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Map;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@GlobalContextClass
public class SystemEnvironmentGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(SystemEnvironmentGlobalContext.class);

    @ContextMethod(name = "ПеременныеСреды", alias = "EnvironmentVariables")
    public V8Map environmentVariables() {
        var result = V8Map.constructor();
        var env = System.getenv();
        for (var envEntry: env.entrySet()) {
            result.insert(ValueFactory.create(envEntry.getKey()),
                    ValueFactory.create(envEntry.getValue()));
        }
        return result;
    }

    @ContextMethod(name = "ПолучитьПеременнуюСреды", alias = "getEnvironmentVariable")
    public String getEnvironmentVariable(String name) {
        return System.getenv(name);
    }

    @ContextMethod(name = "УстановитьПеременнуюСреды", alias = "SetEnvironmentVariable")
    public void setEnvironmentVariable(String name, String value) {
        throw MachineException.operationNotImplementedException();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
