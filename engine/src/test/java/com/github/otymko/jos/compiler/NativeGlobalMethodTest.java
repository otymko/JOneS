/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.common.BaseExternalScriptTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

class NativeGlobalMethodTest extends BaseExternalScriptTest {
  @TestFactory
  Collection<DynamicTest> nativeTests() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/nativeGlobalMethods.os"));
  }

}
