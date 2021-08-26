package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.exception.EngineException;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;
import lombok.Getter;

@ContextClass(name = "ИнформацияОбОшибке", alias = "ErrorInfo")
public class ExceptionInfoContext extends ContextValue {

  @Getter
  private final EngineException exception;

  public ExceptionInfoContext(EngineException exc){
    exception = exc;
  }

  @Override
  public ContextInfo getContextInfo() {
    return ContextInfo.createByClass(getClass());
  }
}
