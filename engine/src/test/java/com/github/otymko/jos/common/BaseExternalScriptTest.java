/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.common;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import org.junit.jupiter.api.DynamicTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Базовый класс тестирования из скриптов на .os
 */
public abstract class BaseExternalScriptTest {
  // https://stackoverflow.com/questions/16273318/transliteration-from-cyrillic-to-latin-icu4j-java
  private static final char[] ABS_CYRILLIC = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
  private static final String[] ABS_LATIN = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
  private static final String METHOD_GET_LIST_ALL_TESTS = "ПолучитьСписокТестов";

  /**
   * @param pathToScript путь к скрипту с тестами
   * @return коллекция динамических тестов, которые вызывают тесты из sdo
   * @throws IOException
   */
  public Collection<DynamicTest> getTestsFromScript(Path pathToScript) throws IOException {
    var engine = new ScriptEngine();
    var testSdo = createTestSDO(engine, pathToScript);
    var rawTests = getAllTestsFromSdo(engine, testSdo);
    return Optional.of(rawTests).stream()
      .map(V8Array.class::cast)
      .flatMap(v8Array -> StreamSupport.stream(v8Array.iterator().spliterator(), false))
      .map(test -> createDynamicTest(engine, testSdo, test))
      .collect(Collectors.toList());
  }

  private static ScriptDrivenObject createTestSDO(ScriptEngine engine, Path pathToScript) throws IOException {
    var compiler = new ScriptCompiler(engine);
    var image = compiler.compile(pathToScript, UserScriptContext.class);
    return engine.newObject(image, UserScriptContext.class);
  }

  private static IValue getAllTestsFromSdo(ScriptEngine engine, ScriptDrivenObject sdo) {
    int methodIndex = sdo.getScriptMethod(METHOD_GET_LIST_ALL_TESTS);
    if (methodIndex < 0) {
      throw new IllegalArgumentException("Метод ПолучитьСписокТестов не найден");
    }
    var tests = sdo.callScriptMethod(engine, methodIndex, new IValue[]{new ScriptTester()});
    var rawTests = tests.getRawValue();
    if (!(rawTests instanceof V8Array)) {
      throw new IllegalArgumentException("Метод ПолучитьСписокТестов вернул не массив с именами тестов");
    }
    return rawTests;
  }

  private static DynamicTest createDynamicTest(ScriptEngine engine, ScriptDrivenObject sdo, IValue test) {
    var methodName = test.getRawValue().asString();
    var methodIndex = sdo.getScriptMethod(methodName);
    return DynamicTest.dynamicTest(
      transliterate(methodName),
      () -> sdo.callScriptMethod(engine, methodIndex, new IValue[0])
    );
  }

  private static String transliterate(String message) {
    var builder = new StringBuilder();
    for (int i = 0; i < message.length(); i++) {
      var currentChar = message.charAt(i);
      if (isNumberChar(currentChar)) {
        builder.append(currentChar);
        continue;
      }
      for (int x = 0; x < ABS_CYRILLIC.length; x++) {
        if (currentChar == ABS_CYRILLIC[x]) {
          builder.append(ABS_LATIN[x]);
        }
      }
    }
    return builder.toString();
  }

  private static boolean isNumberChar(char number) {
    return number >= 48 && number <= 57;
  }

}
