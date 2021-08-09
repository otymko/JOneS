package com.github.otymko.jos;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class ScriptEngineTest {

  @Test
  void testCompileSample() throws Exception {
    var script = Path.of("src/test/resources/sample.os");
    var model = "Проверка 1\r\nПеременнаяБоди";
    TestHelper.checkScript(script, model);
  }

  @Test
  void testTwo() throws Exception {
    var script = Path.of("src/test/resources/draft.os");
    var model = "1814400";
    TestHelper.checkScript(script, model);
  }

  @Test
  void testFunction() throws Exception {
    var script = Path.of("src/test/resources/function.os");
    TestHelper.checkScript(script, "130");
  }

  @Test
  void testSimpleInstanceType() throws Exception {
    var script = Path.of("src/test/resources/array.os");
    TestHelper.checkScript(script, "101");
  }

  @Test
  void testSimpleCall() throws Exception {
    var code = "Сообщить(\"Привет, мир!\");";
    TestHelper.checkCode(code, "Привет, мир!");
  }

  @Test
  void testTransferVariable() throws Exception {
    var script = Path.of("src/test/resources/use-transfer-variable.os");
    TestHelper.checkScript(script, "99");
  }

  @Test
  void testTransferVariableByValue() throws Exception {
    var script = Path.of("src/test/resources/byValuePrimitive.os");
    TestHelper.checkScript(script, "101");
  }

  @Test
  void testMethodWithDefaultValue() throws Exception {
    Path script = Path.of("src/test/resources/method-default-value.os");
    TestHelper.checkScript(script, "151");
  }

  @Test
  void testModuleVar() throws Exception {
    TestHelper.checkScript(Path.of("src/test/resources/module-var.os"), "44");
  }

  @Test
  void testScript() throws Exception {
    var pathToScript = Path.of("src/test/resources/console-script.os");
    TestHelper.checkWithMethod(pathToScript, "ПроверкаПримитива", "1");
    TestHelper.checkWithMethod(pathToScript, "ПроверкаОбъекта", "911");
  }

  @Test
  void testCheckType() throws Exception {
    var code = "Сообщить(Тип(\"Массив\"));\n" +
      "Сообщить(Тип(\"Число\"));";
    TestHelper.checkCode(code, "Массив\r\nЧисло");
  }

}
