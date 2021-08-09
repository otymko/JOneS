/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.context.ContextType;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.Optional;

public class TypeManager {
  private final TypeStorage storage = new TypeStorage();

  public void registerType(String name, Class<? extends ContextType> implementingClass) {
    storage.getTypes().put(name, implementingClass);
  }

  public Optional<ContextInfo> getContextInfoByName(String name) {
    var type = storage.getTypes().getOrDefault(name, null);
    if (type == null) {
      return Optional.empty();
    }

    // FIXME: долго и больно
    ContextInfo contextInfo;
    try {
      contextInfo = (ContextInfo) type.getField("INFO").get(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
      return Optional.empty();
    }

    return Optional.of(contextInfo);
  }

}
