/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.sdo;

import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

public class TestUserScriptContext extends ScriptDrivenObject {

  protected TestUserScriptContext(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @Override
  public ContextInfo getContextInfo() {
    return null;
  }

}
