package com.github.otymko.jos.context;

import com.github.otymko.jos.context.value.Value;

public interface RuntimeContextInstance {

  void callMethodScript(int methodId, Value[] arguments);

}
