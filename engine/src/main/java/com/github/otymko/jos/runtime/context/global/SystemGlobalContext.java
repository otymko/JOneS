/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.compiler.EnumerationHelper;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.GlobalContextClass;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.enumeration.MessageStatus;
import com.github.otymko.jos.runtime.context.type.enumeration.SymbolsContext;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@GlobalContextClass
@NoArgsConstructor
public class SystemGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(SystemGlobalContext.class);

    @ContextProperty(name = "Символы", alias = "Chars")
    public static final SymbolsContext SYMBOLS = new SymbolsContext();

    @ContextMethod(name = "Сообщить", alias = "Message")
    // TODO: для null аргументов можно ввести @ContextMethodArgument(defaultValue = MessageStatus.ORDINARY)
    public static void message(String message, IValue status) {
        var statusValue = EnumerationHelper.getEnumValueOrDefault(status, MessageStatus.ORDINARY);

        String rawMessage;
        switch ((MessageStatus) statusValue.getValue()) {
            case WITHOUT_STATUS:
            case ORDINARY:
                rawMessage = message;
                break;
            default:
                rawMessage = String.format("%s: %s", statusValue.getName(), message);
                break;
        }

        // TODO: увести в отдельный класс консоли
        System.out.println(rawMessage);
    }

    @ContextMethod(name = "ТекущаяУниверсальнаяДатаВМиллисекундах", alias = "CurrentUniversalDateInMilliseconds")
    public static float currentUniversalDateInMilliseconds() {
        return System.nanoTime() / 1_000_000;
    }

    @ContextMethod(name = "ЗначениеЗаполнено", alias = "ValueIsFilled")
    public static boolean valueIsFilled(IValue value) {
        return valueIsFilledInternal(value);
    }

    private static boolean valueIsFilledInternal(IValue pValue) {
        if (pValue == null) {
            return false;
        }

        final var value = pValue.getRawValue();

        if (value instanceof NullValue) {
            return false;
        }

        switch (value.getDataType()) {
            case UNDEFINED:
                return false;
            case STRING:
                return !value.asString().isBlank();
            case NUMBER:
                return !value.asNumber().equals(BigDecimal.ZERO);
            case DATE:
                return !((DateValue) value).isEmpty();
            case BOOLEAN:
                return true;
            default:
                throw MachineException.checkIsFilledNotSupportedForType(value.getDataType().toString());
        }
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
