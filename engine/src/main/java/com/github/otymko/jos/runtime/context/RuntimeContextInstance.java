package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.runtime.IValue;

public interface RuntimeContextInstance {

  void callMethodScript(int methodId, IValue[] arguments);

}
