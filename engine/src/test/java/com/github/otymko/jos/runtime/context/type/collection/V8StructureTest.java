/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.TestHelper;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class V8StructureTest {

  @Test
  void testSimple() throws Exception {
    var pathToScript = Path.of("src/test/resources/structures/simple.os");
    TestHelper.checkScript(pathToScript, "1");
  }

  @Test
  void testSimpleInsert() throws Exception {
    var pathToScript = Path.of("src/test/resources/structures/insert-without-value.os");
    TestHelper.checkScript(pathToScript, "1");
  }

  @Test
  void testResolveProperty() throws Exception {
    var pathToScript = Path.of("src/test/resources/structures/property-access.os");
    TestHelper.checkScript(pathToScript, "Значение\r\n" +
      "Новое значение");
  }

  @Test
  void testStress() throws Exception {
    var pathToScript = Path.of("src/test/resources/structures/stress.os");
    TestHelper.checkScript(pathToScript, "3\r\n" +
      "1\r\n" +
      "2\r\n" +
      "Свойство есть: Да\r\n" +
      "2\r\n" +
      "Свойство есть: Нет\r\n" +
      "0");
  }

  @Test
  void testIterable() throws Exception {
    var pathToScript = Path.of("src/test/resources/structures/iterable.os");
    var model = "Ключ: Ключ1, Значение: Значение 1\r\n" +
      "Ключ: Ключ1, Значение: Значение 1\r\n" +
      "Ключ: Ключ2, Значение: Значение 2\r\n" +
      "Ключ: Ключ2, Значение: Значение 2\r\n" +
      "Ключ: Ключ3, Значение: Значение 3\r\n" +
      "Ключ: Ключ3, Значение: Значение 3";
    TestHelper.checkScript(pathToScript, model);
  }

}