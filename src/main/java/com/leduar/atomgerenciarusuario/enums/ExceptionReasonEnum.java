package com.leduar.atomgerenciarusuario.enums;

import com.leduar.atomgerenciarusuario.exceptions.LoginSenhaException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionReasonEnum {
    LOGIN_INEXISTENTE(LoginSenhaException.class, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid login or password");
    private Class exception;
    private HttpStatus statusCode;
    private String dsError;

    public static ExceptionReasonEnum getEnum(Class param) {
        for (ExceptionReasonEnum valor : values()) {
            if (valor.exception == param) {
                return valor;
            }
        }
        return null;
    }
}
