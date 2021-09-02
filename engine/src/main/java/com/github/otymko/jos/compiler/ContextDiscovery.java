/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

import java.util.List;
import java.util.stream.Collectors;

// FIXME: некрасиво, это должно быть частью компоненты typeManager
@UtilityClass
public class ContextDiscovery {
  @Getter(lazy = true)
  private final List<EnumerationContext> enumerationContext = calcSystemEnums();

  public void implementEnumeration(Class<? extends EnumType> enumType) {
    var context = new EnumerationContext(enumType);
    getEnumerationContext().add(context);
  }

  public EnumerationContext getEnumByClass(Class<? extends EnumType> enumClass) {
    return getEnumerationContext().stream()
      .filter(context -> context.getEnumType() == enumClass)
      .findAny()
      .get();
  }

  // fixme: рефлексия это долго
  private List<EnumerationContext> calcSystemEnums() {
    var reflections = new Reflections("com.github.otymko.jos.runtime.context.type.enumeration");
    return reflections.getSubTypesOf(EnumType.class).stream()
      .map(EnumerationContext::new)
      .collect(Collectors.toList());
  }

}
