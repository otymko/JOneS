/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.common.ScriptTester;
import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import com.github.otymko.jos.sdo.TestUserScriptContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

@ContextClass(name = "ТестСвойств", alias = "TestProperties")
public class TestProperties extends ContextValue {

  private static final ContextInfo INFO = ContextInfo.createByClass(TestProperties.class);

  private static final String METHOD_GET_LIST_ALL_TESTS = "ПолучитьСписокТестов";

  private int property = 0;

  @ContextConstructor
  public static IValue constructor() {
    return new TestProperties();
  }

  @ContextProperty(name = "ТолькоЗапись", alias = "WriteOnly")
  public void setWriteOnlyProperty(IValue value) {
  }

  @ContextProperty(name = "ТолькоЧтение", alias = "ReadOnly")
  public IValue getReadOnly() {
    return ValueFactory.create(42);
  }

  @ContextProperty(name = "свойство", alias = "Property")
  public void setProperty(IValue value) {
    property = value.asNumber().intValue();
  }

  @ContextProperty(name = "СВОЙСТВО", alias = "Property")
  public IValue getProperty() {
    return ValueFactory.create(property);
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }


  @Test
  public void test() throws Exception {
    var pathToScript = Path.of("src/test/resources/tests/properties.os");

    var engine = new ScriptEngine();
    engine.getTypeManager().registerType("ТестСвойств", INFO);

    var compiler = new ScriptCompiler(engine);
    var moduleImage = compiler.compile(pathToScript, TestUserScriptContext.class);
    var sdo = engine.newObject(moduleImage);

    int methodIndex = sdo.getScriptMethod(METHOD_GET_LIST_ALL_TESTS);
    if (methodIndex < 0) {
      throw new IllegalArgumentException("Метод ПолучитьСписокТестов не найден");
    }
    var tests = sdo.callScriptMethod(engine, methodIndex, new IValue[]{new ScriptTester()});
    var rawTests = tests.getRawValue();
    if (!(rawTests instanceof V8Array)) {
      throw new IllegalArgumentException("Метод ПолучитьСписокТестов вернул не массив с именами тестов");
    }

    var testNamesArray = (V8Array)rawTests;
    var testIndex = testNamesArray.count().asNumber().intValue();
    while (testIndex-- > 0) {
      var methodName = testNamesArray.get(ValueFactory.create(testIndex)).asString();
      var testMethodIndex = sdo.getScriptMethod(methodName);
      sdo.callScriptMethod(engine, testMethodIndex, new IValue[0]);
    }
  }
}
