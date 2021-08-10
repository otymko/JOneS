package com.github.otymko.jos.exception;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class CompilerExceptionTest {

  @Test
  void testSymbolNotFoundException() {
    var code = "Переменная = А + 1;";
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    assertThatCode(() -> compiler.compile(code, UserScriptContext.class))
      .isInstanceOf(CompilerException.class)
      .hasMessageContaining("Неизвестный символ: А");
  }

  @Test
  void testNativeMethodArguments() {
    var codeOne = "Значение = Тип(\"Строка\", Неопределено)";
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);

    assertThatCode(() -> compiler.compile(codeOne, UserScriptContext.class))
      .isInstanceOf(CompilerException.class)
      .hasMessageContaining("Слишком много фактических параметров");

    var codeTwo = "Значение = Тип()";
    assertThatCode(() -> compiler.compile(codeTwo, UserScriptContext.class))
      .isInstanceOf(CompilerException.class)
      .hasMessageContaining("Недостаточно фактических параметров");
  }

  @Test
  void test() {
    var code = "Значение = 1 f\n" +
      "Значе2 = 1";
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    assertThatCode(() -> compiler.compile(code, UserScriptContext.class))
      .isInstanceOf(CompilerException.class)
      .hasMessageContaining("Ошибка разбора исходного кода в");
  }

}