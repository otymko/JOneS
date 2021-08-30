/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.Optional;

public class TypeManager {
  private final TypeStorage storage = new TypeStorage();

  public void registerType(String name, ContextInfo info) {
    storage.getTypes().put(name, info);
  }

  public Optional<ContextInfo> getContextInfoByName(String name) {
    return Optional.ofNullable(storage.getTypes().getOrDefault(name, null));
  }

}
