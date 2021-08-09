package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.machine.OperationCode;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class NativeGlobalMethod {
  private static final Map<String, OperationCode> methods = new HashMap<>();
  private static final Map<OperationCode, Integer> methodsArgument = new EnumMap<>(OperationCode.class);

  static {
    initNativeMethods();
  }

  private NativeGlobalMethod() {
    // none
  }

  public static Optional<OperationCode> getOperationCode(String name) {
    return Optional.ofNullable(methods.get(name.toUpperCase(Locale.ENGLISH)));
  }

  public static int getArgumentsByOperationCode(OperationCode code) {
    return methodsArgument.getOrDefault(code, 0);
  }

  private static void initNativeMethods() {
    addNativeMethod("Тип", "Type", OperationCode.Type, 1);
    addNativeMethod("ТипЗнч", "TypeOf", OperationCode.ValType, 1);
  }

  private static void addNativeMethod(String name, String alias, OperationCode code, int arguments) {
    methods.put(name.toUpperCase(Locale.ENGLISH), code);
    methods.put(alias.toUpperCase(Locale.ENGLISH), code);
    methodsArgument.put(code, arguments);
  }

}