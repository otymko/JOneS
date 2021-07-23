package com.github.otymko.jos.vm;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Байткод команда
 */
@Value
public class Command {
  OperationCode code;
  @Setter
  @NonFinal
  int argument;
}
