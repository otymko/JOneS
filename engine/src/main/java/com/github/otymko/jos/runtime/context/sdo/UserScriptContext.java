/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.sdo;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

/**
 * Реализация типа "Скрипт".
 */
@ContextClass(name = "Скрипт", alias = "Script")
public class UserScriptContext extends ScriptDrivenObject {
    public static final ContextInfo INFO = ContextInfo.createByClass(UserScriptContext.class);

    public UserScriptContext(ModuleImage moduleImage) {
        super(moduleImage);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
