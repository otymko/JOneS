package com.github.otymko.jos.runtime.machine.info;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Определение параметра метода
 */
@Value
@Builder
public class ParameterInfo {
  String name;

  @Builder.Default
  boolean byValue = false;

  @Builder.Default
  @Accessors(fluent = true)
  boolean hasDefaultValue = false;

  // DefaultValueIndex ??

  // todo: Аннотации ?

}
