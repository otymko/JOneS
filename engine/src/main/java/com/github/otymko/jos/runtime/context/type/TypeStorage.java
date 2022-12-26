/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Хранилище типов.
 */
public class TypeStorage {
    /**
     * Соответствие описания типов по именам.
     */
    @Getter
    private final Map<String, ContextInfo> types = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    /**
     * Список контекство перечислений.
     */
    @Getter
    private final List<EnumerationContext> enumerationContext = new ArrayList<>();
}
