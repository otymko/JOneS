/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.core.IVariable;
import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.VariableReference;
import com.github.otymko.jos.runtime.machine.info.MethodInfo;

/**
 * Подключаемый контекст.
 */
public interface AttachableContext extends RuntimeContext, ContextType {
    /**
     * Методы контекста.
     */
    default MethodInfo[] getMethods() {
        return getContextInfo().getMethods();
    }

    /**
     * Переменные контекста.
     */
    default IVariable[] getVariables() {
        var variables = new IVariable[getContextInfo().getProperties().length];
        var index = 0;
        for (var property : getContextInfo().getProperties()) {
            variables[index] = VariableReference.createContextPropertyReference(this, index, property.getName());
            index++;
        }
        return variables;
    }
}
