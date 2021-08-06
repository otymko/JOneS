package com.github.otymko.jos.compiler;

import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

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
      .anyMatch(info -> info.getName().equals("ТретийАргумент") && !info.isByValue() && info.hasDefaultValue());
  }

}
