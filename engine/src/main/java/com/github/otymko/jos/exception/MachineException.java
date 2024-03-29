/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

import com.github.otymko.jos.core.exception.ErrorPositionInfo;
import com.github.otymko.jos.core.localization.Resources;
import com.github.otymko.jos.runtime.machine.StackTraceRecord;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static com.github.otymko.jos.core.localization.MessageResource.*;
import static com.github.otymko.jos.core.localization.Resources.getResourceString;


/**
 * Исключение при выполнении в стековой машине
 */
public class MachineException extends EngineException {
    @Getter
    private final ErrorPositionInfo errorPositionInfo;
    /**
     * Кратное описание ошибки.
     */
    @Getter
    private final String description;

    private List<StackTraceRecord> stackTrace;

    public MachineException(String message) {
        super(message);

        description = message;
        errorPositionInfo = new ErrorPositionInfo();
        errorPositionInfo.setLine(-1);
    }

    public MachineException(String message, Throwable cause) {
        super(message, cause);

        description = message;
        errorPositionInfo = new ErrorPositionInfo();
        errorPositionInfo.setLine(-1);
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
                getResourceString(ERROR_IN_MODULE_TEMPLATE),
                errorPositionInfo.getSource(),
                errorPositionInfo.getLine(),
                super.getMessage()
        );
        if (!errorPositionInfo.getCode().isEmpty()) {
            message += "\n" + errorPositionInfo.getCode();
        }
        return message;
    }

    public String getMessageWithoutCodeFragment() {
        return super.getMessage();
    }

    public static MachineException typeNotSupportedException(String typeName) {
        var message = String.format(getResourceString(TYPE_NOT_SUPPORTED), typeName);
        return new MachineException(message);
    }

    public static MachineException typeNotRegisteredException(String typeName) {
        var message = String.format(getResourceString(TYPE_NOT_REGISTERED), typeName);
        return new MachineException(message);
    }

    public static MachineException objectNotSupportAccessByIndexException() {
        var message = getResourceString(OBJECT_NOT_SUPPORT_ACCESS_BY_INDEX);
        return new MachineException(message);
    }

    public static MachineException constructorNotFoundException(String typeName) {
        var message = String.format(getResourceString(CONSTRUCTOR_NOT_FOUND), typeName);
        return new MachineException(message);
    }

    public static MachineException objectIsNotContextTypeException() {
        var message = getResourceString(OBJECT_IS_NOT_CONTEXT_TYPE);
        return new MachineException(message);
    }

    public static MachineException operationNotSupportedException() {
        var message = getResourceString(OPERATION_NOT_SUPPORTED);
        return new MachineException(message);
    }

    public static MachineException operationNotImplementedException() {
        var message = getResourceString(OPERATION_NOT_IMPLEMENTED);
        return new MachineException(message);
    }

    public static MachineException divideByZeroException() {
        var message = getResourceString(DIVIDE_BY_ZERO);
        return new MachineException(message);
    }

    public static MachineException indexValueOutOfRangeException() {
        var message = getResourceString(INDEX_VALUE_OUT_OF_RANGE);
        return new MachineException(message);
    }

    public static MachineException convertToNumberException() {
        var message = getResourceString(CONVERT_TO_NUMBER);
        return new MachineException(message);
    }

    public static MachineException convertToBooleanException() {
        var message = getResourceString(CONVERT_TO_BOOLEAN);
        return new MachineException(message);
    }

    public static MachineException convertToDateException() {
        var message = getResourceString(CONVERT_TO_DATE);
        return new MachineException(message);
    }

    public static MachineException invalidPropertyNameStructureException(String propertyName) {
        var message = String.format(getResourceString(INVALID_PROPERTY_NAME_STRUCTURE), propertyName);
        return new MachineException(message);
    }

    public static MachineException wrongStackConditionException() {
        var message = getResourceString(WRONG_STACK_CONDITION);
        return new MachineException(message);
    }

    public static MachineException getPropertyIsNotReadableException(String propertyName) {
        var message = String.format(getResourceString(GET_PROPERTY_IS_NOT_READABLE), propertyName);
        return new MachineException(message);
    }

    public static MachineException getPropertyIsNotWritableException(String propertyName) {
        var message = String.format(getResourceString(GET_PROPERTY_IS_NOT_WRITABLE), propertyName);
        return new MachineException(message);
    }

    public static MachineException indexedValueIsReadOnly() {
        return new MachineException(getResourceString(INDEXED_VALUE_IS_READ_ONLY));
    }

    public static MachineException getPropertyNotFoundException(String propertyName) {
        var message = String.format(getResourceString(GET_PROPERTY_NOT_FOUND), propertyName);
        return new MachineException(message);
    }

    public static MachineException iteratorIsNotDefinedException() {
        var message = getResourceString(ITERATOR_IS_NOT_DEFINED);
        return new MachineException(message);
    }

    public static MachineException failedToInstantiateSdo() {
        var message = getResourceString(FAILED_TO_INSTANTIATE_SDO);
        return new MachineException(message);
    }

    public static MachineException invalidArgumentValueException() {
        var message = getResourceString(INVALID_ARGUMENT_VALUE);
        return new MachineException(message);
    }

    public static MachineException methodCallWrongArgValue(String methodName, int argPosition) {
        var message = getResourceString(METHOD_CALL_WRONG_ARG_VALUE);
        return new MachineException(message);
    }

    public static MachineException methodNotFoundException(String methodName) {
        var message = String.format(getResourceString(METHOD_NOT_FOUND), methodName);
        return new MachineException(message);
    }

    public static MachineException checkIsFilledNotSupportedForType(String typeName) {
        var message = String.format(getResourceString(CHECK_IS_FILLED_NOT_SUPPORTED, typeName));
        return new MachineException(message);
    }

    public static MachineException keyNotFound() {
        var message = getResourceString(KEY_NOT_FOUND);
        return new MachineException(message);
    }

    public static MachineException fileAccessDenied(String path) {
        var message = String.format(getResourceString(FILE_ACCESS_DENIED), path);
        return new MachineException(message);
    }

    public static MachineException objectFieldIsNotWritable(String key) {
        var message = String.format(getResourceString(OBJECT_FIELD_IS_NOT_WRITABLE), key);

        return new MachineException(message);
    }

    public static MachineException objectFiledNotFound(String key) {
        var message = String.format(getResourceString(OBJECT_FIELD_NOT_FOUND), key);

        return new MachineException(message);
    }

    public static MachineException templateSyntaxErrorAtPosition(int position) {
        var message = String.format(getResourceString(TEMPLATE_SYNTAX_ERROR_AT_POSITION), position);

        return new MachineException(message);
    }

    public static MachineException templateSyntaxErrorAtPositionInvalidSubstitutionNumber(int position) {
        var message = String.format(
                getResourceString(TEMPLATE_SYNTAX_ERROR_AT_POSITION_INVALID_SUBSTITUTION_NUMBER), position);

        return new MachineException(message);
    }

}
