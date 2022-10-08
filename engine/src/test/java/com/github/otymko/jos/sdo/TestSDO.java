/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.sdo;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.app.ScriptEngine;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class TestSDO {

  @Test
  void test() throws Exception {
    var pathToScript = Path.of("src/test/resources/custom-sdo.os");

    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, TestUserScriptContext.class);
    var sdo = engine.newObject(moduleImage);

    var methodId = sdo.getScriptMethod("ВывестиЧисло");
    sdo.callScriptMethod(engine, methodId, new IValue[0]);

    methodId = sdo.getScriptMethod("ВывестиЧислоПоЗначению");
    sdo.callScriptMethod(engine, methodId, new IValue[]{ValueFactory.create(42)});
  }

  @Test
  void testCustomKey() throws Exception {
    var pathToScript = Path.of("src/test/resources/sdo/custom-key.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, TestTwoScriptContext.class);
    var sdo = engine.newObject(moduleImage, TestTwoScriptContext.class);
  }
}
