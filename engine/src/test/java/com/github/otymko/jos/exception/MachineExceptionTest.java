package com.github.otymko.jos.exception;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class MachineExceptionTest {

  @Test
  void testTypeNotRegisteredException() {
    var code = "Тип = Тип(\"Массив1\");";
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(code, UserScriptContext.class);

    assertThatCode(() -> engine.newObject(moduleImage))
      .isInstanceOf(MachineException.class)
      .hasMessageContaining("Тип не зарегистрирован (Массив1)");
  }

}