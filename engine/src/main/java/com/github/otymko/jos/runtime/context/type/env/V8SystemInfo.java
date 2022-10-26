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
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "ТипПлатформы", alias = "PlatformType")
    public String getPlatformType() {
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "ПользовательОС", alias = "OSUser")
    public String getOsUser() {
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "Это64БитнаяОперационнаяСистема", alias = "Is64bitOperatingSystem")
    public boolean is64bitOperatingSystem() {
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "КоличествоПроцессоров", alias = "ProcessorCount")
    public int getProcessorCount() {
        throw MachineException.operationNotImplementedException();
    }

    @ContextProperty(name = "ВремяРаботыСМоментаЗагрузки", alias = "SystemPageSize")
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
