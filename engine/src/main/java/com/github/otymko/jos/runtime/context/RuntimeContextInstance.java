package com.github.otymko.jos.runtime.context;

import com.github.otymko.jos.runtime.type.BaseValue;

public interface RuntimeContextInstance {

  void callMethodScript(int methodId, BaseValue[] arguments);

}
