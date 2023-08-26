package com.leduar.atomgerenciarusuario.enums;

import com.leduar.atomgerenciarusuario.exceptions.EmailExistenteException;
import com.leduar.atomgerenciarusuario.exceptions.LoginExistenteException;
import com.leduar.atomgerenciarusuario.exceptions.LoginSenhaException;
import com.leduar.atomgerenciarusuario.exceptions.UsuarioNaoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionReasonEnum {
    LOGIN_INEXISTENTE(LoginSenhaException.class, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid login or password"),
    LOGIN_JA_EXISTE_NA_BASE(LoginExistenteException.class, HttpStatus.CONFLICT, "Login already exists"),
    EMAIL_JA_EXISTE_NA_BASE(EmailExistenteException.class, HttpStatus.CONFLICT, "Email already exists"),
    USUARIO_NAO_ENCONTRADO(UsuarioNaoEncontradoException .class, HttpStatus.BAD_REQUEST, "Usuário não encontrado");
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
