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

    @Override
    public EnumerationContext getContextInfo() {
        return INFO;
    }
}
