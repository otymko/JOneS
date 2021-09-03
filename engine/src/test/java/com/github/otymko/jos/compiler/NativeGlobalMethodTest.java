/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.TestHelper;
import org.junit.jupiter.api.Test;

class NativeGlobalMethodTest {

  @Test
  void testUpperCase() throws Exception {
    var code = "Сообщить(ВРег(\"значение\"));";
    var model = "ЗНАЧЕНИЕ";
    TestHelper.checkCode(code, model);
  }

  @Test
  void testLowerCase() throws Exception {
    var code = "Сообщить(НРег(\"ЗнаЧение\"));";
    var model = "значение";
    TestHelper.checkCode(code, model);
  }

  @Test
  void testStringLength() throws Exception {
    var code = "Сообщить(СтрДлина(\"З наЧен $ ие\"));";
    var model = "12";
    TestHelper.checkCode(code, model);
  }

  @Test
  void testLeft() throws Exception {
    var code = "Сообщить(Лев(\"Значение\", 3));";
    TestHelper.checkCode(code, "Зна");

    code = "Сообщить(Лев(\"Значение\", 10));";
    TestHelper.checkCode(code, "Значение");

    code = "Сообщить(Лев(\"Значение\", 0));";
    TestHelper.checkCode(code, "");

    code = "Сообщить(Лев(\"Значение\", -1));";
    TestHelper.checkCode(code, "");
  }

  @Test
  void testRight() throws Exception {
    var code = "Сообщить(Прав(\"Значение\", 3));";
    TestHelper.checkCode(code, "ние");

    code = "Сообщить(Прав(\"Значение\", 10));";
    TestHelper.checkCode(code, "Значение");

    code = "Сообщить(Прав(\"Значение\", 0));";
    TestHelper.checkCode(code, "");

    code = "Сообщить(Прав(\"Значение\", -1));";
    TestHelper.checkCode(code, "");
  }

  @Test
  void testMiddle() throws Exception {
    String code;

    code = "Сообщить(Сред(\"Значение\", 2, 0));";
    TestHelper.checkCode(code, "");

    code = "Сообщить(Сред(\"Значение\", 2));";
    TestHelper.checkCode(code, "начение");

    code = "Сообщить(Сред(\"Значение\", 2, 3));";
    TestHelper.checkCode(code, "нач");

    code = "Сообщить(Сред(\"Значение\", 2, 6));";
    TestHelper.checkCode(code, "начени");

    code = "Сообщить(Сред(\"Значение\", 2, 7));";
    TestHelper.checkCode(code, "начение");

    code = "Сообщить(Сред(\"Значение\", 2, 11));";
    TestHelper.checkCode(code, "начение");
  }

}
