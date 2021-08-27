package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ContextClass(name = "ИнформацияОбОшибке", alias = "ErrorInfo")
public class ExceptionInfoContext extends ContextValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(ExceptionInfoContext.class);

  EngineException exception;

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

}
