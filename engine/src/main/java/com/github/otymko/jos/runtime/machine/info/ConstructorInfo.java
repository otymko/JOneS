/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.machine.info;

import lombok.Value;

import java.lang.reflect.Method;

@Value
public class ConstructorInfo {
    ParameterInfo[] parameters;
    Method method;
}
