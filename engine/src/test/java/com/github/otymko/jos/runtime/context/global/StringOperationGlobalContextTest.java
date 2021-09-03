/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.TestHelper;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class StringOperationGlobalContextTest {

  @Test
  void test() throws Exception {
    var script = Path.of("src/test/resources/global/StringOperation/case.os");
    var model = "8\n1\n23";
    TestHelper.checkScript(script, model);
    // TODO: протестировать индекс начала поиска и порядок срабатывания
  }

}