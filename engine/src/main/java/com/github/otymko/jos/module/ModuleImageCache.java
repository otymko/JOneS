/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.module;

import com.github.otymko.jos.compiler.ConstantDefinition;
import com.github.otymko.jos.compiler.MethodDescriptor;
import com.github.otymko.jos.compiler.SymbolAddress;
import com.github.otymko.jos.runtime.machine.Command;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Слепок образа модуля при компиляции
 */
@Data
public class ModuleImageCache {
    private ModuleSource source;
    private int entryPoint = -1;
    private List<Command> code = new ArrayList<>();
    private List<Pair<Integer, Integer>> linesOffset = new ArrayList<>();
    private List<ConstantDefinition> constants = new ArrayList<>();
    private List<VariableInfo> variables = new ArrayList<>();
    private List<MethodDescriptor> methods = new ArrayList<>();
    private List<SymbolAddress> methodRefs = new ArrayList<>();
    private List<SymbolAddress> variableRefs = new ArrayList<>();
}
