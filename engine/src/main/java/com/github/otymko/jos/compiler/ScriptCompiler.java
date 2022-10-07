/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLTokenizer;
import com.github.otymko.jos.exception.CompilerException;
import com.github.otymko.jos.hosting.ScriptEngine;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.module.ModuleImageCache;
import com.github.otymko.jos.module.ModuleSource;
import com.github.otymko.jos.runtime.SymbolType;
import com.github.otymko.jos.runtime.context.global.FileOperationsGlobalContext;
import com.github.otymko.jos.runtime.context.global.StringOperationGlobalContext;
import com.github.otymko.jos.runtime.context.global.SystemGlobalContext;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import com.github.otymko.jos.util.Common;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

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

  public ModuleImage compile(Path pathToScript, Class<? extends ScriptDrivenObject> sdoClass) throws CompilerException, IOException {
    moduleContext.implementContext(sdoClass);
    String content;
    content = Common.getContentFromFile(pathToScript);
    var source = new ModuleSource(pathToScript, content);
    return compileInternal(source);
  }

  public ModuleImage compile(String content, Class<? extends ScriptDrivenObject> targetClass) throws CompilerException {
    moduleContext.implementContext(targetClass);
    var source = new ModuleSource(content);
    return compileInternal(source);
  }

  private void initContext() {
    initOuterContext();
    moduleContext = new CompilerContext(outerContext.getMaxScopeIndex());
  }

  public SymbolAddress findMethodInContext(String name) {
    var address = moduleContext.getMethodByName(name);
    if (address == null) {
      address = outerContext.getMethodByName(name);
    }
    return address;
  }

  public VariableBinding findVariableBindingInContext(String name) {
    var address = moduleContext.getVariableByName(name);
    if (address != null) {
      return new VariableBinding(SymbolType.VARIABLE, address);
    }
    address = outerContext.getVariableByName(name);
    if (address != null) {
      return new VariableBinding(SymbolType.CONTEXT_PROPERTY, address);
    }
    return null;
  }

  public SymbolAddress findVariableInContext(String name) {
    var address = moduleContext.getVariableByName(name);
    if (address == null) {
      address = outerContext.getVariableByName(name);
    }
    return address;
  }

  private ModuleImage compileInternal(ModuleSource source) {
    var imageCache = new ModuleImageCache();
    imageCache.setSource(source);
    var tokenizer = new BSLTokenizer(source.getContent());
    var ast = tokenizer.getAst();
    findError(ast);
    var moduleVisitor = new Compiler(imageCache, this);
    moduleVisitor.visitFile(ast);

    moduleContext.getScopes().clear();

    return buildImage(imageCache);
  }

  private static ModuleImage buildImage(ModuleImageCache cache) {
    return ModuleImage.builder()
      .source(cache.getSource())
      .entry(cache.getEntryPoint())
      .code(List.copyOf(cache.getCode()))
      .methods(List.copyOf(cache.getMethods()))
      .variables(List.copyOf(cache.getVariables()))
      .constants(List.copyOf(cache.getConstants()))
      .methodRefs(List.copyOf(cache.getMethodRefs()))
      .variableRefs(List.copyOf(cache.getVariableRefs()))
      .build();
  }

  private static void findError(BSLParser.FileContext fileContext) {
    var walker = new ParseTreeWalker();
    var listener = new ParseErrorListener();
    walker.walk(listener, fileContext);
  }

  private void initOuterContext() {
    addGlobalContext();
    outerContext.implementContext(SystemGlobalContext.class);
    outerContext.implementContext(StringOperationGlobalContext.class);
    outerContext.implementContext(FileOperationsGlobalContext.class);
  }

  private void addGlobalContext() {
    var scope = getGlobalSymbolScope();
    outerContext.getScopes().add(scope);
  }

  private SymbolScope getGlobalSymbolScope() {
    var scope = new SymbolScope();
    var contexts = TypeManager.getInstance().getEnumerationContext();
    for (var context : contexts) {
      var variableInfo = new VariableInfo(context.getContextInfo().getName(),
        context.getContextInfo().getAlias(), SymbolType.CONTEXT_PROPERTY);
      scope.getVariables().add(variableInfo);
      var index = scope.getVariables().indexOf(variableInfo);
      scope.getVariableNumbers().put(variableInfo.getName().toUpperCase(Locale.ENGLISH), index);
      scope.getVariableNumbers().put(variableInfo.getAlias().toUpperCase(Locale.ENGLISH), index);
    }
    return scope;
  }

}
