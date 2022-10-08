/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.EnumValue;

@EnumClass(name = "СтатусСообщения", alias = "MessageStatus")
public enum MessageStatus implements EnumType {
    @EnumValue(name = "БезСтатуса", alias = "WithoutStatus")
    WITHOUT_STATUS,
    @EnumValue(name = "Важное", alias = "Important")
    IMPORTANT,
    @EnumValue(name = "Внимание", alias = "Attention")
    ATTENTION,
    @EnumValue(name = "Информация", alias = "Information")
    INFORMATION,
    @EnumValue(name = "Обычное", alias = "Ordinary")
    ORDINARY,
    @EnumValue(name = "ОченьВажное", alias = "VeryImportant")
    VARY_IMPORTANT
}
