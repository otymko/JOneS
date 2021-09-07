/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.common;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextMethod;
import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Контекст тестирования скриптов на .os. На текущий момент умеет только в утверждения
 */
@ContextClass(name = "СкриптТестер", alias = "ScriptTester")
public class ScriptTester implements ContextType, IValue {
  public static ContextInfo INFO = ContextInfo.createByClass(ScriptTester.class);

  @ContextMethod(name = "Проверить", alias = "Check")
  public static void check(IValue conditional, IValue additionalErrorMessage) {
    if (!conditional.asBoolean()) {
      // TODO: локализация
      // TODO: использование additionalErrorMessage
      final var errorMessage = String.format("Переданный параметр (%s) не является Истиной, а хотели, чтобы являлся.",
        conditional.asString());
      throw new MachineException(errorMessage);
    }
  }

  // ПроверитьИстину

  // ПроверитьЛожь

  // ПроверитьРавенствоДатСТочностью2Секунды

  // ПроверитьДату

  @ContextMethod(name = "ПроверитьРавенство", alias = "CheckEquals")
  public static void checkEquals(IValue oneValue, IValue twoValue, IValue additionalErrorMessage) {
    var oneValueRaw = oneValue.getRawValue();
    var twoValueRaw = twoValue.getRawValue();

    if (oneValueRaw.compareTo(twoValueRaw) != 0) {
      // TODO: локализация
      // TODO: использование additionalErrorMessage
      final var errorMessage = String.format("Сравниваемые значения (%s; %s) не равны, а хотели, чтобы были равны.",
        oneValueRaw.asString(), twoValueRaw.asString());
      throw new MachineException(errorMessage);
    }
  }

  @ContextMethod(name = "ПроверитьНеРавенство", alias = "CheckNotEquals")
  public static void checkNotEquals(IValue oneValue, IValue twoValue, IValue additionalErrorMessage) {
    var oneValueRaw = oneValue.getRawValue();
    var twoValueRaw = twoValue.getRawValue();

    if (oneValueRaw.compareTo(twoValueRaw) == 0) {
      // TODO: локализация
      // TODO: использование additionalErrorMessage
      final var errorMessage = String.format("Сравниваемые значения (%s; %s) равны, а хотели, чтобы были не равны.",
        oneValueRaw.asString(), twoValueRaw.asString());
      throw new MachineException(errorMessage);
    }
  }

  // ПроверитьБольше

  // ПроверитьБольшеИлиРавно

  // ПроверитьМеньше

  // ПроверитьМеньшеИлиРавно

  // ПроверитьЗаполненность

  // ПроверитьНеЗаполненность

  // ПроверитьВхождение

  // ПроверитьКодСОшибкой

  // ПроверитьТип

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  @Override
  public BigDecimal asNumber() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public Date asDate() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public boolean asBoolean() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public String asString() {
    return INFO.getName();
  }

  @Override
  public IValue getRawValue() {
    return this;
  }

  @Override
  public DataType getDataType() {
    return DataType.OBJECT;
  }

  @Override
  public int compareTo(IValue o) {
    return 1;
  }

}
