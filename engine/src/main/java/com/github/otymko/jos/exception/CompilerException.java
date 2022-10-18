/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.exception;

import com.github.otymko.jos.core.localization.Resources;

import static com.github.otymko.jos.core.localization.MessageResource.*;

/**
 * Исключение, выбрасываемые при компиляции.
 */
public class CompilerException extends EngineException {

    public CompilerException(String message) {
        super(message);
    }

    public static CompilerException symbolNotFoundException(String identifier) {
        var message = String.format(Resources.getResourceString(SYMBOL_NOT_FOUND), identifier);
        return new CompilerException(message);
    }

    public static CompilerException tooManyMethodArgumentsException() {
        var message = Resources.getResourceString(TOO_MANY_METHOD_ARGUMENTS);
        return new CompilerException(message);
    }

    public static CompilerException tooFewMethodArgumentsException() {
        var message = Resources.getResourceString(TOO_FEW_METHOD_ARGUMENTS);
        return new CompilerException(message);
    }

    public static CompilerException notSupportedException() {
        var message = Resources.getResourceString(NOT_SUPPORTED);
        return new CompilerException(message);
    }

    public static CompilerException notImplementedException(String what) {
        var message = String.format(Resources.getResourceString(NOT_IMPLEMENTED), what);
        return new CompilerException(message);
    }

    public static CompilerException notSupportedExpressionOperatorException(String operator) {
        var message = String.format(Resources.getResourceString(NOT_SUPPORTED_EXPRESSION), operator);
        return new CompilerException(message);
    }

    public static CompilerException methodNotFoundException(String methodName) {
        var message = String.format(Resources.getResourceString(METHOD_NOT_FOUND), methodName);
        return new CompilerException(message);
    }

    public static CompilerException mismatchedRaiseExpressionException() {
        var message = Resources.getResourceString(MISMATCHED_RAISE_EXPRESSION);
        return new CompilerException(message);
    }

    public static CompilerException errorInExpression() {
        var message = Resources.getResourceString(ERROR_IN_EXPRESSION);
        return new CompilerException(message);
    }

    public static CompilerException returnStatementOutsideMethod() {
        var message = Resources.getResourceString(RETURN_STATEMENT_OUTSIDE_METHOD);
        return new CompilerException(message);
    }
}
