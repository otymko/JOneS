package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLTokenizer;
import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.compiler.image.ModuleImageCache;
import com.github.otymko.jos.context.ScriptDrivenObject;
import com.github.otymko.jos.context.SystemGlobalContext;
import com.github.otymko.jos.util.Common;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ScriptCompiler {
  @Getter
  private final ScriptEngine engine;
  @Getter
  private final CompilerContext outerContext;
  @Getter
  private CompilerContext moduleContext;

  public ScriptCompiler(ScriptEngine engine) {
    this.engine = engine;
    outerContext = new CompilerContext();
    initContext();
  }

  public ModuleImage compile(Path pathToScript, Class<? extends ScriptDrivenObject> sdoClass) throws Exception {
    moduleContext.implementContext(sdoClass);
    String content;
    try {
      content = Common.getContentFromFile(pathToScript);
    } catch (IOException e) {
      throw new Exception("failed to read the file");
    }
    return compileInternal(content);
  }

  public ModuleImage compile(String content, Class<? extends ScriptDrivenObject> targetClass) throws Exception {
    moduleContext.implementContext(targetClass);
    return compileInternal(content);
  }

  private void initContext() {
    outerContext.implementContext(SystemGlobalContext.class);
    moduleContext = new CompilerContext(outerContext.getMaxScopeIndex());
  }

  public SymbolAddress findMethodInContext(String name) {
    var address = moduleContext.getMethodByName(name);
    if (address == null) {
      address = outerContext.getMethodByName(name);
    }
    return address;
  }

  public SymbolAddress findVariableInContext(String name) {
    var address = moduleContext.getVariableByName(name);
    if (address == null) {
      address = outerContext.getVariableByName(name);
    }
    return address;
  }

  private ModuleImage compileInternal(String content) {
    var imageCache = new ModuleImageCache();
    var tokenizer = new BSLTokenizer(content);
    var ast = tokenizer.getAst();
    var moduleVisitor = new ModuleVisitor(imageCache, this);
    moduleVisitor.visitFile(ast);
    return buildImage(imageCache);
  }

  private static ModuleImage buildImage(ModuleImageCache cache) {
    return ModuleImage.builder()
      .entry(cache.getEntryPoint())
      .code(List.copyOf(cache.getCode()))
      .methods(List.copyOf(cache.getMethods()))
      .variables(List.copyOf(cache.getVariables()))
      .constants(List.copyOf(cache.getConstants()))
      .methodRefs(List.copyOf(cache.getMethodRefs()))
      .variableRefs(List.copyOf(cache.getVariableRefs()))
      .build();
  }

}
