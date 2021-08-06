package com.github.otymko.jos;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ScriptEngineTest {

  @Test
  void testCompileSample() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    var pathToScript = Path.of("src/test/resources/sample.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    engine.newObject(moduleImage);

    var result = out.toString().trim();
    var example = "\"Проверка 1\"\r\n\"ПеременнаяБоди\"";
    assertThat(result).isEqualTo(example);
  }

  @Test
  void testTwo() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    var pathToScript = Path.of("src/test/resources/draft.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    engine.newObject(moduleImage);

    var result = out.toString().trim();
    var example = "1814400";
    assertThat(result).isEqualTo(example);
  }

  @Test
  void testFunction() throws Exception {
    check(Path.of("src/test/resources/function.os"), "130");
  }

  @Test
  void testSimpleInstanceType() throws Exception {
    var pathToScript = Path.of("src/test/resources/array.os");
    check(pathToScript, "101");
  }

  @Test
  void testSimpleCall() throws Exception {
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile("Сообщить(\"Привет, мир!\");", UserScriptContext.class);
    engine.newObject(moduleImage);
  }

  @Test
  void testTransferVariable() throws Exception {
    check(Path.of("src/test/resources/use-transfer-variable.os"), "99");
  }

  @Test
  void testTransferVariableByValue() throws Exception {
    check(Path.of("src/test/resources/byValuePrimitive.os"), "101");
  }

  @Test
  void testMethodWithDefaultValue() throws Exception {
    check(Path.of("src/test/resources/method-default-value.os"), "151");
  }

  @Test
  void testModuleVar() throws Exception {
    check(Path.of("src/test/resources/module-var.os"), "44");
  }

  @Test
  void testScript() throws Exception {
    var pathToScript = Path.of("src/test/resources/console-script.os");
    checkWithMethod(pathToScript, "ПроверкаПримитива", "1");
    checkWithMethod(pathToScript, "ПроверкаОбъекта", "911");
  }

  private void check(Path pathToScript, String model) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    engine.newObject(moduleImage);
    var result = out.toString().trim();
    assertThat(result).isEqualTo(model);
  }

  private void checkWithMethod(Path pathToScript, String methodName, String model) throws Exception {
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
