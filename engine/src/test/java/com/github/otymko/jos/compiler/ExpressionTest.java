/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.localization.MessageResource;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionTest {
    private static final String TEMPLATE = "Значение = %s;\nСообщить(Значение);";
    private static final String YES_STRING = Resources.getResourceString(MessageResource.DEFAULT_TRUE_PRESENTATION);
    private static final String NO_STRING = Resources.getResourceString(MessageResource.DEFAULT_FALSE_PRESENTATION);

    @Test
    void testSimple() throws Exception {
        checkEvalExpression("1 + 2 + 3 - 1", "5");
        checkEvalExpression("1 + (1 + 2 * 3) + 4 / 5", "8.8");
    }

    @Test
    void testSimpleLogic() throws Exception {
        checkEvalExpression("Истина", YES_STRING);
        checkEvalExpression("Не Истина", NO_STRING);
        checkEvalExpression("Ложь", NO_STRING);
        checkEvalExpression("Не Ложь", YES_STRING);
        checkEvalExpression("Ложь И Ложь", NO_STRING);
        checkEvalExpression("Истина И Ложь", NO_STRING);
        checkEvalExpression("Ложь И Истина", NO_STRING);
        checkEvalExpression("Истина И Истина", YES_STRING);
        checkEvalExpression("Ложь Или Ложь", NO_STRING);
        checkEvalExpression("Истина Или Ложь", YES_STRING);
        checkEvalExpression("Ложь Или Истина", YES_STRING);
        checkEvalExpression("Истина И Истина", YES_STRING);
        checkEvalExpression("Истина Или Истина И Истина", YES_STRING);
        checkEvalExpression("Ложь Или Истина И Истина", YES_STRING);
        checkEvalExpression("Ложь Или Ложь И Истина", NO_STRING);
        checkEvalExpression("Ложь Или Истина И Ложь", NO_STRING);
        checkEvalExpression("Истина И (Истина Или Ложь)", YES_STRING);
        checkEvalExpression("(Истина Или Ложь) И Истина", YES_STRING);
    }

    @Test
    void testCompare() throws Exception {
        checkEvalExpression("1 > 0", YES_STRING);
        checkEvalExpression("1 < 0", NO_STRING);
        checkEvalExpression("10 >= 10", YES_STRING);
        checkEvalExpression("10 >= 11", NO_STRING);
        checkEvalExpression("10 <= 10", YES_STRING);
        checkEvalExpression("10 <= 9", NO_STRING);
        checkEvalExpression("10 = 10", YES_STRING);
        checkEvalExpression("10 = 9", NO_STRING);
        checkEvalExpression("10 <> 10", NO_STRING);
        checkEvalExpression("10 <> 9", YES_STRING);
    }

    @Test
    void test0() throws Exception {
        var code =
                "Значение = 1;\n" +
                        "Сообщить(\"Число \" + Значение);";
        check(code, "Число 1");
    }

    private void checkEvalExpression(String data, String model) throws Exception {
        var code = String.format(TEMPLATE, data);
        check(code, model);
    }

    private void check(String code, String model) throws Exception {
        final ByteArrayOutputStream out = getAttachedOut();
        var engine = new ScriptEngine();
        var compiler = new ScriptCompiler(engine);
        var moduleImage = compiler.compile(code, UserScriptContext.class);
        engine.newObject(moduleImage);
        var result = out.toString().trim();
        assertThat(result).isEqualTo(model);
    }

    private ByteArrayOutputStream getAttachedOut() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        return out;
    }

}
