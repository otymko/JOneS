/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.module;

import com.github.otymko.jos.runtime.machine.OperationCode;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.Writer;

/**
 * Дампер образа модуля.
 */
@UtilityClass
public class ModuleImageDumper {
    public static void dump(ModuleImage image, Writer writer) throws IOException {
        int offset = 0;
        for (var command : image.getCode()) {
            writer.write(String.format("%6d: %s", offset, command));
            if (command.getCode() == OperationCode.PUSH_CONST
                    || command.getCode() == OperationCode.RESOLVE_METHOD_PROC
                    || command.getCode() == OperationCode.RESOLVE_METHOD_FUNC
                    || command.getCode() == OperationCode.RESOLVE_PROP) {
                writer.write(String.format(" ; %s", image.getConstants().get(command.getArgument())));
            }
            writer.write('\n');
            offset++;
        }
    }
}
