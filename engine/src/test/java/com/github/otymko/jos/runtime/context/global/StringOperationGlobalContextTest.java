/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.global;

import com.github.otymko.jos.TestHelper;
import com.github.otymko.jos.localization.MessageResource;
import com.github.otymko.jos.localization.Resources;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class StringOperationGlobalContextTest {

    private static final String YES_STRING = Resources.getResourceString(MessageResource.DEFAULT_TRUE_PRESENTATION);
    private static final String NO_STRING = Resources.getResourceString(MessageResource.DEFAULT_FALSE_PRESENTATION);

    @Test
    void testFind() throws Exception {
        var script = Path.of("src/test/resources/global/StringOperation/find.os");
        var model = "8\n1\n23";
        TestHelper.checkScript(script, model);
        // TODO: протестировать индекс начала поиска и порядок срабатывания
    }

    @Test
    void testStartsWith() throws Exception {
        var script = Path.of("src/test/resources/global/StringOperation/startsWith.os");
        var model = String.format("%s\n%s\n%s", YES_STRING, NO_STRING, NO_STRING);
        TestHelper.checkScript(script, model);
        // TODO: проверить СтрНачинаетсяС(ГдеИскать, "")
    }

}