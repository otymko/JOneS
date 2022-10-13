/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.collection;

import com.github.otymko.jos.TestHelper;
import com.github.otymko.jos.localization.MessageResource;
import com.github.otymko.jos.localization.Resources;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class V8StructureTest {

    private static final String YES_STRING = Resources.getResourceString(MessageResource.DEFAULT_TRUE_PRESENTATION);
    private static final String NO_STRING = Resources.getResourceString(MessageResource.DEFAULT_FALSE_PRESENTATION);

    @Test
    void testSimple() throws Exception {
        var pathToScript = Path.of("src/test/resources/structures/simple.os");
        TestHelper.checkScript(pathToScript, "1");
    }

    @Test
    void testSimpleInsert() throws Exception {
        var pathToScript = Path.of("src/test/resources/structures/insert-without-value.os");
        TestHelper.checkScript(pathToScript, "1");
    }

    @Test
    void testResolveProperty() throws Exception {
        var pathToScript = Path.of("src/test/resources/structures/property-access.os");
        TestHelper.checkScript(pathToScript, "Значение\n" +
                "Новое значение");
    }

    @Test
    void testStress() throws Exception {
        var pathToScript = Path.of("src/test/resources/structures/stress.os");
        TestHelper.checkScript(pathToScript, "3\n" +
                "1\n" +
                "2\n" +
                "Свойство есть: " + YES_STRING + "\n" +
                "2\n" +
                "Свойство есть: " + NO_STRING + "\n" +
                "0");
    }

}