/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.TestHelper;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class V8KeyAndValueTest {

  @Test
  void test() throws Exception {
    var pathToScript = Path.of("src/test/resources/keyAndValue/accessMode.os");
    var model = "Ключ1\n" +
      "Значение1\n" +
      "ОК\n" +
      "ОК";
    TestHelper.checkScript(pathToScript, model);
  }

}