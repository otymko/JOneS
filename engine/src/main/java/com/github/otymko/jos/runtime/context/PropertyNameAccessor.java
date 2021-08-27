/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

public interface PropertyNameAccessor {
  IValue getPropertyValue(IValue index);
  void setPropertyValue(IValue index, IValue value);
  boolean hasProperty(IValue index);
}
