/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.common;

import com.github.otymko.jos.runtime.RuntimeContext;
import com.github.otymko.jos.runtime.context.type.collection.V8Array;
import com.github.otymko.jos.runtime.context.type.collection.V8Map;
import com.github.otymko.jos.runtime.context.type.collection.V8Structure;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeContextTest {

    @Test
    void testNameAndAlias() throws Exception {
        checkRuntimeContextObject(V8Array.constructor());
        checkRuntimeContextObject(V8Structure.constructor());
        checkRuntimeContextObject((RuntimeContext) V8Map.constructor());
    }

    private void checkMethods(RuntimeContext object) {
        for (final var method : object.getContextInfo().getMethods()) {
            final var idByName = object.findMethodId(method.getName());
            assertThat(idByName).isNotNegative();

            if (method.getAlias() != null) {
                final var idByAlias = object.findMethodId(method.getAlias());
                assertThat(idByAlias).isEqualTo(idByName);
            }
        }
    }

    private void checkProperties(RuntimeContext object) {
        for (final var property : object.getContextInfo().getProperties()) {
            final var idByName = object.findProperty(property.getName());
            assertThat(idByName).isNotNegative();

            if (property.getAlias() != null) {
                final var idByAlias = object.findProperty(property.getAlias());
                assertThat(idByAlias).isEqualTo(idByName);
            }
        }
    }

    private void checkRuntimeContextObject(RuntimeContext object) {
        checkMethods(object);
        checkProperties(object);
    }

}
