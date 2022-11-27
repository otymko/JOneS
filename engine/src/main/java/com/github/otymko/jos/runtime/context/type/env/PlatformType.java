/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.env;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.core.annotation.EnumValue;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;

/**
 * Содержит известные поддерживаемые типы платформ
 */
@EnumClass(name = "ТипПлатформы", alias = "PlatformType")
public enum PlatformType implements EnumType {

    /**
     * Операционная система семейства linux, архитектура i386
     */
    @EnumValue(name = "Linux_x86", alias = "Linux_x86")
    LINUX_X86,

    /**
     * Операционная система семейства linux, архитектура amd64
     */
    @EnumValue(name = "Linux_x86_64", alias = "Linux_x86_64")
    LINUX_X86_64,

    /**
     * Операционная система Mac OS X, архитектура i386
     */
    @EnumValue(name = "MacOS_x86", alias = "MacOS_x86")
    MACOS_X86,

    /**
     * Операционная система семейства Mac OS X, архитектура amd64
     */
    @EnumValue(name = "MacOS_x86_64", alias = "MacOS_x86_64")
    MACOS_X86_64,

    /**
     * Операционная система семейства WinNT, архитектура i386
     */
    @EnumValue(name = "Windows_x86", alias = "Windows_x86")
    Windows_x86,

    /**
     * Операционная система семейства WinNT, архитектура amd64
     */
    @EnumValue(name = "Windows_x86_64", alias = "Windows_x86_64")
    WINDOWS_X86_64,

    /**
     * Неизвестная операционная система
     */
    @EnumValue(name = "Unknown", alias = "Unknown")
    UNKNOWN;

    public static final EnumerationContext INFO = new EnumerationContext(PlatformType.class);

    /**
     * Определяет тип платформы по строковому представлению операционной системы
     * @param osName Строковое представление операционной системы
     * @param is64 Признак 64-битной архитектуры
     * @return Определенный тип платформы
     */
    public static PlatformType parse(String osName, boolean is64) {
        var prepared = osName.toUpperCase();
        if (prepared.contains("WINDOWS"))
            return is64 ? WINDOWS_X86_64 : Windows_x86;
        if (prepared.contains("LINUX"))
            return is64 ? LINUX_X86_64 : LINUX_X86;
        if (prepared.contains("MAC OS"))
            return is64 ? MACOS_X86_64 : MACOS_X86;
        return PlatformType.UNKNOWN;
    }

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
