/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.typedescription;

import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.enumeration.DateFractions;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;
import lombok.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Квалификаторы даты для Описания
 */
@ContextClass(name = "КвалификаторыДаты", alias = "DateQualifiers")
@Value
public class DateQualifiers extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(DateQualifiers.class);

    /**
     * Возвращает квалификаторы даты с указанными частями дат.
     *
     * @param sourceDateParts Части дат.
     *
     * @return Квалификатор дат.
     *
     * @see DateFractions
     * @see DateQualifiers
     */
    @ContextConstructor
    public static DateQualifiers constructor(DateFractions sourceDateParts) {
        var dateParts = sourceDateParts == null ? DateFractions.DATE_TIME : sourceDateParts;

        return new DateQualifiers(dateParts);
    }

    /**
     * Возвращает квалификаторы даты с частями дат Дата
     *
     * @return Квалификатор дат
     * @see DateFractions
     * @see DateQualifiers
     */
    @ContextConstructor
    public static DateQualifiers constructor() {
        return new DateQualifiers(DateFractions.DATE);
    }

    /**
     * Части даты, хранимые в типе
     *
     * @see DateFractions
     */
    @ContextProperty(name = "ЧастиДаты", alias = "DateFractions", accessMode = PropertyAccessMode.READ_ONLY)
    @Getter
    DateFractions dateFractions;
    private Date adjustDate(Date date) {
        switch (dateFractions) {
            case DATE: {
                var adjusted = new GregorianCalendar();
                adjusted.setTime(date);
                adjusted.set(Calendar.HOUR, 0);
                adjusted.set(Calendar.MINUTE, 0);
                adjusted.set(Calendar.SECOND, 0);
                adjusted.set(Calendar.MILLISECOND, 0);
                return adjusted.getTime();
            }
            case TIME: {
                var adjusted = new GregorianCalendar();
                adjusted.setTime(date);
                adjusted.set(Calendar.YEAR, 1);
                adjusted.set(Calendar.MONTH, Calendar.JANUARY);
                adjusted.set(Calendar.DAY_OF_MONTH, 1);
                return adjusted.getTime();
            }
            default:
            case DATE_TIME:
                return date;
        }
    }

    public IValue adjustValue(IValue value) {
        try {
            final var asDate = value.asDate();
            return ValueFactory.create(adjustDate(asDate));
        } catch (Exception e) {
            return ValueFactory.create(new GregorianCalendar(1, Calendar.JANUARY, 1).getTime());
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof DateQualifiers)) {
            return false;
        }
        final var asDateQualifiers = (DateQualifiers) o;
        return asDateQualifiers.dateFractions == dateFractions;
    }

    public int hashCode() {
        return dateFractions.hashCode();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
