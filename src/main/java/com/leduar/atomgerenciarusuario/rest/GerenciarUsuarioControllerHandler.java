package com.leduar.atomgerenciarusuario.rest;

import com.baeldung.openapi.model.ErrorMessageRepresentation;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GerenciarUsuarioControllerHandler {

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageRepresentation handlerNumberFormatException(final NumberFormatException exception) {
        return createError(exception, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageRepresentation handlerJsonMappingException(final JsonMappingException exception) {
        return createError(exception, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageRepresentation handlerMissingRequestHeaderException(final MissingRequestHeaderException exception) {
        return createError(exception, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageRepresentation handlerInvalidFormatException(final InvalidFormatException exception) {
        return createError(exception, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageRepresentation handlerNullPointerException(final NullPointerException exception) {
        return createError(exception, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorMessageRepresentation handlerHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        return createError(exception, HttpStatus.BAD_REQUEST.value());
    }

    private ErrorMessageRepresentation createError(Exception exception, int code) {
        return ErrorMessageRepresentation.builder()
                .errorCode(code)
                .message(exception.getMessage())
                .build();
    }
}
