/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.core.localization.MessageResource;
import com.github.otymko.jos.core.localization.Resources;
import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collections;

/**
 * Реализация типа "ИнформацияОбОшибке" (описание ошибки в блоке исключения)
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ContextClass(name = "ИнформацияОбОшибке", alias = "ErrorInfo")
public class ExceptionInfoContext extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(ExceptionInfoContext.class);
    /**
     * Нативное исключение.
     */
    EngineException exception;
    /**
     * Имя модуля.
     */
    String moduleName;
    /**
     * Исходный код.
     */
    String sourceLine;
    /**
     * Номер строки.
     */
    int lineNumber;
    /**
     * Краткое описание.
     */
    String description;
    /**
     * Стек вызовов.
     */
    StackTraceCollectionContext stackTraceCollection;

    public ExceptionInfoContext(EngineException exception) {
        this.exception = exception;

        if (exception instanceof MachineException) {
            var machineException = (MachineException)exception;

            description = machineException.getDescription();
            moduleName = machineException.getErrorPositionInfo().getSource();
            sourceLine = machineException.getErrorPositionInfo().getCode();
            lineNumber = machineException.getErrorPositionInfo().getLine();

            stackTraceCollection = new StackTraceCollectionContext(machineException.getBslStackTrace());
        } else {
            description = exception.getMessage();
            moduleName = Resources.getResourceString(MessageResource.SOURCE_CODE_NOT_AVAILABLE);
            sourceLine = "";
            lineNumber = -1;

            stackTraceCollection = new StackTraceCollectionContext(Collections.emptyList());
        }
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextProperty(name = "ИмяМодуля", alias = "ModuleName", accessMode = PropertyAccessMode.READ_ONLY)
    public String getModuleName() {
        return moduleName;
    }

    @ContextProperty(name = "ИсходнаяСтрока", alias = "SourceLine", accessMode = PropertyAccessMode.READ_ONLY)
    public String getSourceLine() {
        return sourceLine;
    }

    @ContextProperty(name = "НомерСтроки", alias = "LineNumber", accessMode = PropertyAccessMode.READ_ONLY)
    public int getLineNumber() {
        return lineNumber;
    }

    @ContextProperty(name = "Описание", alias = "Description", accessMode = PropertyAccessMode.READ_ONLY)
    public String getDescription() {
        return description;
    }

    @ContextProperty(name = "Причина", alias = "Cause", accessMode = PropertyAccessMode.READ_ONLY)
    public IValue getCause() {
        if (exception.getCause() == null) {
            return ValueFactory.create();
        }

        EngineException innerException;
        if (exception.getCause() instanceof EngineException) {
            innerException = (EngineException)exception.getCause();
        } else {
            innerException = new EngineException(exception.getCause().getMessage(), exception.getCause());
        }

        return new ExceptionInfoContext(innerException);
    }

    @ContextMethod(name = "ПолучитьСтекВызовов", alias = "GetStackTrace")
    public StackTraceCollectionContext getStackTrace() {
        return stackTraceCollection;
    }

    @ContextMethod(name = "ПодробноеОписаниеОшибки", alias = "DetailedDescription")
    public String getDetailedDescription() {
        return exception.getMessage();
    }
}
