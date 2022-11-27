/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.env;

import com.github.otymko.jos.core.PropertyAccessMode;
import com.github.otymko.jos.core.annotation.ContextClass;
import com.github.otymko.jos.core.annotation.ContextConstructor;
import com.github.otymko.jos.core.annotation.ContextProperty;
import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Содержит описание ключевых параметров среды исполнения
 */
@NoArgsConstructor
@ContextClass(name = "СистемнаяИнформация", alias = "SystemInfo")
public class V8SystemInfo extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8SystemInfo.class);

    /**
     * Создает новый объект СистеимнаяИнформация
     *
     * @return СистемнаяИнформация
     */
    @ContextConstructor
    public static V8SystemInfo constructor() {
        return new V8SystemInfo();
    }

    /**
     * Возвращает версию движка среды исполнения
     *
     * @return Строка с версией движка
     */
    @ContextProperty(name = "Версия", alias = "Version", accessMode = PropertyAccessMode.READ_ONLY)
    public String getVersion() {
        var manifestStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("META-INF/MANIFEST.MF");
        var manifest = new Manifest();
        try {
            manifest.read(manifestStream);
        } catch (IOException e) {
            return "";
        }
        var versionFromFile = manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
        return versionFromFile == null ? "" : versionFromFile;
    }

    /**
     * Возвразщает имя компьютера среды исполнения
     *
     * @return Имя компьютера
     */
    @ContextProperty(name = "ИмяКомпьютера", alias = "MachineName")
    public String getMachineName() {
        throw MachineException.operationNotImplementedException();
    }

    /**
     * Возвращает имя и версию операцинной системы среды исполнения
     *
     * @return Имя и версия операционной системы
     */
    @ContextProperty(name = "ВерсияОС", alias = "OSVersion")
    public String getOsVersion() {
        return String.format("%s %s",
                System.getProperty("os.name"),
                System.getProperty("os.version"));
    }

    /**
     * Возвращает тип платформы среды исполнения
     * @return Тип платформы
     */
    @ContextProperty(name = "ТипПлатформы", alias = "PlatformType")
    public PlatformType getPlatformType() {
        var is64 = getIs64bitOperatingSystem();
        var osName = System.getProperty("os.name");
        return PlatformType.parse(osName, is64);
    }

    /**
     * Возвращает пользователя операционной системы, от имени которого исполняется скрипт
     * @return Имя пользователя ОС
     */
    @ContextProperty(name = "ПользовательОС", alias = "OSUser")
    public String getOsUser() {
        return System.getProperty("user.name");
    }

    /**
     * Возвращает признак 64-битной операционной системы
     *
     * @return Признак 64-битной ОС
     */
    @ContextProperty(name = "Это64БитнаяОперационнаяСистема", alias = "Is64bitOperatingSystem")
    public boolean getIs64bitOperatingSystem() {
        var arch = System.getProperty("os.arch");
        return arch.endsWith("64");
    }

    /**
     * Возвращает количество процессоров текущей среды исполнения
     *
     * @return Количество процессоров
     */
    @ContextProperty(name = "КоличествоПроцессоров", alias = "ProcessorCount")
    public int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Возвращает размер страницы оперативной памяти текущей среды исполнения
     *
     * @return Размер страницы
     */
    @ContextProperty(name = "РазмерСтраницы", alias = "SystemPageSize")
    public int getSystemPageSize() {
        throw MachineException.operationNotImplementedException();
    }

    /**
     * Возвращает время работы с момента загрузки системы
     *
     * @return Время в миллисекундах
     */
    @ContextProperty(name = "ВремяРаботыСМоментаЗагрузки", alias = "TickCount")
    public long getTickCount() {
        return System.currentTimeMillis();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
