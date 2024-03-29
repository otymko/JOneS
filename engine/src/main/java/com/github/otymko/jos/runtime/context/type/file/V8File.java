/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.file;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextMethod;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.core.IValue;
import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.file.exception.FileAttributeException;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.util.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.function.Supplier;

/**
 * Реализация типа "Файл"
 */
@ContextClass(name = "Файл", alias = "File")
public class V8File extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8File.class);

    private final File file;

    @ContextConstructor
    public static V8File createByPath(String path) {
        return new V8File(path);
    }

    public V8File(String path) {
        this.file = new File(path);
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    @ContextProperty(name = "Имя", alias = "Name", accessMode = PropertyAccessMode.READ_ONLY)
    public String getName() {
        return file.getName();
    }

    @ContextProperty(name = "ИмяБезРасширения", alias = "BaseName", accessMode = PropertyAccessMode.READ_ONLY)
    public String getBaseName() {
        var fileName = file.getName();
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1);
        }

        return fileName;
    }

    @ContextProperty(name = "Расширение", alias = "Extension", accessMode = PropertyAccessMode.READ_ONLY)
    public String getExtension() {
        var fileName = file.getName();
        if (!fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @ContextProperty(name = "ПолноеИмя", alias = "FullName", accessMode = PropertyAccessMode.READ_ONLY)
    public IValue getFullName() {
        return doWithWrapException(() -> {
            var value = file.getAbsolutePath();

            return ValueFactory.create(value);
        });
    }

    @ContextProperty(name = "Путь", alias = "Path", accessMode = PropertyAccessMode.READ_ONLY)
    public IValue getPath() {
        return doWithWrapException(() -> {
            var parentPath = file.getParentFile().getAbsolutePath();

            return ValueFactory.create(parentPath + FileSystems.getDefault().getSeparator());
        });
    }

    @ContextMethod(name = "ПолучитьВремяИзменения", alias = "GetModificationTime")
    public IValue getModificationTime() {
        return doWithWrapException(() -> {
            BasicFileAttributes attributes = getFileAttributes();
            var instant = attributes.lastModifiedTime().toInstant();

            return ValueFactory.create(java.util.Date.from(instant));
        });
    }

    @ContextMethod(name = "ПолучитьНевидимость", alias = "GetHidden")
    public IValue getHidden() {
        return doWithWrapException(() -> ValueFactory.create(file.isHidden()));
    }

    @ContextMethod(name = "ПолучитьТолькоЧтение", alias = "GetReadOnly")
    public IValue getReadOnly() {
        return doWithWrapException(() -> ValueFactory.create(!file.canWrite()));
    }

    @ContextMethod(name = "Существует", alias = "Exists")
    public IValue isExists() {
        return doWithWrapException(() -> ValueFactory.create(file.exists()));
    }

    @ContextMethod(name = "Размер", alias = "Size")
    public IValue getSize() {
        return doWithWrapException(() -> {
            BasicFileAttributes attributes = getFileAttributes();

            return ValueFactory.create(BigDecimal.valueOf(attributes.size()));
        });
    }

    @ContextMethod(name = "УстановитьВремяИзменения", alias = "SetModificationTime")
    public void setModificationTime(Date date) {
        var instant = date.toInstant();

        doWithWrapException(() -> {
            try {
                Files.setLastModifiedTime(file.toPath(), FileTime.from(instant));
            } catch (IOException e) {
                throw new FileAttributeException(e);
            }
        });
    }

    @ContextMethod(name = "УстановитьНевидимость", alias = "SetHidden")
    public void setHidden(boolean value) {
        if (CommonUtils.isWindows()) {
            return;
        }

        doWithWrapException(() -> {
            try {
                Files.setAttribute(file.toPath(), "dos:hidden", value);
            } catch (IOException e) {
                throw new FileAttributeException(e);
            }
        });
    }

    @ContextMethod(name = "УстановитьТолькоЧтение", alias = "SetReadOnly")
    public void setReadOnly(boolean value) {
        doWithWrapException(() -> {
            var result = file.setReadable(value);
            if (!result) {
                throw new FileAttributeException();
            }
        });
    }

    @ContextMethod(name = "ЭтоКаталог", alias = "IsDirectory")
    public IValue isDirectory() {
        return doWithWrapException(() -> ValueFactory.create(file.isDirectory()));
    }

    @ContextMethod(name = "ЭтоФайл", alias = "IsFile")
    public IValue isFile() {
        return doWithWrapException(() -> ValueFactory.create(file.isFile()));
    }

    private BasicFileAttributes getFileAttributes() {
        BasicFileAttributes attributes;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            throw new FileAttributeException(e);
        }

        return attributes;
    }

    private void doWithWrapException(Runnable supplier) {
        try {
            supplier.run();
        } catch (SecurityException | FileAttributeException exception) {
            throw MachineException.fileAccessDenied(file.getAbsolutePath());
        }
    }

    private <T> T doWithWrapException(Supplier<T> supplier) {
        T result;

        try {
            result = supplier.get();
        } catch (SecurityException | FileAttributeException exception) {
            throw MachineException.fileAccessDenied(file.getAbsolutePath());
        }

        return result;
    }
}

