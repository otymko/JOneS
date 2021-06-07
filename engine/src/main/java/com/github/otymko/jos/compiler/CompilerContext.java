package com.github.otymko.jos.compiler;

import com.github.otymko.jos.context.ContextInitializer;
import com.github.otymko.jos.context.RuntimeContextInstance;
import com.github.otymko.jos.vm.info.VariableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompilerContext {
  private final int scopeIndexOffset;
  @Getter
  private final List<SymbolScope> scopes = new ArrayList<>();

  public CompilerContext(int scopeIndexOffset) {
    this.scopeIndexOffset = scopeIndexOffset + 1;
  }

  public CompilerContext() {
    scopeIndexOffset = 0;
  }

  public void implementContext(Class<? extends RuntimeContextInstance> targetClass) {
    var methods = ContextInitializer.getContextMethods(targetClass);
    var scope = new SymbolScope();
    var index = 0;
    for (var method : methods) {
      scope.getMethods().add(method);
      scope.getMethodNumbers().put(method.getName().toUpperCase(Locale.ENGLISH), index);
      scope.getMethodNumbers().put(method.getAlias().toUpperCase(Locale.ENGLISH), index);
      index++;
    }
    scopes.add(scope);
  }

  public SymbolAddress getMethodByName(String originalName) {
    var name = originalName.toUpperCase(Locale.ENGLISH);
    SymbolAddress address = null;
    int scopeId = 0;
    // FIXME: перебор снизу стопки
    for (var scope : scopes) {
      if (scope.getMethodNumbers().containsKey(name)) {
        int symbolId = scope.getMethodNumbers().get(name);
        address = new SymbolAddress(symbolId, scopeId + scopeIndexOffset);
        break;
      }
      scopeId++;
    }
    return address;
  }

  public SymbolAddress getVariableByName(String originalName) {
    var name = originalName.toUpperCase(Locale.ENGLISH);
    SymbolAddress address = null;
    for (var scopeId = scopes.size() - 1; scopeId >= 0; scopeId--) {
      var scope = scopes.get(scopeId);
      if (scope.getVariableNumbers().containsKey(name)) {
        int symbolId = scope.getVariableNumbers().get(name);
        address = new SymbolAddress(symbolId, scopeId + scopeIndexOffset);
        break;
      }
    }
    return address;
  }

  public SymbolAddress defineVariable(VariableInfo variable) {
    var indexScope = scopes.size() - 1;
    var lastScope = scopes.get(indexScope);
    lastScope.getVariables().add(variable);
    var index = lastScope.getVariables().size() - 1;
    lastScope.getVariableNumbers().put(variable.getName().toUpperCase(Locale.ENGLISH), index);
    return new SymbolAddress(index, indexScope);
  }

  public void pushScope(SymbolScope scope) {
    scopes.add(scope);
  }

  public void popScope(SymbolScope scope) {
    scopes.remove(scope);
  }

  public int getMaxScopeIndex() {
    return scopes.size() + scopeIndexOffset - 1;
  }

}
