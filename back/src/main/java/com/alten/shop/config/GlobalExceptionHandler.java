package com.alten.shop.config;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.alten.shop.dto.error.ApiError;
import com.alten.shop.dto.error.ApiErrorItem;
import com.alten.shop.exception.FunctionalException;
import com.alten.shop.exception.ResourceNotFoundException;
import com.alten.shop.exception.TechnicalException;
import com.alten.shop.exception.ValidationConstraintException;
import com.alten.shop.util.GlobalConstants;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import io.micrometer.tracing.Tracer;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Tracer tracer;
    private final MessageSource messageSource;

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError onMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return ApiError.builder()
                .type(GlobalConstants.URI_MISSING_REQUEST_PARAMETER)
                .title("Missing Request Parameter").detail(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value()).build();
    }



    private String getMessage(String traceId) {
        String message = messageSource.getMessage(GlobalConstants.ERROR_WS_TECHNICAL, new Object[]{traceId}, Locale.FRANCE);
        if (StringUtils.startsWithIgnoreCase(message, "!")) {
            message = "Unexpected technical error has issued. Please provide your administrator with the token "
                    + traceId + " for more details";
        }
        return message;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Message not readable: {}", e.getMessage());
        return ApiError.builder()
                .type(GlobalConstants.URI_MESSAGE_NOT_READABLE)
                .title("Invalid Request Format")
                .detail("The request contains invalid data format. Please verify your input values match the required types.")
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError onConstraintValidationException(
            ConstraintViolationException e) {
        return ApiError.builder()
                .type(GlobalConstants.URI_VALIDATION_CONSTRAINT_VIOLATION)
                .title("Field validation").detail(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(e.getConstraintViolations().stream()
                        .map(violation -> ApiErrorItem.builder()
                                .target(violation.getPropertyPath().toString())
                                .message(violation.getMessage()).build())
                        .collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        return ApiError.builder()
                .type(GlobalConstants.URI_METHOD_ARGUMENT_NOT_VALID)
                .title("Method Argument Not Valid")
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(e.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> ApiErrorItem.builder()
                                .target(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler(ValidationConstraintException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError onValidationConstraintException(
            ValidationConstraintException e) {
        return ApiError.builder()
                .type(GlobalConstants.URI_VALIDATION_CONSTRAINT)
                .title("Constraint Not Valid")
                .detail(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(ResourceNotFoundException e) {
        return ApiError.builder()
                .type(GlobalConstants.URI_RESOURCE_NOT_FOUND_EXCEPTION)
                .title("Resource Not Found")
                .detail(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
    
    @ExceptionHandler(FunctionalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleFunctionalException(FunctionalException e) {
        return ApiError.builder()
                .type(GlobalConstants.URI_FUNCTIONAL_EXCEPTION)
                .title("Functional Error")
                .detail(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler({TechnicalException.class, RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public final ApiError onTechnicalException(Exception e) {
        log.error(e.getMessage(), e);

        String traceId = getTraceId();
        String message = getMessage(traceId);

        return ApiError.builder()
                .traceId(traceId)
                .title("Technical Error")
                .detail(message)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .type(GlobalConstants.URI_UNEXPECTED_TECHNICAL_ERROR)
                .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleResponseStatusException(ResponseStatusException e) {
        log.warn("Access Denied: {}", e.getReason());
        return ApiError.builder()
                .type(GlobalConstants.URI_ACCESS_DENIED)
                .title("Access Denied")
                .detail(e.getReason())
                .status(HttpStatus.FORBIDDEN.value())
                .build();
    }

    private String getTraceId() {
        return Optional.ofNullable(tracer.currentSpan())
                       .map(span -> span.context().traceId())
                       .orElse(null);
    }
}