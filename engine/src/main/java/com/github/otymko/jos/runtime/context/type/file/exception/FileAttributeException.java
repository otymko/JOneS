/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.file.exception;

/**
 * Исключение, выбрасываемое, если не удалось прочитать или записать атрибут файла.
 */
public final class FileAttributeException extends RuntimeException {
    public FileAttributeException() {
        super();
    }

    public FileAttributeException(Throwable cause) {
        super(cause);
    }
}
