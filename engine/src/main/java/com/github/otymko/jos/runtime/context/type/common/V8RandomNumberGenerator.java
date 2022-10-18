/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.common;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import java.util.Random;

/**
 * Объект для получения последовательности псевдослучайных чисел
 */
@ContextClass(name = "ГенераторСлучайныхЧисел", alias = "RandomNumberGenerator")
public class V8RandomNumberGenerator extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8RandomNumberGenerator.class);
    private static final long MAX_RANGE = 4294967295L;

    private final Random random;

    @ContextConstructor
    public static IValue constructor(IValue seed) {
        var seedAsNumber = ValueFactory.rawValueOrUndefined(seed);
        if (seedAsNumber.getDataType() == DataType.NUMBER) {
            return new V8RandomNumberGenerator(seedAsNumber.asNumber().longValue());
        } else if (seedAsNumber.getDataType() == DataType.UNDEFINED) {
            return new V8RandomNumberGenerator();
        } else {
            throw MachineException.invalidArgumentValueException();
        }
    }

    private V8RandomNumberGenerator() {
        random = new Random(); //NOSONAR
    }

    private V8RandomNumberGenerator(long seed) {
        random = new Random(seed); //NOSONAR
    }

    @ContextMethod(name = "СлучайноеЧисло", alias = "RandomNumber")
    public long randomNumber(Long lower, Long upper) {
        long realLower = (lower == null) ? 0 : lower;
        long readUpper = (upper == null) ? MAX_RANGE : upper;
        if (realLower > readUpper) {
            throw MachineException.invalidArgumentValueException();
        }
        var range = readUpper - realLower;
        var value = next() % (range + 1);
        return value + realLower;
    }

    private long next() {
        var bytes = new byte[4];
        random.nextBytes(bytes);
        return (bytes[0] & 0xFFL) << 24
                | (bytes[1] & 0xFFL) << 16
                | (bytes[2] & 0xFFL) << 8
                | (bytes[3] & 0xFFL);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
