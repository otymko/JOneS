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

}
