package com.github.otymko.jos;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.context.UserScriptContext;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DraftTest {
  private final ByteArrayOutputStream out = new ByteArrayOutputStream();

  @Test
  void test() throws Exception {
    System.setOut(new PrintStream(out));

    var pathToScript = Path.of("src/test/resources/draft.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    engine.newObject(moduleImage);

    var result = out.toString().trim();
    var example = "10";
    assertThat(result).isEqualTo(example);
  }

}
