/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class ScriptEngineTest {

    @Test
    void testCompileSample() throws Exception {
        var script = Path.of("src/test/resources/sample.os");
        var model = "Проверка 1\nПеременнаяБоди";
        TestHelper.checkScript(script, model);
    }

    @Test
    void testTwo() throws Exception {
        var script = Path.of("src/test/resources/draft.os");
        var model = "1814400";
        TestHelper.checkScript(script, model);
    }

    @Test
    void testFunction() throws Exception {
        var script = Path.of("src/test/resources/function.os");
        TestHelper.checkScript(script, "130");
    }

    @Test
    void testSimpleInstanceType() throws Exception {
        var script = Path.of("src/test/resources/array.os");
        TestHelper.checkScript(script, "101");
    }

    @Test
    void testSimpleCall() throws Exception {
        var code = "Сообщить(\"Привет, мир!\");";
        TestHelper.checkCode(code, "Привет, мир!");
    }

    @Test
    void testTransferVariable() throws Exception {
        var script = Path.of("src/test/resources/use-transfer-variable.os");
        TestHelper.checkScript(script, "99");
    }

    @Test
    void testTransferVariableByValue() throws Exception {
        var script = Path.of("src/test/resources/byValuePrimitive.os");
        TestHelper.checkScript(script, "101");
    }

    @Test
    void testMethodWithDefaultValue() throws Exception {
        Path script = Path.of("src/test/resources/method-default-value.os");
        TestHelper.checkScript(script, "151");
    }

    @Test
    void testModuleVar() throws Exception {
        TestHelper.checkScript(Path.of("src/test/resources/module-var.os"), "44");
    }

    @Test
    void testScript() throws Exception {
        var pathToScript = Path.of("src/test/resources/console-script.os");
        TestHelper.checkWithMethod(pathToScript, "ПроверкаПримитива", "1");
        TestHelper.checkWithMethod(pathToScript, "ПроверкаОбъекта", "911");
    }

    @Test
    void testCheckType() throws Exception {
        var code = "Сообщить(Тип(\"Массив\"));\n" +
                "Сообщить(Тип(\"Число\"));";
        TestHelper.checkCode(code, "Массив\nЧисло");
    }

    @Test
    void testTypeOf() throws Exception {
        TestHelper.checkCode("Сообщить(ТипЗнч(1));", "Число");
        TestHelper.checkCode("Сообщить(ТипЗнч(\"Простая строка\"));", "Строка");
        TestHelper.checkCode("Сообщить(ТипЗнч(Новый Массив));", "Массив");
    }

    @Test
    void testExceptionsBehavior() throws Exception {
        // TODO: проверить вложенные попытки с исключениями
        var script = Path.of("src/test/resources/exceptions.os");
        TestHelper.checkScript(script, "РучноеИсключение\nНеВызывайМеня");
    }

    @Test
    void testSimpleReference() throws Exception {
        Path script = Path.of("src/test/resources/test-var-reference.os");
        TestHelper.checkScript(script, "101");
    }

    @Test
    void testSimpleDereference() throws Exception {
        // https://github.com/otymko/JOneS/issues/142
        Path script = Path.of("src/test/resources/test-var-dereference.os");
        TestHelper.checkScript(script, "1");
    }

    @Test
    void testEratosfen() throws Exception {
        Path script = Path.of("src/test/resources/Eratosfen.os");
        TestHelper.checkScriptWithoutException(script);
    }

    @Test
    void testEnum() throws Exception {
        var script = Path.of("src/test/resources/global-enum.os");
        TestHelper.checkScript(script, "ПеречислениеСтатусСообщения\nВажное");
    }

    @Test
    void testMessageShow() throws Exception {
        var script = Path.of("src/test/resources/message-show.os");
        TestHelper.checkScript(script, "Текст?\n" +
                "Важное: Текст?\n" +
                "Внимание: Текст?\n" +
                "Информация: Текст?\n" +
                "Текст?\n" +
                "ОченьВажное: Текст?");
    }

    @Test
    void testTernaryOperator() throws Exception {
        TestHelper.checkCode("Значение = ?(Истина, \"Значение1\", \"Значение2\"); Сообщить(Значение);",
                "Значение1");
        TestHelper.checkCode("Значение = ?(Ложь, \"Значение1\", \"Значение2\"); Сообщить(Значение);",
                "Значение2");
    }

    @Test
    void testEnumsFromGlobalContext() throws Exception {
        var script = Path.of("src/test/resources/global-variable.os");
        TestHelper.checkScript(script, "");
    }

}
