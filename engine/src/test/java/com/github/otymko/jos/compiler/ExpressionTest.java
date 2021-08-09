/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionTest {
  private static final String TEMPLATE = "Значение = %s;\nСообщить(Значение);";

  @Test
  void testSimple() throws Exception {
    checkEvalExpression("1 + 2 + 3 - 1", "5");
    checkEvalExpression("1 + (1 + 2 * 3) + 4 / 5", "8.8");
  }

  @Test
  void testSimpleLogic() throws Exception {
    checkEvalExpression("Истина", "Да");
    checkEvalExpression("Не Истина", "Нет");
    checkEvalExpression("Ложь", "Нет");
    checkEvalExpression("Не Ложь", "Да");
    checkEvalExpression("Ложь И Ложь", "Нет");
    checkEvalExpression("Истина И Ложь", "Нет");
    checkEvalExpression("Ложь И Истина", "Нет");
    checkEvalExpression("Истина И Истина", "Да");
    checkEvalExpression("Ложь Или Ложь", "Нет");
    checkEvalExpression("Истина Или Ложь", "Да");
    checkEvalExpression("Ложь Или Истина", "Да");
    checkEvalExpression("Истина И Истина", "Да");
    checkEvalExpression("Истина Или Истина И Истина", "Да");
    checkEvalExpression("Ложь Или Истина И Истина", "Да");
    checkEvalExpression("Ложь Или Ложь И Истина", "Нет");
    checkEvalExpression("Ложь Или Истина И Ложь", "Нет");
    checkEvalExpression("Истина И (Истина Или Ложь)", "Да");
    checkEvalExpression("(Истина Или Ложь) И Истина", "Да");
  }

  @Test
  void testCompare() throws Exception {
    checkEvalExpression("1 > 0", "Да");
    checkEvalExpression("1 < 0", "Нет");
    checkEvalExpression("10 >= 10", "Да");
    checkEvalExpression("10 >= 11", "Нет");
    checkEvalExpression("10 <= 10", "Да");
    checkEvalExpression("10 <= 9", "Нет");
    checkEvalExpression("10 = 10", "Да");
    checkEvalExpression("10 = 9", "Нет");
    checkEvalExpression("10 <> 10", "Нет");
    checkEvalExpression("10 <> 9", "Да");
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
