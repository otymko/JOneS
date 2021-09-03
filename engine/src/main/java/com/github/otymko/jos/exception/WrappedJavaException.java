/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

import com.github.otymko.jos.localization.Resources;

import static com.github.otymko.jos.localization.MessageResource.EXTERNAL_SYSTEM_EXCEPTION;

public class WrappedJavaException extends MachineException {
  public WrappedJavaException(Exception exception) {
    super(Resources.getResourceString(EXTERNAL_SYSTEM_EXCEPTION), exception);
  }
}
