/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.compiler.ScriptCompiler;
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

  @Test
  void testInternalMachineException() {
    var code = "А = Новый Массив; А.Установить(0, 0);";
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(code, UserScriptContext.class);

    assertThatCode(() -> engine.newObject(moduleImage))
            .isInstanceOf(MachineException.class)
            .hasMessageContaining("Значение индекса выходит за пределы диапазона");
  }

}