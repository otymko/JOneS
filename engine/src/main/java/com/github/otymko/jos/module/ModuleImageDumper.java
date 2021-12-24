/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.module;

import com.github.otymko.jos.runtime.machine.OperationCode;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.Writer;

@UtilityClass
public class ModuleImageDumper {

  public static void dump(ModuleImage image, Writer writer) throws IOException {
    int offset = 0;
    for (final var opCode : image.getCode()) {
      writer.write(String.format("%6d: %s", offset, opCode));
      if (opCode.getCode() == OperationCode.PushConst
        || opCode.getCode() == OperationCode.ResolveMethodProc
        || opCode.getCode() == OperationCode.ResolveMethodFunc
        || opCode.getCode() == OperationCode.ResolveProp) {
        writer.write(String.format(" ; %s", image.getConstants().get(opCode.getArgument())));
      }
      writer.write('\n');
      offset++;
    }
  }

}
