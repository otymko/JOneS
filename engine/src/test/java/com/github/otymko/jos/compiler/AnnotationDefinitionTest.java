/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.app.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationDefinitionTest {

  @Test
  void test() throws IOException {
    var pathToScript = Path.of("src/test/resources/annotation/method.os");
    var engine = new ScriptEngine();
    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);

    checkMethodWithAnnotations(moduleImage);
    checkEmptyMethod(moduleImage, 1, "МетодБезАннотаций");
    checkEmptyMethod(moduleImage, 2, "МетодБезАннотацийНомерДва");
  }

  private static void checkMethodWithAnnotations(ModuleImage moduleImage) {
    var methodInfo = moduleImage.getMethods().get(0).getSignature();
    assertThat(methodInfo.getName()).isEqualTo("Метод");

    assertThat(methodInfo.getAnnotations()).hasSize(4);

    var annotation = methodInfo.getAnnotations()[0];
    assertThat(annotation.getName()).isEqualTo("ПростаяАннотация");
    assertThat(annotation.getParameters()).isEmpty();

    annotation = methodInfo.getAnnotations()[1];
    assertThat(annotation.getName()).isEqualTo("АннотацияСоЗначением");
    assertThat(annotation.getParameters()).hasSize(2)
      .anyMatch(parameter -> getConstantStringValue(moduleImage, parameter).equals("ЗначениеАннотации"))
      .anyMatch(parameter -> getConstantStringValue(moduleImage, parameter).equals("Значение2"))
      .allMatch(parameter -> parameter.getName().equals(AnnotationParameter.UNKNOWN_NAME));

    annotation = methodInfo.getAnnotations()[2];
    assertThat(annotation.getName()).isEqualTo("АннотацияСНесколькимиЗначениями");
    assertThat(annotation.getParameters()).hasSize(2)
      .anyMatch(parameter -> parameter.getName().equals("ПервыйПараметр")
        && getConstantStringValue(moduleImage, parameter).equals("Значение1"))
      .anyMatch(parameter -> parameter.getName().equals("ВторойПараметр")
        && getConstantNumericValue(moduleImage, parameter) == 100f);

    annotation = methodInfo.getAnnotations()[3];
    assertThat(annotation.getName()).isEqualTo("ТипДанных");
    assertThat(annotation.getParameters()).hasSize(1)
      .anyMatch(parameter -> getConstantStringValue(moduleImage, parameter).equals("Строка")
        && parameter.getName().equals(AnnotationParameter.UNKNOWN_NAME));
  }

  private static String getConstantStringValue(ModuleImage image, AnnotationParameter parameter) {
    var constant = image.getConstants().get(parameter.getValueIndex());
    return constant.getValue().asString();
  }

  private static float getConstantNumericValue(ModuleImage image, AnnotationParameter parameter) {
    var constant = image.getConstants().get(parameter.getValueIndex());
    return constant.getValue().asNumber().floatValue();
  }

  private static void checkEmptyMethod(ModuleImage moduleImage, int index, String name) {
    var methodInfo = moduleImage.getMethods().get(index).getSignature();
    assertThat(methodInfo.getName()).isEqualTo(name);
    assertThat(methodInfo.getAnnotations()).isEmpty();
  }
}