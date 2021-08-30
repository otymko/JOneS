/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.type.enumeration.EnumerationContext;
import com.github.otymko.jos.runtime.context.type.enumeration.MessageStatus;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ContextDiscovery {
  @Getter(lazy = true)
  private final List<EnumerationContext> enumerationContext = getEnumerationContexts();

  private List<EnumerationContext> getEnumerationContexts() {
    List<EnumerationContext> enumerations = new ArrayList<>();
    var context = new EnumerationContext(MessageStatus.class);
    enumerations.add(context);
    return enumerations;
  }
}
