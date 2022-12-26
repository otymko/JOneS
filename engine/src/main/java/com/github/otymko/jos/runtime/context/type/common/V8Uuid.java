/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.common;

import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * Реализация типа "УникальныйИдентификатор"
 */
@ContextClass(name = "УникальныйИдентификатор", alias = "UUID")
@EqualsAndHashCode
public final class V8Uuid extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8Uuid.class);

    @Getter
    private final UUID uuid;

    @ContextConstructor
    public static V8Uuid create() {
        return new V8Uuid();
    }

    @ContextConstructor
    public static V8Uuid create(String value) {
        return new V8Uuid(value);
    }

    private V8Uuid() {
        this.uuid = UUID.randomUUID();
    }

    private V8Uuid(String value) {
        // FIXME обработать текст исключения
        this.uuid = UUID.fromString(value);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @Override
    public int compareTo(IValue object) {
        if (!(object instanceof V8Uuid)) {
            return -1;
        }

        var value = (V8Uuid)object;

        return this.getUuid().compareTo(value.getUuid());
    }

    @Override
    public String asString() {
        return toString();
    }
}
