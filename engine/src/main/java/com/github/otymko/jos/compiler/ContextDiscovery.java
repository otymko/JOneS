/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.EnumClass;
import com.github.otymko.jos.runtime.context.EnumType;
import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import io.github.classgraph.ClassGraph;
import lombok.Getter;
import lombok.experimental.UtilityClass;

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

  // fixme: рефлексия это долго
  private List<EnumerationContext> calcSystemEnums() {
    try (var scanResult = new ClassGraph()
      .enableAnnotationInfo()
      .acceptPackages("com.github.otymko.jos.runtime.context.type.enumeration")
      .scan()) {

      var classes = scanResult.getAllClasses();
      return classes.stream()
        .filter(classInfo -> classInfo.hasAnnotation(EnumClass.class.getName()))
        .map(classInfo -> classInfo.loadClass(EnumType.class))
        .map(EnumerationContext::new)
        .collect(Collectors.toList());
    }
  }

}
