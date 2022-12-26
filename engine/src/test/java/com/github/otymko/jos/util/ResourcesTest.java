/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.util;

import com.github.otymko.jos.core.localization.MessageResource;
import com.github.otymko.jos.core.localization.Resources;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ResourcesTest {

    @Test
    void testKeys() {
        // проверим что ключи одинаковые в двух пропертях
        var propertiesRu = Collections.list(
                ResourceBundle.getBundle("Message", Locale.forLanguageTag("ru")).getKeys());
        var propertiesEn = Collections.list(
                ResourceBundle.getBundle("Message", Locale.forLanguageTag("en")).getKeys());

        checkKeys(propertiesRu, propertiesEn);
        checkKeys(propertiesEn, propertiesRu);
    }

    @SneakyThrows
    @Test
    void testMessageKeys() {
        for (var field : MessageResource.class.getFields()) {
            if (Modifier.isFinal(field.getModifiers())) {
                var value = (String) field.get(null);
                assertThat(Resources.getResourceString(value)).isNotNull().isNotEmpty();
            }
        }
    }

    private static void checkKeys(List<String> values, List<String> values2) {
        var delta = values.stream()
                .filter(Predicate.not(values2::contains))
                .collect(Collectors.toList());
        assertThat(delta).isEmpty();
    }

}