/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.otymko.jos.compiler;

import com.github._1c_syntax.bsl.parser.BSLParser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Обработка аннотации при компиляции модуля.
 */
@RequiredArgsConstructor
public class AnnotationProcessing {
    private final Compiler compiler;

    private static void fillParameterName(AnnotationParameter.AnnotationParameterBuilder builder,
                                          BSLParser.AnnotationParamContext annotationParameter) {
        var parameterName = annotationParameter.annotationParamName().IDENTIFIER().getText();
        builder.name(parameterName);
    }

    /**
     * Получить определения аннотаций из контекста.
     *
     * @param annotationContexts узел контекста аннотаций.
     */
    public AnnotationDefinition[] getAnnotationsFromContext(List<? extends BSLParser.AnnotationContext> annotationContexts) {
        return annotationContexts.stream()
                .map(this::createAnnotationDefinition)
                .toArray(AnnotationDefinition[]::new);
    }

    private AnnotationDefinition createAnnotationDefinition(BSLParser.AnnotationContext annotationContext) {
        var name = annotationContext.annotationName().getText();
        AnnotationParameter[] parameters = getAnnotationParameters(annotationContext);
        return new AnnotationDefinition(name, parameters);
    }

    private AnnotationParameter[] getAnnotationParameters(BSLParser.AnnotationContext annotationContext) {
        return Optional.ofNullable(annotationContext.annotationParams())
                .map(this::getParametersFromContext)
                .orElse(new AnnotationParameter[0]);
    }

    private AnnotationParameter[] getParametersFromContext(BSLParser.AnnotationParamsContext parameters) {
        return parameters.annotationParam().stream()
                .map(this::createParameter)
                .toArray(AnnotationParameter[]::new);
    }

    private AnnotationParameter createParameter(BSLParser.AnnotationParamContext annotationParameter) {
        var builder = AnnotationParameter.builder();
        var constValue = Optional.ofNullable(annotationParameter.constValue());
        if (constValue.isPresent()) {
            var indexConstant = compiler.getConstantIndexByValue(constValue.get(), true);
            builder.valueIndex(indexConstant);
        } else {
            var identifier = annotationParameter.annotationParamName().IDENTIFIER().getText();
            var indexConstant = compiler.getConstantIndexByIdentifier(identifier);
            builder.valueIndex(indexConstant);
        }
        Optional.ofNullable(annotationParameter.ASSIGN())
                .ifPresent(node -> fillParameterName(builder, annotationParameter));
        return builder.build();
    }
}
