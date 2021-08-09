package com.github.otymko.jos;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
public class TestHelper {

  public void checkScript(Path pathToScript, String model) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    engine.newObject(moduleImage);
    var result = out.toString().trim();
    assertThat(result).isEqualTo(model);
  }


  public void checkCode(String code, String model) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(code, UserScriptContext.class);
    engine.newObject(moduleImage);
    var result = out.toString().trim();
    assertThat(result).isEqualTo(model);
  }

  public void checkWithMethod(Path pathToScript, String methodName, String model) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    var sdo = engine.newObject(moduleImage);

    var methodId = sdo.getScriptMethod(methodName);
    sdo.callScriptMethod(engine, methodId, new IValue[0]);

    var result = out.toString().trim();
    assertThat(result).isEqualTo(model);
  }

}
