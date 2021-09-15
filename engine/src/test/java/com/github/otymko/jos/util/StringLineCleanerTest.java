/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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

    string = "Test";
    string = StringLineCleaner.clean(string);
    assertThat(string).isEqualTo("Test");
  }

  @Test
  void testSingleQuote() {
    var string = "'Test'";
    string = StringLineCleaner.cleanSingleQuote(string);
    assertThat(string).isEqualTo("Test");

    string = "'''Test'''";
    string = StringLineCleaner.cleanSingleQuote(string);
    assertThat(string).isEqualTo("'Test'");

    string = "Test";
    string = StringLineCleaner.cleanSingleQuote(string);
    assertThat(string).isEqualTo("Test");
  }

}
