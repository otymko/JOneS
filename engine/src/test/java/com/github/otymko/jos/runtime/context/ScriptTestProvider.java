/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.common.ScriptTester;
import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.machine.context.ContextValueConverter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Провайдер контекста шаблонов тестов
 *
 * @see <a href="https://www.baeldung.com/junit5-test-templates">...</a>
 */
public class ScriptTestProvider implements TestTemplateInvocationContextProvider {
    private static final String METHOD_WITH_TESTS_ARRAY = "ПолучитьСписокТестов";
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        var testClass = context.getRequiredTestClass();
        var bslTest = testClass.getAnnotation(TestScript.class);

        return bslTest != null;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        Class<? extends BaseScriptTest> testClass = (Class<? extends BaseScriptTest>) context.getRequiredTestClass();
        var path = getScriptPath(testClass);
        var pathToScript = Path.of(path);

        var engine = new ScriptEngine();
        var compiler = new ScriptCompiler(engine);
        var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
        var sdo = engine.newObject(moduleImage);

        int methodIndex = sdo.getScriptMethod(METHOD_WITH_TESTS_ARRAY);
        if (methodIndex < 0) {
            throw new IllegalArgumentException("Method \"" + METHOD_WITH_TESTS_ARRAY + "\" not found");
        }

        var tests = sdo.callScriptMethod(engine, methodIndex, new IValue[]{new ScriptTester()});
        var rawTests = tests.getRawValue();
        if (!(rawTests instanceof V8Array)) {
            throw new IllegalArgumentException("Метод ПолучитьСписокТестов вернул не массив с именами тестов");
        }

        return Optional.of(rawTests).stream()
                .map(V8Array.class::cast)
                .flatMap(v8Array -> StreamSupport.stream(v8Array.iterator().spliterator(), false))
                .map(methodName -> new ScriptTestCaseTemplate(
                        new ScriptTestCase(engine, sdo, ContextValueConverter.convertValue(methodName, String.class))));
    }

    private static String getScriptPath(Class<? extends BaseScriptTest> testClass) {
        var annotation = testClass.getAnnotation(TestScript.class);

        return annotation.script();
    }

    /**
     * Тестовый кейс из скрипта
     */
    @Value
    public static class ScriptTestCase {
        ScriptEngine engine;
        ScriptDrivenObject sdo;
        String testName;

        public ScriptTestCase(ScriptEngine engine, ScriptDrivenObject sdo, String testName) {
            this.engine = engine;
            this.sdo = sdo;
            this.testName = testName;
        }
    }

    /**
     * Шаблон тестового кейса из скрипта
     */
    @Value
    public static class ScriptTestCaseTemplate implements TestTemplateInvocationContext {
        ScriptTestCase testCase;

        @Override
        public String getDisplayName(int invocationIndex) {
            return getTestCase().getTestName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return List.of(new ScriptParameterResolver(testCase));
        }
    }

    @Value
    @AllArgsConstructor
    private static class ScriptParameterResolver implements ParameterResolver {
        ScriptTestCase testCase;

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
                throws ParameterResolutionException {

            return parameterContext.getParameter().getType().equals(ScriptTestCase.class);
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
                throws ParameterResolutionException {

            if (parameterContext.getParameter().getType().equals(ScriptTestCase.class)) {
                return testCase;
            }

            return null;
        }
    }
}
