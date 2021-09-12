/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;

import java.math.BigDecimal;
import java.util.Date;

public class ValueFormatter {

  private static final String[] BOOLEAN_FALSE = {"БЛ", "BF"};
  private static final String[] BOOLEAN_TRUE = {"БИ", "BT"};

  public static String format(IValue value, String formatString) {
    final var params = parseParameters(formatString);
    switch (value.getDataType()) {
      case BOOLEAN: return boolFormat(value.asBoolean(), params);
      case DATE: return dateFormat(value.asDate(), params);
      case STRING: return value.asString();
      case NUMBER: return numberFormat(value.asNumber(), params);
    }
    throw MachineException.operationNotSupportedException();
  }

  private static FormatParametersList parseParameters(String format) {
    return new FormatParametersList(format);
  }

  private static String boolFormat(boolean value, FormatParametersList params) {
    final var p = params.get( value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
    if (p == null) {
      return ValueFactory.create(value).asString();
    }
    return p;
  }

  private static String dateFormat(Date value, FormatParametersList params) {
    throw MachineException.operationNotImplementedException();
  }

  private static String numberFormat(BigDecimal value, FormatParametersList params) {
    throw MachineException.operationNotImplementedException();
  }

}
