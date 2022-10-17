/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.GlobalContextClass;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.security.SecureRandom;

/**
 * Глобальный контекст по работе с файлами.
 */
@GlobalContextClass
@NoArgsConstructor
public class FileOperationsGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(FileOperationsGlobalContext.class);

    private static final SecureRandom random = new SecureRandom();
    private static final String PROPERTY_TMPDIR = "java.io.tmpdir";
    private static final String TMP_PREFIX = "tmp";

    @ContextMethod(name = "ПолучитьИмяВременногоФайла", alias = "GetTempFileName")
    public static String getTempFileName(String extension) {
        String suffix = "";
        if (extension != null) {
            suffix = "." + extension;
        }

        String tmpDirectory = System.getProperty(PROPERTY_TMPDIR);
        String fileName = getRandomFileName(suffix);

        return Path.of(tmpDirectory, fileName).toString();
    }

    private static String getRandomFileName(String extension) {
        var name = TMP_PREFIX + System.currentTimeMillis() + random.nextLong();
        if (!extension.isEmpty()) {
            name = name + extension;
        }

        return name;
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
