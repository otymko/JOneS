package com.github.otymko.jos.hosting;

import com.github.otymko.jos.compiler.ScriptCompiler;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.runtime.context.ContextInitializer;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import com.github.otymko.jos.runtime.context.type.StandardTypeInitializer;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.machine.MachineInstance;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class ScriptEngine {
  @Getter
  private final TypeManager typeManager;
  @Getter
  private final MachineInstance machine;

  public ScriptEngine() {
    typeManager = new TypeManager();
    machine = new MachineInstance(this);
    // RuntimeEnvironment
    // SystemGlobalContext
    // CreateProcess
    //    CompilerService
    //    ModuleImage -> LoadedModule
    //    InitProcess
    initialize();
  }

  public int execute(Path pathToScript) throws Exception {
    var compiler = new ScriptCompiler(this);
    var moduleImage = compiler.compile(pathToScript, UserScriptContext.class);
    var scriptObject = newObject(moduleImage);
    return 0;
  }

  private void initialize() {
    StandardTypeInitializer.initialize(typeManager);
    ContextInitializer.initialize(machine);
  }

  public ScriptDrivenObject newObject(ModuleImage image) {
    var scriptContext = new UserScriptContext(image); // непонятно как тут хранится состояние
    initializeScriptObject(scriptContext);
    return scriptContext;
  }

  public ScriptDrivenObject newObject(ModuleImage image, Class<? extends ScriptDrivenObject> targetClass) throws Exception {
    ScriptDrivenObject scriptContext;
    try {
      var constructor = targetClass.getConstructor(ModuleImage.class);
      scriptContext = constructor.newInstance(image);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new Exception("Не удалось создай экземпляр SDO");
    }
    getMachine().implementContext(scriptContext);
    initializeScriptObject(scriptContext);
    return scriptContext;
  }

  private void initializeScriptObject(ScriptDrivenObject sdo) {
    sdo.initialize(this);
  }

}
