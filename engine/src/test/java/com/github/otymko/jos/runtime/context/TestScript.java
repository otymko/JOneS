package com.github.otymko.jos.runtime.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указание скрипта для тестирования, используется в BaseScriptTest
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TestScript {
    String script();
}
