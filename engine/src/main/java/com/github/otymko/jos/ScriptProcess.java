package com.github.otymko.jos;

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
