/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TypeStorage {
  @Getter
  private final Map<String, ContextInfo> types = new HashMap<>();
}
