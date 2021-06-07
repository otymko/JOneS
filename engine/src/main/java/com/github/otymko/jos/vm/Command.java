package com.github.otymko.jos.vm;

import lombok.Value;

/**
 * Байткод команда
 */
@Value
public class Command {
  OperationCode code;
  int argument;
}
