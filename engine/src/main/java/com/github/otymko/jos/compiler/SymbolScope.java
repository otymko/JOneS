/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.machine.info.MethodInfo;
import com.github.otymko.jos.runtime.machine.info.VariableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolScope {
    @Getter
    private final Map<String, Integer> variableNumbers = new HashMap<>();
    @Getter
    private final List<VariableInfo> variables = new ArrayList<>();
    @Getter
    private final Map<String, Integer> methodNumbers = new HashMap<>();
    @Getter
    private final List<MethodInfo> methods = new ArrayList<>();
}
