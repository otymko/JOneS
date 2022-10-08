/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@UtilityClass
public class TestHelper {
  private static final String IN_LINE_SEPARATOR = "\n";

  public void checkScript(Path pathToScript, String model) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    engine.newObject(moduleImage);
    var result = out.toString().trim();
    result = result.replaceAll(System.lineSeparator(), IN_LINE_SEPARATOR);
    assertThat(result).isEqualTo(model);
  }

  public void checkScriptWithoutException(Path pathToScript) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    assertThatCode(() -> engine.newObject(moduleImage)).doesNotThrowAnyException();
    var result = out.toString().trim();
    result = result.replaceAll(System.lineSeparator(), IN_LINE_SEPARATOR);
    assertThat(result).isEmpty();
  }

  public void checkCode(String code, String model) throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(code, UserScriptContext.class);
    engine.newObject(moduleImage);
    var result = out.toString().trim();
    result = result.replaceAll(System.lineSeparator(), IN_LINE_SEPARATOR);
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
    result = result.replaceAll(System.lineSeparator(), IN_LINE_SEPARATOR);
    assertThat(result).isEqualTo(model);
  }

}
