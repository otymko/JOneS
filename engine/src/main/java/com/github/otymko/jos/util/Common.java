/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import com.github.otymko.jos.exception.ErrorInfo;
import com.github.otymko.jos.module.ModuleImage;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@UtilityClass
public class Common {

  public String getContentFromFile(Path path) throws IOException {
    return IOUtils.toString(path.toUri(), StandardCharsets.UTF_8);
  }

  public String getAbsolutPath(Path path) {
    return path.normalize().toAbsolutePath().toString();
  }

  public void fillCodePositionInErrorInfo(ErrorInfo errorInfo, ModuleImage image, int numberLine) {
    var source = image.getSource().getContent().split("\n");
    if (source.length >= numberLine) {
      errorInfo.setCode(source[numberLine - 1]);
    }
  }

}
