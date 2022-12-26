/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github.otymko.jos.CustomEnum;
import com.github.otymko.jos.ScriptEngine;
import com.github.otymko.jos.runtime.context.sdo.UserScriptContext;
import com.github.otymko.jos.runtime.context.type.TypeManager;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EnumerationsTest {
    private static final String IN_LINE_SEPARATOR = "\n";

    @Test
    void testInternalEnum() {
        var code = "Значение = МоеПеречисление.Значение1;\nСообщить(Значение);";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // FIXME: некрасиво
        TypeManager.getInstance().implementEnumeration(CustomEnum.class);

        var engine = new ScriptEngine();
        var compiler = new ScriptCompiler(engine);
        var moduleImage = compiler.compile(code, UserScriptContext.class);
        assertThatCode(() -> engine.newObject(moduleImage)).doesNotThrowAnyException();

        var result = out.toString().trim();
        result = result.replaceAll(System.lineSeparator(), IN_LINE_SEPARATOR);
        assertThat(result).isEqualTo("Значение1");
    }

}