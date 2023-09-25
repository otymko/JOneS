/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.type.EnumerationValue;
import com.github.otymko.jos.runtime.context.type.enumeration.RoundMode;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * Глобальные методы по работе с числами.
 */
@GlobalContextClass
@NoArgsConstructor
public class NumberOperationsGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(NumberOperationsGlobalContext.class);

    @ContextMethod(name = "Цел", alias = "Int")
    public static BigDecimal integer(BigDecimal number) {
        return new BigDecimal(number.toBigInteger());
    }

    @ContextMethod(name = "Окр", alias = "Round")
    public static BigDecimal round(BigDecimal number, Integer precision, IValue mode) {
        RoundingMode roundingMode = getRoundingMode(mode);
        precision = (precision != null) ? precision : 0;

        BigDecimal result = number.setScale(precision, roundingMode);
        return result.scale() < 0 ? result.setScale(0) : result;
    }

    @ContextMethod(name = "Log", alias = "Log")
    public static BigDecimal log(BigDecimal number) {
        return toBigDecimal(Math.log(number.doubleValue()));
    }

    @ContextMethod(name = "Log10", alias = "Log10")
    public static BigDecimal log10(BigDecimal number) {
        return toBigDecimal(Math.log10(number.doubleValue()));
    }

    @ContextMethod(name = "Sin", alias = "Sin")
    public static BigDecimal sin(BigDecimal number) {
        return toBigDecimal(Math.sin(number.doubleValue()));
    }

    @ContextMethod(name = "Cos", alias = "Cos")
    public static BigDecimal cos(BigDecimal number) {
        return toBigDecimal(Math.cos(number.doubleValue()));
    }

    @ContextMethod(name = "Tan", alias = "Tan")
    public static BigDecimal tan(BigDecimal number) {
        return toBigDecimal(Math.tan(number.doubleValue()));
    }

    @ContextMethod(name = "ASin", alias = "ASin")
    public static BigDecimal asin(BigDecimal number) {
        return toBigDecimal(Math.asin(number.doubleValue()));
    }

    @ContextMethod(name = "ACos", alias = "ACos")
    public static BigDecimal acos(BigDecimal number) {
        return toBigDecimal(Math.acos(number.doubleValue()));
    }

    @ContextMethod(name = "ATan", alias = "ATan")
    public static BigDecimal atan(BigDecimal number) {
        return toBigDecimal(Math.atan(number.doubleValue()));
    }

    @ContextMethod(name = "Exp", alias = "Exp")
    public static BigDecimal Exp(BigDecimal number) {
        return toBigDecimal(Math.exp(number.doubleValue()));
    }

    @ContextMethod(name = "Pow", alias = "Pow")
    public static BigDecimal pow(BigDecimal base, BigDecimal power) {
        return toBigDecimal(Math.pow(base.doubleValue(), power.doubleValue()));
    }

    @ContextMethod(name = "Sqrt", alias = "Sqrt")
    public static BigDecimal sqrt(BigDecimal number) {
        return toBigDecimal(Math.sqrt(number.doubleValue()));
    }

    @ContextMethod(name = "Мин", alias = "Min")
    public static BigDecimal min(BigDecimal number, BigDecimal... numbers) {
        return Arrays.stream(numbers).min(BigDecimal::compareTo).get();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    private static BigDecimal toBigDecimal(Double number) {
        return BigDecimal.valueOf(number).stripTrailingZeros();
    }

    private static RoundingMode getRoundingMode(IValue mode) {
        if (mode == null)
            return RoundingMode.HALF_UP;

        if (mode instanceof EnumerationValue)
            return ((EnumerationValue) mode).getValue() == RoundMode.HALF_DOWN ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;

        if (mode instanceof NumberValue)
            return mode.asNumber().equals(BigDecimal.ZERO) ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;

        return null;
    }
}