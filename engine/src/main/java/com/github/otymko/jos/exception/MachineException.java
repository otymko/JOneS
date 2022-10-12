/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

import com.github.otymko.jos.localization.Resources;
import com.github.otymko.jos.runtime.machine.StackTraceRecord;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static com.github.otymko.jos.localization.MessageResource.*;


/**
 * Исключение при выполнении в стековой машине
 */
public class MachineException extends EngineException {
    @Getter
    private final ErrorInfo errorInfo;

    private List<StackTraceRecord> stackTrace;

    public MachineException(String message) {
        super(message);

        errorInfo = new ErrorInfo();
        errorInfo.setLine(-1);
    }

    public MachineException(String message, Throwable cause) {
        super(message, cause);
        errorInfo = new ErrorInfo();
        errorInfo.setLine(-1);
    }

    public void setBslStackTrace(List<StackTraceRecord> stackTrace) {
        this.stackTrace = Collections.unmodifiableList(stackTrace);
    }

    public List<StackTraceRecord> getBslStackTrace() {
        if (stackTrace == null) {
            return Collections.emptyList();
        }
        return stackTrace;
    }

    @Override
    public String getMessage() {
        var message = String.format(
                Resources.getResourceString(ERROR_IN_MODULE_TEMPLATE),
                errorInfo.getSource(),
                errorInfo.getLine(),
                super.getMessage()
        );
        if (!errorInfo.getCode().isEmpty()) {
            message += "\n" + errorInfo.getCode();
        }
        return message;
    }

    public String getMessageWithoutCodeFragment() {
        return super.getMessage();
    }

    public static MachineException typeNotSupportedException(String typeName) {
        var message = String.format(Resources.getResourceString(TYPE_NOT_SUPPORTED), typeName);
        return new MachineException(message);
    }

    public static MachineException typeNotRegisteredException(String typeName) {
        var message = String.format(Resources.getResourceString(TYPE_NOT_REGISTERED), typeName);
        return new MachineException(message);
    }

    public static MachineException objectNotSupportAccessByIndexException() {
        var message = Resources.getResourceString(OBJECT_NOT_SUPPORT_ACCESS_BY_INDEX);
        return new MachineException(message);
    }

    public static MachineException constructorNotFoundException(String typeName) {
        var message = String.format(Resources.getResourceString(CONSTRUCTOR_NOT_FOUND), typeName);
        return new MachineException(message);
    }

    public static MachineException objectIsNotContextTypeException() {
        var message = Resources.getResourceString(OBJECT_IS_NOT_CONTEXT_TYPE);
        return new MachineException(message);
    }

    public static MachineException operationNotSupportedException() {
        var message = Resources.getResourceString(OPERATION_NOT_SUPPORTED);
        return new MachineException(message);
    }

    public static MachineException operationNotImplementedException() {
        var message = Resources.getResourceString(OPERATION_NOT_IMPLEMENTED);
        return new MachineException(message);
    }

    public static MachineException divideByZeroException() {
        var message = Resources.getResourceString(DIVIDE_BY_ZERO);
        return new MachineException(message);
    }

    public static MachineException indexValueOutOfRangeException() {
        var message = Resources.getResourceString(INDEX_VALUE_OUT_OF_RANGE);
        return new MachineException(message);
    }

    public static MachineException convertToNumberException() {
        var message = Resources.getResourceString(CONVERT_TO_NUMBER);
        return new MachineException(message);
    }

    public static MachineException convertToBooleanException() {
        var message = Resources.getResourceString(CONVERT_TO_BOOLEAN);
        return new MachineException(message);
    }

    public static MachineException convertToDateException() {
        var message = Resources.getResourceString(CONVERT_TO_DATE);
        return new MachineException(message);
    }

    public static MachineException invalidPropertyNameStructureException(String propertyName) {
        var message = String.format(Resources.getResourceString(INVALID_PROPERTY_NAME_STRUCTURE), propertyName);
        return new MachineException(message);
    }

    public static MachineException wrongStackConditionException() {
        var message = Resources.getResourceString(WRONG_STACK_CONDITION);
        return new MachineException(message);
    }

    public static MachineException getPropertyIsNotReadableException(String propertyName) {
        var message = String.format(Resources.getResourceString(GET_PROPERTY_IS_NOT_READABLE), propertyName);
        return new MachineException(message);
    }

    public static MachineException getPropertyIsNotWritableException(String propertyName) {
        var message = String.format(Resources.getResourceString(GET_PROPERTY_IS_NOT_WRITABLE), propertyName);
        return new MachineException(message);
    }

    public static MachineException indexedValueIsReadable() {
        return new MachineException(Resources.getResourceString(INDEXED_VALUE_IS_READABLE));
    }

    public static MachineException getPropertyNotFoundException(String propertyName) {
        var message = String.format(Resources.getResourceString(GET_PROPERTY_NOT_FOUND), propertyName);
        return new MachineException(message);
    }

    public static MachineException iteratorIsNotDefinedException() {
        var message = Resources.getResourceString(ITERATOR_IS_NOT_DEFINED);
        return new MachineException(message);
    }

    public static MachineException failedToInstantiateSdo() {
        var message = Resources.getResourceString(FAILED_TO_INSTANTIATE_SDO);
        return new MachineException(message);
    }

    public static MachineException invalidArgumentValueException() {
        var message = Resources.getResourceString(INVALID_ARGUMENT_VALUE);
        return new MachineException(message);
    }

    public static MachineException methodCallWrongArgValue(String methodName, int argPosition) {
        var message = Resources.getResourceString(METHOD_CALL_WRONG_ARG_VALUE);
        return new MachineException(message);
    }

    public static MachineException methodNotFoundException(String methodName) {
        var message = String.format(Resources.getResourceString(METHOD_NOT_FOUND), methodName);
        return new MachineException(message);
    }

    public static MachineException checkIsFilledNotSupportedForType(String typeName) {
        var message = String.format(Resources.getResourceString(CHECK_IS_FILLED_NOT_SUPPORTED, typeName));
        return new MachineException(message);
    }

    public static MachineException keyNotFound() {
        var message = Resources.getResourceString(KEY_NOT_FOUND);
        return new MachineException(message);
    }

    public static MachineException fileAccessDenied(String path) {
        var message = String.format(Resources.getResourceString(FILE_ACCESS_DENIED), path);
        return new MachineException(message);
    }

}
