/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.TestHelper;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.runtime.machine.OperationCode;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class CompilerTest {

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
    assertThat(moduleImage.getConstants()).hasSize(4);
  }

  @Test
  void testWhileLoopCompilation() throws Exception {
    var pathToScript = Path.of("src/test/resources/while-loop.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);

    var code = moduleImage.getCode();
    var line = findCommand(code, OperationCode.LINE_NUM, 0, 2);
    var falseCondition = findCommand(code, OperationCode.JMP_FALSE, line);
    var jmpFalse = code.get(falseCondition);
    assertThat(code.get(jmpFalse.getArgument()).getCode()).isEqualTo(OperationCode.NOP);
    var jump = code.get(jmpFalse.getArgument() - 1);
    assertThat(jump.getCode()).isEqualTo(OperationCode.JMP);
    assertThat(jump.getArgument()).isEqualTo(line);

    var breakCommandIndex = findCommand(code, OperationCode.JMP, falseCondition, jmpFalse.getArgument());
    assertThat(breakCommandIndex).isNotEqualTo(-1);

    var continueCommandIndex = findCommand(code, OperationCode.JMP, falseCondition, line);
    assertThat(continueCommandIndex).isNotEqualTo(-1);
  }

  @Test
  void testCodeBlocks() throws IOException {
    var pathToScript = Path.of("src/test/resources/code-blocks.os");
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    assertThatCode(() -> compiler.compile(pathToScript, UserScriptContext.class)).doesNotThrowAnyException();
  }

  @Test
  void testForEach() throws Exception {
    var pathToScript = Path.of("src/test/resources/foreach.os");
    TestHelper.checkScript(pathToScript, "1\n2\n3");
  }

  @Test
  void testFor() throws Exception {
    var pathToScript = Path.of("src/test/resources/for.os");
    TestHelper.checkScript(pathToScript, "1\n2\n3");
  }

  @Test
  void testSimpleCondition() throws Exception {
    var pathToScript = Path.of("src/test/resources/simple-if.os");
    TestHelper.checkScript(pathToScript, "Истина");
  }

  @Test
  void testConditionWithElse() throws Exception {
    var pathToScript = Path.of("src/test/resources/if-with-else.os");
    TestHelper.checkScript(pathToScript, "Вторая ветка");
  }

  @Test
  void testConditionIfElifElse() throws Exception {
    var pathToScript = Path.of("src/test/resources/if-elif-else.os");
    TestHelper.checkScript(pathToScript, "1.1\n2.2\n3.3");
  }

  @Test
  void testComplexIdentifier() throws Exception {
    var pathToScript = Path.of("src/test/resources/complex-id.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);

    var code = moduleImage.getCode();
    var line = findCommand(code, OperationCode.LINE_NUM, 0, 3);

    var methodCall = findCommand(code, OperationCode.RESOLVE_METHOD_FUNC, line, -1);
    assertThat(methodCall).isNotEqualTo(-1); // Должен быть вызов метода (ПривестиЗначение)

    var resolveProp = findCommand(code, OperationCode.RESOLVE_PROP, line, -1);
    assertThat(resolveProp).isNotEqualTo(-1); // Должно быть обращение к свойству (К)
  }

  private int findCommand(List<Command> commands, OperationCode code, int start) {
    return findCommand(commands, code, start, -1);
  }

  private int findCommand(List<Command> commands, OperationCode code, int start, int arg) {
    for (int i = start; i < commands.size(); i++) {
      if (commands.get(i).getCode() == code) {
        if (arg == -1 || commands.get(i).getArgument() == arg)
          return i;
      }
    }

    return -1;
  }

}
