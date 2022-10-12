/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.AttachableContext;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.GlobalContextClass;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.security.SecureRandom;

@GlobalContextClass
@NoArgsConstructor
public class FileOperationsGlobalContext implements AttachableContext {
    public static final ContextInfo INFO = ContextInfo.createByClass(FileOperationsGlobalContext.class);

    private static final SecureRandom random = new SecureRandom();

    private static final String PROPERTY_TMPDIR = "java.io.tmpdir";
    private static final String TMP_PREFIX = "tmp";

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextMethod(name = "ПолучитьИмяВременногоФайла", alias = "GetTempFileName")
    public static IValue getTempFileName(String extension) {
        String suffix = "";
        if (extension != null) {
            suffix = "." + extension;
        }

        String tmpDirectory = System.getProperty(PROPERTY_TMPDIR);
        String fileName = getRandomFileName(suffix);
        String pathToFile = Path.of(tmpDirectory, fileName).toString();

        return ValueFactory.create(pathToFile);
    }

    private static String getRandomFileName(String extension) {
        var name = TMP_PREFIX + System.currentTimeMillis() + random.nextLong();
        if (!extension.isEmpty()) {
            name = name + extension;
        }

        return name;
    }
}
