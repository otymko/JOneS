package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.runtime.context.type.ScriptTestProvider;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Базовый класс для запуска тестов из скриптов
 */
@ExtendWith(ScriptTestProvider.class)
public abstract class BaseScriptTest {
    @TestTemplate
    void moduleTest(ScriptTestProvider.ScriptTestCase testCase) {
        int methodIndex = testCase.getSdo().getScriptMethod(testCase.getTestName());
        if (methodIndex < 0) {
            throw new IllegalArgumentException(String.format("Test method \"%s\" not found", testCase.getTestName()));
        }

        testCase.getSdo().callScriptMethod(testCase.getEngine(), methodIndex, new IValue[0]);
    }
}
