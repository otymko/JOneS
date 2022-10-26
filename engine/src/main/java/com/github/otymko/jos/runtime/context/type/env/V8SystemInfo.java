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
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ContextClass(name = "СистемнаяИнформация", alias = "SystemInfo")
public class V8SystemInfo extends ContextValue {
    public static final ContextInfo INFO = ContextInfo.createByClass(V8SystemInfo.class);

    @ContextConstructor
    public static V8SystemInfo constructor() {
        return new V8SystemInfo();
    }

    @Getter
    @ContextProperty(name = "Версия", alias = "Version", accessMode = PropertyAccessMode.READ_ONLY)
    private final String version = "0.2.0"; // FIXME: извлекать из движка

    @ContextProperty(name = "ИмяКомпьютера", alias = "MachineName")
    public String getMachineName() {
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "ВерсияОС", alias = "OSVersion")
    public String getOsVersion() {
        return String.format("%s %s",
                System.getProperty("os.name"),
                System.getProperty("os.version"));
    }

    @ContextProperty(name = "ТипПлатформы", alias = "PlatformType")
    public PlatformType getPlatformType() {
        var is64 = getIs64bitOperatingSystem();
        var osName = System.getProperty("os.name");
        return PlatformType.parse(osName, is64);
    }

    @ContextProperty(name = "ПользовательОС", alias = "OSUser")
    public String getOsUser() {
        return System.getProperty("user.name");
    }

    @ContextProperty(name = "Это64БитнаяОперационнаяСистема", alias = "Is64bitOperatingSystem")
    public boolean getIs64bitOperatingSystem() {
        var arch = System.getProperty("os.arch");
        return arch.endsWith("64");
    }

    @ContextProperty(name = "КоличествоПроцессоров", alias = "ProcessorCount")
    public int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    @ContextProperty(name = "РазмерСтраницы", alias = "SystemPageSize")
    public int getSystemPageSize() {
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "ВремяРаботыСМоментаЗагрузки", alias = "TickCount")
    public long getTickCount() {
        return System.currentTimeMillis();
    }

    @Override
    public ContextInfo getContextInfo() {
        return INFO;
    }
}
