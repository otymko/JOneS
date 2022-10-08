/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.runtime.context.type;

import com.github.otymko.jos.exception.MachineException;
import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.context.IValue;
import com.github.otymko.jos.runtime.context.type.primitive.BooleanValue;
import com.github.otymko.jos.runtime.context.type.primitive.DateValue;
import com.github.otymko.jos.runtime.context.type.primitive.NullValue;
import com.github.otymko.jos.runtime.context.type.primitive.NumberValue;
import com.github.otymko.jos.runtime.context.type.primitive.StringValue;
import lombok.experimental.UtilityClass;

import static com.github.otymko.jos.localization.MessageResource.PRIMITIVE_DATA_TYPE_NOT_SUPPORTED;

@UtilityClass
public class ValueParser {

    public IValue parse(String view, DataType dataType) {
        IValue result;
        switch (dataType) {
            case BOOLEAN:
                result = BooleanValue.parse(view);
                break;
            case DATE:
                result = DateValue.parse(view);
                break;
            case NUMBER:
                result = NumberValue.parse(view);
                break;
            case STRING:
                result = StringValue.parse(view);
                break;
            case UNDEFINED:
                result = ValueFactory.create();
                break;
            case GENERIC_VALUE:
                result = NullValue.parse(view);
                break;
            default:
                var message = String.format(Resources.getResourceString(PRIMITIVE_DATA_TYPE_NOT_SUPPORTED), dataType);
                throw new MachineException(message);
        }

        return result;
    }

}
