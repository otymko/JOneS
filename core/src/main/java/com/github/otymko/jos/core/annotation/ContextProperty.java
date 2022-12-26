/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.core.annotation;

import com.github.otymko.jos.core.PropertyAccessMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Признак свойства контекстного класса.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ContextProperty {
    /**
     * Имя свойства на русском.
     */
    String name();

    /**
     * Альтернативное имя свойства на английском.
     */
    String alias();

    /**
     * Режим доступа к свойству.
     */
    PropertyAccessMode accessMode() default PropertyAccessMode.READ_AND_WRITE;
}
