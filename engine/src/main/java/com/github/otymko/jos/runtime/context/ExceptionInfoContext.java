/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ContextClass(name = "ИнформацияОбОшибке", alias = "ErrorInfo")
public class ExceptionInfoContext extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(ExceptionInfoContext.class);

    EngineException exception;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

}
