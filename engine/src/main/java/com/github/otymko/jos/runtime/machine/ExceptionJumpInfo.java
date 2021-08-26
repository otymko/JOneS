package com.github.otymko.jos.runtime.machine;

import lombok.Data;

@Data
class ExceptionJumpInfo {
  private int handlerAddress;
  private ExecutionFrame handlerFrame;
  private int stackSize;
}
