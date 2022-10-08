/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.app;

import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.app.exception.ApplicationException;
import com.github.otymko.jos.localization.MessageResource;
import com.github.otymko.jos.localization.Resources;

import java.io.File;
import java.nio.file.Path;

public class ScriptLauncher {

  public static void main(String[] args) throws Exception {

    if (args.length == 0) {
      throw new ApplicationException(Resources.getResourceString(MessageResource.EXPECTED_AT_LEAST_ONE_ARGUMENT));
    }

    final var pathToScriptFromArgument = args[0];
    if (pathToScriptFromArgument.isEmpty()) {
      throw new ApplicationException(Resources.getResourceString(MessageResource.PATH_TO_SCRIPT_IS_NOT_SPECIFIED));
    }

    Path pathToScript;
    var pwd = Path.of("");
    var file = new File(pathToScriptFromArgument);
    if (file.exists()) {
      pathToScript = file.toPath().toAbsolutePath();
    } else {
      var otherPathToScript = Path.of(pwd.toString(), pathToScriptFromArgument);
      if (otherPathToScript.toFile().exists()) {
        throw new ApplicationException(Resources.getResourceString(MessageResource.SCRIPT_FILE_NOT_FOUND));
      }
      pathToScript = otherPathToScript.toAbsolutePath();
    }

    var engine = new ScriptEngine();
    int exitCode = engine.execute(pathToScript);
    System.exit(exitCode);
  }

}
