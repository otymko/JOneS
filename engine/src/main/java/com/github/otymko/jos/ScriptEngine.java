package com.github.otymko.jos;

import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.context.*;
import com.github.otymko.jos.context.type.TypeManager;
import com.github.otymko.jos.vm.MachineInstance;
import lombok.Getter;

public class ScriptEngine {
  @Getter
  private final TypeManager typeManager;
  @Getter
  private final MachineInstance machine;

  public ScriptEngine() {
    typeManager = new TypeManager();
    machine = new MachineInstance();
    // RuntimeEnvironment
    // SystemGlobalContext
    // CreateProcess
    //    CompilerService
    //    ModuleImage -> LoadedModule
    //    InitProcess
    initialize();
  }

  private void initialize() {
    StandardTypeInitializer.initialize(typeManager);
    ContextInitializer.initialize(machine);
  }

  public ScriptDrivenObject newObject(ModuleImage image) {
    var scriptContext = new UserScriptContext(image); // не попонятно как тут хранится состояние
    initializeScriptObject(scriptContext);
    return scriptContext;
  }

  private void initializeScriptObject(ScriptDrivenObject sdo) {
    sdo.initialize(this);
  }

}
