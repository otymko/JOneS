package com.github.otymko.jos.sdo;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
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

}
