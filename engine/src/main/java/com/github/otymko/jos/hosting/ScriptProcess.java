/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.hosting;

import lombok.SneakyThrows;

import java.nio.file.Path;

public class ScriptProcess {
  private int exitCode = 0;

  public ScriptProcess(Path path) {
  }

  @SneakyThrows
  public void start() {
  }

  private int execute(Path pathToScript) throws Exception {
    return 0;
  }

  public int getExitCode() {
    return exitCode;
  }

}
