/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine;

import lombok.Data;

/**
 * Прыжок исключения.
 */
@Data
class ExceptionJumpInfo {
    /**
     * Адрес обработчик.
     */
    private int handlerAddress;
    /**
     * Кадр обработчика.
     */
    private ExecutionFrame handlerFrame;
    /**
     * Размер стека.
     */
    private int stackSize;
}
