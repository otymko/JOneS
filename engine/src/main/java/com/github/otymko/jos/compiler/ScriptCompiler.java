/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLTokenizer;
import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.exception.CompilerException;
import com.github.otymko.jos.module.ModuleImage;
import com.github.otymko.jos.module.ModuleImageCache;
import com.github.otymko.jos.module.ModuleSource;
import com.github.otymko.jos.runtime.SymbolType;
import com.github.otymko.jos.runtime.context.global.FileOperationsGlobalContext;
import com.github.otymko.jos.runtime.context.global.StringOperationGlobalContext;
import com.github.otymko.jos.runtime.context.global.NumberOperationsGlobalContext;
import com.github.otymko.jos.runtime.context.global.SystemGlobalContext;
import com.github.otymko.jos.runtime.context.sdo.ScriptDrivenObject;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import com.github.otymko.jos.util.CommonUtils;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Компилятор скриптов.
 */
public class ScriptCompiler {
    /**
     * Движок.
     */
    @Getter
    private final ScriptEngine engine;
    /**
     * Внешний контекст компиляции.
     */
    @Getter
    private final CompilerContext outerContext;
    /**
     * Контекст компиляции модуля.
     */
    @Getter
    private CompilerContext moduleContext;

    private static ModuleImage buildImage(ModuleImageCache cache) {
        return ModuleImage.builder()
                .source(cache.getSource())
                .entryPoint(cache.getEntryPoint())
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

    public ScriptCompiler(ScriptEngine engine) {
        this.engine = engine;
        outerContext = new CompilerContext();
        initContext();
    }

    /**
     * Компилировать модуль скрипта.
     *
     * @param pathToScript Путь к модулю.
     * @param sdoClass Контекстный класс объекта.
     *
     * @throws CompilerException Ошибка компиляции.
     * @throws IOException Ошибка чтения содержимого модуля.
     */
    public ModuleImage compile(Path pathToScript, Class<? extends ScriptDrivenObject> sdoClass) throws CompilerException, IOException {
        moduleContext.implementContext(sdoClass);
        String content;
        content = CommonUtils.getContentFromFile(pathToScript);
        var source = new ModuleSource(pathToScript, content);
        return compileInternal(source);
    }

    /**
     * Компилировать модуль скрипта из содержимого.
     *
     * @param content Содержимое модуля.
     * @param sdoClass Контекстный класс объекта.
     *
     * @throws CompilerException Ошибка компиляции.
     */
    public ModuleImage compile(String content, Class<? extends ScriptDrivenObject> sdoClass) throws CompilerException {
        moduleContext.implementContext(sdoClass);
        var source = new ModuleSource(content);
        return compileInternal(source);
    }

    /**
     * Найти адрес символа метода в контексте по имени.
     *
     * @param name Имя метода.
     */
    public SymbolAddress findMethodInContext(String name) {
        var address = moduleContext.getMethodByName(name);
        if (address == null) {
            address = outerContext.getMethodByName(name);
        }
        return address;
    }

    /**
     * Найти связь символа переменной в контекте по имени.
     *
     * @param name Имя переменной.
     */
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

    /**
     * Получить адрес символа переменной в контесте по имени.
     *
     * @param name Имя переменной.
     */
    public SymbolAddress findVariableInContext(String name) {
        var address = moduleContext.getVariableByName(name);
        if (address == null) {
            address = outerContext.getVariableByName(name);
        }
        return address;
    }

    private void initContext() {
        initOuterContext();
        moduleContext = new CompilerContext(outerContext.getMaxScopeIndex());
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

    private void initOuterContext() {
        addGlobalContext();
        outerContext.implementContext(SystemGlobalContext.class);
        outerContext.implementContext(StringOperationGlobalContext.class);
        outerContext.implementContext(FileOperationsGlobalContext.class);
        outerContext.implementContext(NumberOperationsGlobalContext.class);
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
