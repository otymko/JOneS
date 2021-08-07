package com.github.otymko.jos.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringLineCleanerTest {

  @Test
  void test() {
    var string = "\"Test\"";
    string = StringLineCleaner.clean(string);
    assertThat(string).isEqualTo("Test");

    string = "\"\"\"Test\"\"\"";
    string = StringLineCleaner.clean(string);
    assertThat(string).isEqualTo("\"Test\"");
  }

}
