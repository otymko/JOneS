package com.github.otymko.jos.runtime.context.type.env;

import com.github.otymko.jos.core.annotation.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;

@EnumClass(name = "СпециальнаяПапка", alias = "SpecialFolder")
public enum SpecialFolder implements EnumType {
    ;
    public static final EnumerationContext INFO = new EnumerationContext(SpecialFolder.class);
    @Override
    public EnumerationContext getContextInfo() {
        return null;
    }
}
