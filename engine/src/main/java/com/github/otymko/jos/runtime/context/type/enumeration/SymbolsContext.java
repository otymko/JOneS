/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type.enumeration;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.ContextClass;
import com.github.otymko.jos.runtime.context.ContextProperty;
import com.github.otymko.jos.runtime.context.ContextValue;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import com.github.otymko.jos.runtime.machine.info.ContextInfo;

import java.math.BigDecimal;
import java.util.Date;

@ContextClass(name = "Символы", alias = "Chars")
public class SymbolsContext extends ContextValue {
  public static final ContextInfo INFO = ContextInfo.createByClass(SymbolsContext.class);

  @ContextProperty(name = "ПС", alias = "LF")
  public final IValue LF = ValueFactory.create("\n");

  @ContextProperty(name = "ВК", alias = "CR")
  public final IValue CR = ValueFactory.create("\r");

  @ContextProperty(name = "ВТаб", alias = "VTab")
  public final IValue V_TAB = ValueFactory.create("\\v");

  @ContextProperty(name = "Таб", alias = "Tab")
  public final IValue TAB = ValueFactory.create("\t");

  @ContextProperty(name = "ПФ", alias = "FF")
  public final IValue FF = ValueFactory.create("\f");

  @ContextProperty(name = "НПП", alias = "Nbsp")
  public final IValue NBSP = ValueFactory.create("\u00A0");

  public SymbolsContext() {
    // none
  }

  @Override
  public ContextInfo getContextInfo() {
    return INFO;
  }

  @Override
  public BigDecimal asNumber() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public Date asDate() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public boolean asBoolean() {
    throw MachineException.operationNotSupportedException();
  }

  @Override
  public String asString() {
    return INFO.getName();
  }

  @Override
  public DataType getDataType() {
    return DataType.OBJECT;
  }

}
