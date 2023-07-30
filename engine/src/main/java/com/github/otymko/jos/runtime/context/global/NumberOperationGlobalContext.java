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

    @ContextMethod(name = "Цел", alias = "Int")
    public static Integer integer(Double number) {
        return (int) number.doubleValue();
    }

    @ContextMethod(name = "Окр", alias = "Round")
    public static BigDecimal round(Double number, Integer precision, Integer mode) {

        RoundingMode roundingMode = (mode != null && mode == 0) ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;
        precision = (precision != null) ? precision : 0;

        BigDecimal result = BigDecimal.valueOf(number).setScale(precision, roundingMode);
        return result.scale() < 0 ? result.setScale(0) : result;
    }

    @ContextMethod(name = "Log", alias = "Log")
    public static BigDecimal log(Double number) {
        return BigDecimal.valueOf(Math.log(number)).stripTrailingZeros();
    }

    @ContextMethod(name = "Log10", alias = "Log10")
    public static BigDecimal log10(Double number) {
        return BigDecimal.valueOf(Math.log10(number)).stripTrailingZeros();
    }

    @ContextMethod(name = "Sin", alias = "Sin")
    public static Double sin(Double number) {
        return Math.sin(number);
    }

    @ContextMethod(name = "Cos", alias = "Cos")
    public static Double cos(Double number) {
        return Math.cos(number);
    }

    @ContextMethod(name = "Tan", alias = "Tan")
    public static Double tan(Double number) {
        return Math.tan(number);
    }

    @ContextMethod(name = "ASin", alias = "ASin")
    public static Double asin(Double number) {
        return Math.asin(number);
    }

    @ContextMethod(name = "ACos", alias = "ACos")
    public static Double acos(Double number) {
        return Math.acos(number);
    }

    @ContextMethod(name = "ATan", alias = "ATan")
    public static Double atan(Double number) {
        return Math.atan(number);
    }

    @ContextMethod(name = "Exp", alias = "Exp")
    public static Double Exp(Double number) {
        return Math.exp(number);
    }

    @ContextMethod(name = "Pow", alias = "Pow")
    public static BigDecimal pow(Double base, Double power) {
        return BigDecimal.valueOf(Math.pow(base, power)).stripTrailingZeros();
    }

    @ContextMethod(name = "Sqrt", alias = "Sqrt")
    public static BigDecimal sqrt(Double number) {
        return BigDecimal.valueOf(Math.sqrt(number)).stripTrailingZeros();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
