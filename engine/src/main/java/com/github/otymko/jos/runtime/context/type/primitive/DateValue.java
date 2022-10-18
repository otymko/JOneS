/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.primitive;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import com.github.otymko.jos.runtime.context.type.PrimitiveValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.format.ValueFormatter;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.function.Predicate;

@ContextClass(name = "Дата", alias = "Date")
public class DateValue extends PrimitiveValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(DateValue.class);

    private static final Date EMPTY_DATE = new GregorianCalendar(1, Calendar.JANUARY, 1).getTime();
    private static final Predicate<String> IS_EMPTY_DATE = view -> view.equals("00000000") || view.equals("000000000000")
            || view.equals("00000000000000");

    private final Date value;

    public DateValue(Date value) {
        this.value = value;
        setDataType(DataType.DATE);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public Date asDate() {
        return value;
    }

    @Override
    public String asString() {
        return ValueFormatter.format(this, "");
    }

    @Override
    public int compareTo(IValue object) {
        if (object.getDataType() == DataType.DATE) {
            return value.compareTo(object.asDate());
        }
        return super.compareTo(object);
    }

    public static boolean isEmpty(Date date) {
        return date.equals(EMPTY_DATE);
    }

    public boolean isEmpty() {
        return value.equals(EMPTY_DATE);
    }

    public static IValue parse(String view) {
        IValue result;
        String format;
        if (view.length() == 14) {
            format = "yyyyMMddHHmmss";
        } else if (view.length() == 8) {
            format = "yyyyMMdd";
        } else if (view.length() == 12) {
            format = "yyyyMMddHHmm";
        } else {
            throw MachineException.convertToDateException();
        }
        if (IS_EMPTY_DATE.test(view)) {
            result = ValueFactory.create(EMPTY_DATE);
        } else {
            Date date;
            try {
                var dateFormat = new SimpleDateFormat(format);
                date = dateFormat.parse(view);
            } catch (ParseException exception) {
                throw MachineException.convertToDateException();
            }
            result = ValueFactory.create(date);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof IValue)) {
            return false;
        }
        var baseValue = (IValue) obj;
        return baseValue.getDataType() == DataType.DATE && value.equals(baseValue.asDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
