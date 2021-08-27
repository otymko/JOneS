/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.TestHelper;
import org.junit.jupiter.api.Test;

class V8StructureTest {

  @Test
  void testSimple() throws Exception {
    var code = "Коллекция = Новый Структура;\n" +
      "Коллекция.Вставить(\"Ключ\", Неопределено);\n" +
      "Коллекция[\"Ключ\"] = 1;\n" +
      "Сообщить(Коллекция[\"Ключ\"]);";
    TestHelper.checkCode(code, "1");
  }

  @Test
  void testSimpleInsert() throws Exception {
    var code = "Коллекция = Новый Структура;\n" +
      "Коллекция.Вставить(\"Ключ\");\n" +
      "Коллекция[\"Ключ\"] = 1;\n" +
      "Сообщить(Коллекция[\"Ключ\"]);";
    TestHelper.checkCode(code, "1");
  }

  @Test
  void testResolveProperty() throws Exception {
    var code = "Коллекция = Новый Структура;\n" +
      "Коллекция.Вставить(\"Ключ\", \"Значение\");\n" +
      "Сообщить(Коллекция.Ключ);\n" +
      "\n" +
      "Коллекция.Ключ = \"Новое значение\";\n" +
      "Сообщить(Коллекция.Ключ);";
    TestHelper.checkCode(code, "Значение\r\n" +
      "Новое значение");
  }

  @Test
  void testBase() throws Exception {
    var code = "Структура = Новый Структура;\n" +
      "Структура.Вставить(\"Ключ1\", 1);\n" +
      "Структура.Вставить(\"Ключ2\", 2);\n" +
      "Структура.Вставить(\"Ключ3\", \"ПроизвольнаяСтрока\");\n" +
      "\n" +
      "Сообщить(Структура.Количество());\n" +
      "Сообщить(Структура[\"Ключ1\"]);\n" +
      "\n" +
      "Структура.Удалить(\"Ключ1\");\n" +
      "\n" +
      "Сообщить(Структура[\"Ключ2\"]);\n" +
      "\n" +
      "Значение = Неопределено;\n" +
      "СвойствоЕсть = Структура.Свойство(\"Ключ2\", Значение);\n" +
      "Сообщить(\"Свойство есть: \" + СвойствоЕсть);\n" +
      "Сообщить(Значение);\n" +
      "\n" +
      "СвойствоЕсть = Структура.Свойство(\"Ключ\");\n" +
      "Сообщить(\"Свойство есть: \" + СвойствоЕсть);\n" +
      "\n" +
      "Структура.Очистить();\n" +
      "\n" +
      "Сообщить(Структура.Количество())";
    TestHelper.checkCode(code, "3\r\n" +
      "1\r\n" +
      "2\r\n" +
      "Свойство есть: Да\r\n" +
      "2\r\n" +
      "Свойство есть: Нет\r\n" +
      "0");
  }

}