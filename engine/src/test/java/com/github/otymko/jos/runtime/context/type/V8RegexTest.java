/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.TestHelper;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class V8RegexTest {

  @Test
  void test() throws Exception {
    var script = Path.of("src/test/resources/regex/case.os");
    var model = "Да\n" +
      "Нет\n" +
      "Да";
    TestHelper.checkScript(script, model);
  }

}