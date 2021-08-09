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
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleVisitorTest {

  @Test
  void testParametersInfo() throws Exception {
    var pathToScript = Path.of("src/test/resources/method-params.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);

    assertThat(moduleImage).isNotNull();
    assertThat(moduleImage.getMethods()).hasSize(1);

    var method = moduleImage.getMethods().get(0);
    var signature = method.getSignature();

    assertThat(signature.getParameters()).hasSize(3)
      .anyMatch(info -> info.getName().equals("ПервыйАргумент") && info.isByValue() && !info.hasDefaultValue())
      .anyMatch(info -> info.getName().equals("ВторойАргумент") && !info.isByValue() && !info.hasDefaultValue())
      .anyMatch(info -> info.getName().equals("ТретийАргумент") && !info.isByValue() && info.hasDefaultValue()
        && info.getDefaultValueIndex() >= 0);
  }

  @Test
  void testCheckConstants() throws Exception {
    var pathToScript = Path.of("src/test/resources/check-constants.os");
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    assertThat(moduleImage.getConstants()).hasSize(2);
  }

}
