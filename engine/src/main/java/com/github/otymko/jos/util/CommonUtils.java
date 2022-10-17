/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import com.github.otymko.jos.core.exception.ErrorPositionInfo;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.DataType;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Утилитный класс общего назначения (TODO: мусорка для методов, нужно разобрать)
 */
@UtilityClass
public class CommonUtils {
    /**
     * Получить контект из файла по указанному пути.
     *
     * @param path путь к файлу.
     *
     * @return Контент файла.
     *
     * @throws IOException Исключение I/O при работе с содержимым файла.
     */
    public String getContentFromFile(Path path) throws IOException {
        return IOUtils.toString(path.toUri(), StandardCharsets.UTF_8);
    }

    /**
     * Получить абсолютный путь к файлу.
     *
     * @param path путь к файлу.
     */
    public String getAbsolutPath(Path path) {
        return path.normalize().toAbsolutePath().toString();
    }

    /**
     * Заполнить место возникновения ошибки.
     *
     * @param errorPositionInfo Информация о месте возникновения ошибки, где ее нужно заполнить.
     * @param image Образ модуля.
     * @param numberLine Номер строки.
     */
    public void fillCodePositionInErrorInfo(ErrorPositionInfo errorPositionInfo, ModuleImage image, int numberLine) {
        var source = image.getSource().getContent().split("\n");
        if (source.length >= numberLine) {
            errorPositionInfo.setCode(source[numberLine - 1]);
        }
    }

    /**
     * Проверить, валиден ли строковой идентификатор.
     *
     * @param name Значение строки.
     */
    public static boolean isValidStringIdentifier(IValue name) {
        if (name.getDataType() == DataType.STRING) {
            return isValidStringIdentifier(name.asString());
        }

        return false;
    }

    /**
     * Возвращает признак, что текущая операционная система на базе Windows.
     */
    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    private static boolean isValidStringIdentifier(String name) {
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
}
