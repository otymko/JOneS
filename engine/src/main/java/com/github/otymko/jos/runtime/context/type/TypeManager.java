/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import org.reflections.Reflections;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.otymko.jos.localization.MessageResource.ENUM_TYPE_NOT_FOUND;

public class TypeManager {
    private static final TypeManager INSTANCE = new TypeManager();

    private static final String ENUM_PACKAGE_NAME = "com.github.otymko.jos.runtime.context.type.enumeration";

    private final TypeStorage storage = new TypeStorage();

    private TypeManager() {
        storage.getEnumerationContext().addAll(getSystemEnums());
        StandardTypeInitializer.initialize(this);
    }

    public static TypeManager getInstance() {
        return INSTANCE;
    }

    public void registerType(String name, ContextInfo info) {
        storage.getTypes().put(name, info);
    }

    public void implementEnumeration(Class<? extends EnumType> enumType) {
        var context = new EnumerationContext(enumType);
        storage.getEnumerationContext().add(context);
    }

    public EnumerationContext getEnumByClass(Class<? extends EnumType> enumClass) {
        var enumContext = storage.getEnumerationContext().stream()
                .filter(context -> context.getEnumType() == enumClass)
                .findAny();
        if (enumContext.isEmpty()) {
            throw new MachineException(Resources.getResourceString(ENUM_TYPE_NOT_FOUND));
        }
        return enumContext.get();
    }

    public Optional<ContextInfo> getContextInfoByName(String name) {
        return Optional.ofNullable(storage.getTypes().getOrDefault(name, null));
    }

    public List<EnumerationContext> getEnumerationContext() {
        return storage.getEnumerationContext();
    }

    private static List<EnumerationContext> getSystemEnums() {
        var reflections = new Reflections(ENUM_PACKAGE_NAME);
        return reflections.getSubTypesOf(EnumType.class).stream()
                .map(EnumerationContext::new)
                .collect(Collectors.toList());
    }

}
