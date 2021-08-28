/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context;

/**
 * Режим доступа к свойству
 */
public enum PropertyAccessMode {
  /**
   * Чтение и запись
   */
  READ_AND_WRITE,
  /**
   * Только запись
   */
  WRITE_ONLY,
  /**
   * Только чтение
   */
  READ_ONLY
}
