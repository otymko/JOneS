/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.file;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextConstructor;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.io.File;

@ContextClass(name = "Файл", alias = "File")
public class V8File extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8File.class);

    private static final String DOT = ".";

    private final File file;

    // FIXME фиктивные поля
    @ContextProperty(name = "Имя", alias = "Name")
    private IValue name;
    @ContextProperty(name = "ИмяБезРасширения", alias = "BaseName")
    private IValue nameWithoutExtension;
    @ContextProperty(name = "Расширение", alias = "Extension")
    private IValue extension;
    @ContextProperty(name = "ПолноеИмя", alias = "FullName")
    private IValue fullName;
    @ContextProperty(name = "Путь", alias = "Path")
    private IValue path;

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }

    public V8File(String path) {
        this.file = new File(path);
    }

    @ContextConstructor
    public static IValue constructorByPath(IValue path) {
        final var rawValue = path.getRawValue();
        if (!(rawValue instanceof StringValue)) {
            throw MachineException.invalidArgumentValueException();
        }

        return new V8File(rawValue.asString());
    }

    public IValue getName() {
        return ValueFactory.create(file.getName());
    }

    public IValue getNameWithoutExtension() {
        var name = file.getName();
        if (name.contains(".")) {
            name = name.substring(0, name.lastIndexOf(".") - 1);
        }

        return ValueFactory.create(name);
    }

    public IValue getExtension() {
        var name = file.getName();
        if (!name.contains(DOT)) {
            return ValueFactory.create("");
        }

        var extension = name.substring(name.lastIndexOf(DOT) + 1);

        return ValueFactory.create(extension);
    }

    public IValue getFullName() {
        var fullName = file.getAbsolutePath();

        return ValueFactory.create(fullName);
    }

    public IValue getPath() {
        var parentPath = file.getParentFile().getAbsolutePath();

        return ValueFactory.create(parentPath + "/");
    }

    @ContextMethod(name = "Существует", alias = "Exists")
    public IValue isExists() {
        return ValueFactory.create(file.exists());
    }
}
