/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

/**
 * Реализация типа "Символы".
 */
@ContextClass(name = "Символы", alias = "Chars")
public class SymbolsContext extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(SymbolsContext.class);

    @ContextProperty(name = "ПС", alias = "LF", accessMode = PropertyAccessMode.READ_ONLY)
    private static final IValue LF = ValueFactory.create("\n");

    @ContextProperty(name = "ВК", alias = "CR", accessMode = PropertyAccessMode.READ_ONLY)
    private static final IValue CR = ValueFactory.create("\r");

    @ContextProperty(name = "ВТаб", alias = "VTab", accessMode = PropertyAccessMode.READ_ONLY)
    private static final IValue V_TAB = ValueFactory.create("\\v");

    @ContextProperty(name = "Таб", alias = "Tab", accessMode = PropertyAccessMode.READ_ONLY)
    private static final IValue TAB = ValueFactory.create("\t");

    @ContextProperty(name = "ПФ", alias = "FF", accessMode = PropertyAccessMode.READ_ONLY)
    private static final IValue FF = ValueFactory.create("\f");

    @ContextProperty(name = "НПП", alias = "Nbsp", accessMode = PropertyAccessMode.READ_ONLY)
    private static final IValue NBSP = ValueFactory.create("\u00A0");

    public SymbolsContext() {
        // none
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public DataType getDataType() {
        return DataType.OBJECT;
    }
}
