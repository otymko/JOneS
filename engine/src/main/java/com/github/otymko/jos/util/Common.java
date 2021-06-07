package com.github.otymko.jos.util;

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

}
