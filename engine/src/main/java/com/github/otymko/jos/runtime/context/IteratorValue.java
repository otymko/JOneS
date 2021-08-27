package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.util.Iterator;

public class IteratorValue extends ContextValue implements Iterable<IValue> {

  private final Iterator<IValue> iterator;

  public IteratorValue(Iterator<IValue> iterator) {
    this.iterator = iterator;
  }

  @Override
  public ContextInfo getContextInfo() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public Iterator<IValue> iterator() {
    return iterator;
  }

}
