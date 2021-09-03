/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

import com.github.otymko.jos.localization.Resources;
import lombok.Data;

import static com.github.otymko.jos.localization.MessageResource.SOURCE_CODE_NOT_AVAILABLE;

@Data
public class ErrorInfo {
  private int line;
  private String source = "";
  private String code = "";

  public String getSource() {
    if (source == null || source.isEmpty()) {
      return Resources.getResourceString(SOURCE_CODE_NOT_AVAILABLE);
    }
    return source;
  }
}
