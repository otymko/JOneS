/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.core.IValue;
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
