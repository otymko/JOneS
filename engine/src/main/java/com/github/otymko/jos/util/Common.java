/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import com.github.otymko.jos.exception.ErrorInfo;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@UtilityClass
public class Common {

    public String getContentFromFile(Path path) throws IOException {
        return IOUtils.toString(path.toUri(), StandardCharsets.UTF_8);
    }

    public String getAbsolutPath(Path path) {
        return path.normalize().toAbsolutePath().toString();
    }

    public void fillCodePositionInErrorInfo(ErrorInfo errorInfo, ModuleImage image, int numberLine) {
        var source = image.getSource().getContent().split("\n");
        if (source.length >= numberLine) {
            errorInfo.setCode(source[numberLine - 1]);
        }
    }

    public static boolean isValidStringIdentifier(IValue name) {
        if (name.getDataType() == DataType.STRING) {
            return isValidStringIdentifier(name.asString());
        }
        return false;
    }

    public static boolean isValidStringIdentifier(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        var chars = name.toCharArray();
        if (!(Character.isLetter(chars[0]) || chars[0] == '_')) {
            return false;
        }

        for (var index = 1; index < name.length(); index++) {
            if (!(Character.isLetterOrDigit(chars[index]) || chars[index] == '_')) {
                return false;
            }
        }

        return true;
    }

    /**
     * Возвращает признак, что текущая операционная система на базе Windows.
     */
    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

}
