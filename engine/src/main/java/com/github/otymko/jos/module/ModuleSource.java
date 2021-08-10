package com.github.otymko.jos.module;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.nio.file.Path;

@Value
@RequiredArgsConstructor
public class ModuleSource {
  Path path;
  String content;

  public ModuleSource(String content) {
    this.path = Path.of("");
    this.content = content;
  }

}
