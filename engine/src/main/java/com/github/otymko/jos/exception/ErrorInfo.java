package com.github.otymko.jos.exception;

import lombok.Data;

@Data
public class ErrorInfo {
  private static final String DEFAULT_PATH = "<Исходный код недоступен>";
  private int line;
  private String source = "";
  private String code = "";

  public String getSource() {
    if (source == null || source.isEmpty()) {
      return DEFAULT_PATH;
    }
    return source;
  }
}
