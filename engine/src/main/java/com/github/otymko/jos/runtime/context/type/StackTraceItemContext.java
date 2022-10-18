/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.machine.StackTraceRecord;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

/**
 * Реализация типа "КадрСтекаВызовов". Используется в информации об ошибке.
 */
@ContextClass(name = "КадрСтекаВызовов", alias = "CallStackFrame")
public class StackTraceItemContext extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(StackTraceItemContext.class);

    private final String method;
    private final int lineNumber;
    private final String moduleName;

    public StackTraceItemContext(StackTraceRecord record) {
        this.method = record.getMethodName();
        this.lineNumber = record.getLineNumber();
        this.moduleName = record.getSource().toString();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextProperty(name = "Метод", alias = "Method")
    public String getMethod() {
        return method;
    }

    @ContextProperty(name = "НомерСтроки", alias = "LineNumber")
    public int getLineNumber() {
        return lineNumber;
    }

    @ContextProperty(name = "ИмяМодуля", alias = "ModuleName")
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String toString() {
        return String.format("%s: %d (%s)", method, lineNumber, moduleName);
    }
}
