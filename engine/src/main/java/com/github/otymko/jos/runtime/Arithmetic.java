/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.DataType;
import com.github.otymko.jos.runtime.context.type.ValueFactory;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Date;

@UtilityClass
public class Arithmetic {

  public IValue add(IValue one, IValue two) {
    if (one.getDataType() == DataType.STRING) {
      return ValueFactory.create(one.asString() + two.asString());
    }
    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
      var date = one.asDate();
      var newValue = new Date(date.getTime() + two.asNumber().longValue());
      return ValueFactory.create(newValue);
    }
    return ValueFactory.create(one.asNumber().add(two.asNumber()));
  }

  public IValue sub(IValue one, IValue two) {
    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.NUMBER) {
      // TODO: реализовать сложение даты и числа
      throw MachineException.operationNotImplementedException();
    }

    if (one.getDataType() == DataType.DATE && two.getDataType() == DataType.DATE) {
      // TODO реализовать сложение дат
      throw MachineException.operationNotImplementedException();
    }

    return ValueFactory.create(one.asNumber().subtract(two.asNumber()));
  }

  public IValue mul(IValue one, IValue two) {
    return ValueFactory.create(one.asNumber().multiply(two.asNumber()));
  }

  public IValue div(IValue one, IValue two) {
    if (two.asNumber().equals(BigDecimal.ZERO)) {
      throw MachineException.divideByZeroException();
    }
    // TODO тесты
    return ValueFactory.create(one.asNumber().divide(two.asNumber()));
  }

  public IValue mod(IValue one, IValue two) {
    if (two.asNumber().equals(BigDecimal.ZERO)) {
      throw MachineException.divideByZeroException();
    }
    // TODO тесты
    return ValueFactory.create(one.asNumber().remainder(two.asNumber()));
  }

  public IValue negative(IValue value) {
    return ValueFactory.create(value.asNumber().negate());
  }

}
