/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.machine.OperationCode;
import com.github.otymko.jos.runtime.machine.info.ParameterInfo;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Описание методов платформы, которые выполняются нативно на стековой машине
 */
public class NativeGlobalMethod {
  private static final ParameterInfo REQUIRED_PARAMETER = createParameterInfo(true);
  private static final ParameterInfo OPTIONAL_PARAMETER = createParameterInfo(false);
  private static final Map<String, OperationCode> methods = new HashMap<>();
  private static final Map<OperationCode, ParameterInfo[]> methodParameters = new EnumMap<>(OperationCode.class);

  static {
    initNativeMethods();
  }

  private NativeGlobalMethod() {
    // none
  }

  public static Optional<OperationCode> getOperationCode(String name) {
    return Optional.ofNullable(methods.get(name.toUpperCase(Locale.ENGLISH)));
  }

  public static ParameterInfo[] getMethodParameters(OperationCode code) {
    return methodParameters.get(code);
  }

  private static void initNativeMethods() {
    addNativeMethod("Формат", "Format", OperationCode.Format, REQUIRED_PARAMETER, REQUIRED_PARAMETER);
    addNativeMethod("ТекущаяДата", "CurrentDate", OperationCode.CurrentDate);
    addNativeMethod("Число", "Number", OperationCode.Number, REQUIRED_PARAMETER);
    addNativeMethod("Строка", "String", OperationCode.Str, REQUIRED_PARAMETER);
    addNativeMethod("Тип", "Type", OperationCode.Type, REQUIRED_PARAMETER);
    addNativeMethod("ТипЗнч", "TypeOf", OperationCode.ValType, REQUIRED_PARAMETER);
    addNativeMethod("ОписаниеОшибки", "ErrorDescription", OperationCode.ExceptionDescr);
    addNativeMethod("ИнформацияОбОшибке", "ErrorDescription", OperationCode.ExceptionDescr);
    addNativeMethod("ВРег", "Upper", OperationCode.UCase, REQUIRED_PARAMETER);
    addNativeMethod("НРег", "Lower", OperationCode.LCase, REQUIRED_PARAMETER);
    addNativeMethod("СтрДлина", "StrLen", OperationCode.StrLen, REQUIRED_PARAMETER);
    addNativeMethod("СокрП", "TrimR", OperationCode.TrimR, REQUIRED_PARAMETER);
    addNativeMethod("СокрЛ", "TrimL", OperationCode.TrimL, REQUIRED_PARAMETER);
    addNativeMethod("СокрЛП", "TrimLR", OperationCode.TrimLR, REQUIRED_PARAMETER);
    addNativeMethod("Лев", "Left", OperationCode.Left, REQUIRED_PARAMETER, REQUIRED_PARAMETER);
    addNativeMethod("Прав", "Right", OperationCode.Right, REQUIRED_PARAMETER, REQUIRED_PARAMETER);
    addNativeMethod("Сред", "Mid", OperationCode.Mid, REQUIRED_PARAMETER, REQUIRED_PARAMETER, OPTIONAL_PARAMETER);
    addNativeMethod("ПустаяСтрока", "EmptyStr", OperationCode.EmptyStr, REQUIRED_PARAMETER);
    addNativeMethod("Символ", "Chr", OperationCode.Chr, REQUIRED_PARAMETER);
    addNativeMethod("КодСимвола", "ChrCode", OperationCode.ChrCode, REQUIRED_PARAMETER, OPTIONAL_PARAMETER);
    addNativeMethod("СтрЗаменить", "StrReplace", OperationCode.StrReplace, REQUIRED_PARAMETER, REQUIRED_PARAMETER, REQUIRED_PARAMETER);
    addNativeMethod("ТекущаяДата", "CurrentDate", OperationCode.CurrentDate);
    addNativeMethod("Число", "Number", OperationCode.Number, REQUIRED_PARAMETER);
    addNativeMethod("Строка", "String", OperationCode.Str, REQUIRED_PARAMETER);
  }

  private static void addNativeMethod(String name, String alias, OperationCode code, ParameterInfo... params) {
    methods.put(name.toUpperCase(Locale.ENGLISH), code);
    methods.put(alias.toUpperCase(Locale.ENGLISH), code);
    methodParameters.put(code, params);
  }

  private static ParameterInfo createParameterInfo(boolean required) {
    var builder = ParameterInfo.builder();
    builder.hasDefaultValue(!required);
    return builder.build();
  }

}
