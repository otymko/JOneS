/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

public class WrappedJavaException extends MachineException {
  public WrappedJavaException(Exception exception) {
    super("Внешнее системное исключение", exception.getCause());
  }
}
