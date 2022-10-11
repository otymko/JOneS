/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.module;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.nio.file.Path;

/**
 * Источник скрипта
 */
@Value
@RequiredArgsConstructor
public class ModuleSource {
    Path path;
    String content;

    public ModuleSource(String content) {
        this.path = Path.of("");
        this.content = content;
    }

}
