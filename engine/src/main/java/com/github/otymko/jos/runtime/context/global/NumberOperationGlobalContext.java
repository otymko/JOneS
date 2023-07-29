/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Глобальные методы по работе с числами.
 */
@GlobalContextClass
@NoArgsConstructor
public class NumberOperationGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(NumberOperationGlobalContext.class);

    @ContextMethod(name = "Окр", alias = "Round")
    public static BigDecimal round(BigDecimal number, Integer precision, Integer mode) {

        RoundingMode roundingMode = (mode != null && mode == 0) ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;
        precision = (precision != null) ? precision : 0;

        BigDecimal result = number.setScale(precision, roundingMode);
        return precision > 0 ? result : result.setScale(0);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
