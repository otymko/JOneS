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

@EnumClass(name = "ТипПлатформы", alias = "PlatformType")
public enum PlatformType implements EnumType {

    @EnumValue(name = "Linux_x86", alias = "Linux_x86")
    Linux_x86,

    @EnumValue(name = "Linux_x86_64", alias = "Linux_x86_64")
    Linux_x86_64,

    @EnumValue(name = "MacOS_x86", alias = "MacOS_x86")
    MacOS_x86,

    @EnumValue(name = "MacOS_x86_64", alias = "MacOS_x86_64")
    MacOS_x86_64,

    @EnumValue(name = "Windows_x86", alias = "Windows_x86")
    Windows_x86,

    @EnumValue(name = "Windows_x86_64", alias = "Windows_x86_64")
    Windows_x86_64,

    @EnumValue(name = "Unknown", alias = "Unknown")
    Unknown;

    public static final EnumerationContext INFO = new EnumerationContext(PlatformType.class);

    public static PlatformType parse(String osName, boolean is64) {
        var prepared = osName.toUpperCase();
        if (prepared.contains("WINDOWS"))
            return is64 ? Windows_x86_64 : Windows_x86;
        if (prepared.contains("LINUX"))
            return is64 ? Linux_x86_64 : Linux_x86;
        if (prepared.contains("MAC OS"))
            return is64 ? MacOS_x86_64 : MacOS_x86;
        return PlatformType.Unknown;
    }

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
