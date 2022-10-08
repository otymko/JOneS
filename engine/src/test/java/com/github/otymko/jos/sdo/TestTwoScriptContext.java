/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.sdo;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

@ContextClass(name = "СкриптСКлючом", alias = "ScriptWithKey")
public class TestTwoScriptContext extends ScriptDrivenObject {
    public static final ContextInfo INFO = ContextInfo.createByClass(TestTwoScriptContext.class);

    public TestTwoScriptContext(ModuleImage moduleImage) {
        super(moduleImage);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextMethod(name = "ПолучитьКлюч", alias = "GetKey")
    public IValue getKey() {
        return ValueFactory.create("Ключ из " + INFO.getName());
    }

}
