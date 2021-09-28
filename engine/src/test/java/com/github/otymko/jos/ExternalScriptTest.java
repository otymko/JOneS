/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos;

import com.github.otymko.jos.common.BaseExternalScriptTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

class ExternalScriptTest extends BaseExternalScriptTest {

  @TestFactory
  Collection<DynamicTest> testSample() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/sample.os"));
  }

  @TestFactory
  Collection<DynamicTest> testStringFunctions() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/StringFunctions.os"));
  }

  @TestFactory
  Collection<DynamicTest> testArrays() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/arrays.os"));
  }

  @TestFactory
  Collection<DynamicTest> testStructures() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/structures.os"));
  }
  
  @TestFactory
  Collection<DynamicTest> testArithmetic() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/arithmetic.os"));
  }

  @TestFactory
  Collection<DynamicTest> testDateFunctions() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/DateFunctions.os"));
  }

  @TestFactory
  Collection<DynamicTest> testTypeConversions() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/TypeConversions.os"));
  }

  @TestFactory
  Collection<DynamicTest> testValueIsFilled() throws IOException {
    return getTestsFromScript(Path.of("src/test/resources/tests/ValueIsFilled.os"));
  }

}
