/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.localization.MessageResource;
import com.github.otymko.jos.core.localization.Resources;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.ContextInitializer;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.machine.MachineInstance;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

/**
 * Скриптовый движок.
 */
public class ScriptEngine {
    /**
     * Менеджер типов.
     */
    @Getter
    private final TypeManager typeManager;
    /**
     * Стековая машина.
     */
    @Getter
    private final MachineInstance machine;

    public ScriptEngine() {
        typeManager = TypeManager.getInstance();
        machine = new MachineInstance(this);
        // TODO:
        // * RuntimeEnvironment
        // * SystemGlobalContext
        // * CreateProcess
        //    * CompilerService
        //    * ModuleImage -> LoadedModule
        //    * InitProcess
        initialize();
    }

    /**
     * Выполнить скрипт.
     *
     * @param pathToScript Путь к скрипту.
     *
     * @return код возврата. 0 - если ошибок не было или он не был изменен во время выполнения.
     */
    public int execute(Path pathToScript) {
        int exitCode;
        try {
            exitCode = executeInternal(pathToScript);
        } catch (IOException exception) {
            var errorMessage = String.format(Resources.getResourceString(MessageResource.ERROR_READING_FILE_SCRIPT),
                    pathToScript, exception.getMessage());
            System.out.println(errorMessage);

            exitCode = 1;
        } catch (EngineException exception) {
            System.out.println(exception.getMessage());

            exitCode = 1;
        }
        return exitCode;
    }

    /**
     * Создать новый объект модуля по его образу.
     *
     * @param image Образ модуля.
     *
     * @return Объект скрипта.
     */
    public ScriptDrivenObject newObject(ModuleImage image) {
        var scriptContext = new UserScriptContext(image);
        getMachine().implementContext(scriptContext);
        initializeScriptObject(scriptContext);
        return scriptContext;
    }

    /**
     * Создать новый объект модуля по его образу с указанным классом типа.
     *
     * @param image Образ модуля.
     * @param targetClass Указнный тип класса объека.
     *
     * @return Объект скрипта.
     *
     * @throws MachineException Исключение выполнения скрипта.
     */
    public ScriptDrivenObject newObject(ModuleImage image, Class<? extends ScriptDrivenObject> targetClass)
            throws MachineException {

        var scriptContext = getInstanceSdoByClass(image, targetClass);
        getMachine().implementContext(scriptContext);
        initializeScriptObject(scriptContext);
        return scriptContext;
    }

    /**
     * Создать новый объект модуля на указанной стековой машине, с образом модуля и указанным классом тип.
     *
     * @param machine Стековая машина, на которой будет создан объект.
     * @param image Образ модуля.
     * @param targetClass Указанный тип класса объекта.
     *
     * @return Объект скрипта.
     *
     * @throws MachineException Исключение выполнения скрипта.
     */
    public ScriptDrivenObject newObject(MachineInstance machine, ModuleImage image, Class<? extends ScriptDrivenObject> targetClass) throws MachineException {
        var scriptContext = getInstanceSdoByClass(image, targetClass);
        machine.implementContext(scriptContext);
        initializeScriptObject(scriptContext, machine);
        return scriptContext;
    }

    private int executeInternal(Path pathToScript) throws IOException {
        var exitCode = 0;
        var compiler = new ScriptCompiler(this);
        var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
        newObject(moduleImage);
        return exitCode;
    }

    private void initialize() {
        ContextInitializer.initialize(machine);
    }

    private ScriptDrivenObject getInstanceSdoByClass(ModuleImage image, Class<? extends ScriptDrivenObject> targetClass) {
        ScriptDrivenObject scriptContext;
        try {
            var constructor = targetClass.getConstructor(ModuleImage.class);
            scriptContext = constructor.newInstance(image);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw MachineException.failedToInstantiateSdo();
        }
        return scriptContext;
    }

    private void initializeScriptObject(ScriptDrivenObject sdo) {
        sdo.initialize(this);
    }

    private void initializeScriptObject(ScriptDrivenObject sdo, MachineInstance machine) {
        sdo.initialize(machine);
    }
}
