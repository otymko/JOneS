/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.core.exception;

import com.github.otymko.jos.core.localization.MessageResource;
import com.github.otymko.jos.core.localization.Resources;
import lombok.Data;

/**
 * Информация об ошибке с позиционированием.
 */
@Data
public class ErrorPositionInfo {
    /**
     * Номер строки кода, в котором произошла ошибка.
     */
    private int line;
    /**
     * Источник кода.
     */
    private String source = "";
    /**
     * Фрагмент кода, в котором возникла ошибка.
     */
    private String code = "";

    /**
     * Полуить источник кода.
     */
    public String getSource() {
        if (source == null || source.isEmpty()) {
            return Resources.getResourceString(MessageResource.SOURCE_CODE_NOT_AVAILABLE);
        }

        return source;
    }
}
