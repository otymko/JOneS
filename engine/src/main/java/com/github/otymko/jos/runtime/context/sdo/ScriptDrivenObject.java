/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.sdo;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.core.IVariable;
import com.github.otymko.jos.runtime.Variable;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.MachineInstance;
import lombok.Getter;

/**
 * Абстрактная реализация объекта скрипта
 */
public abstract class ScriptDrivenObject extends ContextValue implements AttachableContext {
    /**
     * Образ модуля.
     */
    @Getter
    private final ModuleImage moduleImage;
    /**
     * Состояние модуля.
     */
    @Getter
    private IVariable[] state;

    protected ScriptDrivenObject(ModuleImage moduleImage) {
        this.moduleImage = moduleImage;
        init();
    }

    /**
     * Инициализировать объект модуля.
     *
     * @param engine Движок.
     */
    public void initialize(ScriptEngine engine) {
        engine.getMachine().executeModuleBody(this);
    }

    /**
     * Инициализировать объект модуля на указанной стековой машине.
     *
     * @param machine Стековая машина.
     *
     */
    public void initialize(MachineInstance machine) {
        machine.executeModuleBody(this);
    }

    /**
     * Получить индекс метода скрипта.
     *
     * @param name Имя метода.
     */
    public int getScriptMethod(String name) {
        for (var index = 0; index < moduleImage.getMethods().size(); index++) {
            var methodDescription = moduleImage.getMethods().get(index);
            if (methodDescription.getSignature().getName().equalsIgnoreCase(name)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * Выполнить метод скрипта и вернуть результат.
     *
     * @param engine Движок.
     * @param methodId Индекс метода.
     * @param parameters Параметры метода.
     */
    public IValue callScriptMethod(ScriptEngine engine, int methodId, IValue[] parameters) {
        return engine.getMachine().executeMethod(this, methodId, parameters);
    }

    /**
     * Выполнить метод скрипта на указанной стековой машине и вернуть результат.
     *
     * @param machine Стековая машина.
     * @param methodId Индекс метода.
     * @param parameters Параметры метода.
     */
    /* Вариант с отдельной стековой машиной */
    public IValue callScriptMethod(MachineInstance machine, int methodId, IValue[] parameters) {
        return machine.executeMethod(this, methodId, parameters);
    }

    @Override
    public int compareTo(IValue o) {
        // TODO: реализовать сравнение sdo
        return 1;
    }

    private void init() {
        state = new IVariable[moduleImage.getVariables().size()];
        for (var index = 0; index < state.length; index++) {
            state[index] = Variable.create(ValueFactory.create(), moduleImage.getVariables().get(index).getName());
        }
    }
}
