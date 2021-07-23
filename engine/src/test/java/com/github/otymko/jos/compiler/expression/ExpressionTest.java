package com.github.otymko.jos.compiler.expression;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.context.UserScriptContext;
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

  private void checkEvalExpression(String data, String model) throws Exception {
    var code = String.format(TEMPLATE, data);
    var out = getAttachedOut();
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
