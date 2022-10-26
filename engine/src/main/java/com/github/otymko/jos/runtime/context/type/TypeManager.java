/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.localization.Resources;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedLength;
import com.github.otymko.jos.runtime.context.type.enumeration.AllowedSign;
import com.github.otymko.jos.runtime.context.type.enumeration.DateFractions;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.context.type.enumeration.MessageStatus;
import com.github.otymko.jos.runtime.context.type.enumeration.SearchDirection;
import com.github.otymko.jos.runtime.context.type.env.PlatformType;
import com.github.otymko.jos.runtime.context.type.env.SpecialFolder;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.otymko.jos.core.localization.MessageResource.ENUM_TYPE_NOT_FOUND;

/**
 * Менеджер типов.
 */
public class TypeManager {
    private static final TypeManager INSTANCE = new TypeManager();

    private final TypeStorage storage = new TypeStorage();

    public static TypeManager getInstance() {
        return INSTANCE;
    }

    private static List<EnumerationContext> getSystemEnums() {
        List<EnumerationContext> contexts = new ArrayList<>();
        contexts.add(MessageStatus.INFO);
        contexts.add(AllowedLength.INFO);
        contexts.add(AllowedSign.INFO);
        contexts.add(DateFractions.INFO);
        contexts.add(SearchDirection.INFO);
        contexts.add(PlatformType.INFO);
        contexts.add(SpecialFolder.INFO);

        return contexts;
    }

    private TypeManager() {
        storage.getEnumerationContext().addAll(getSystemEnums());
        StandardTypeInitializer.initialize(this);
    }

    /**
     * Зарегистировать тип.
     *
     * @param name Имя типа.
     * @param info Описание контекста.
     */
    public void registerType(String name, ContextInfo info) {
        storage.getTypes().put(name, info);
    }

    /**
     * Внедрить контекстное перечисление.
     *
     * @param enumType класс контекстного перечисление.
     */
    public void implementEnumeration(Class<? extends EnumType> enumType) {
        var context = new EnumerationContext(enumType);
        storage.getEnumerationContext().add(context);
    }

    /**
     * Получить контекст перечисления по контекстному классу перечисления
     *
     * @param enumClass контекстный класс перечисления.
     */
    public EnumerationContext getEnumByClass(Class<? extends EnumType> enumClass) {
        var enumContext = storage.getEnumerationContext().stream()
                .filter(context -> context.getEnumType() == enumClass)
                .findAny();
        if (enumContext.isEmpty()) {
            throw new MachineException(Resources.getResourceString(ENUM_TYPE_NOT_FOUND));
        }
        return enumContext.get();
    }

    /**
     * Полуить описание контекста по имени.
     *
     * @param name Имя типа контекста.
     */
    public Optional<ContextInfo> getContextInfoByName(String name) {
        return Optional.ofNullable(storage.getTypes().getOrDefault(name, null));
    }

    /**
     * Получить список контекство перечислений.
     */
    public List<EnumerationContext> getEnumerationContext() {
        return storage.getEnumerationContext();
    }
}
