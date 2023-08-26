package com.leduar.atomgerenciarusuario.utils;


import com.baeldung.openapi.model.ErrorMessageRepresentation;
import com.leduar.atomgerenciarusuario.enums.ExceptionReasonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ErrorFormat {
    public static ResponseEntity<ErrorMessageRepresentation> convertToEntity(Exception ex) {
        log.info("Ocorreu um erro ao tentar executar o processo! {{}}", ex);
        ExceptionReasonEnum reasonEnum = ExceptionReasonEnum.getEnum(ex.getClass());

        if (reasonEnum != null) {
            return montarBody(reasonEnum.getStatusCode(), reasonEnum.getDsError(), reasonEnum.hashCode());
        } else {
            return montarBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause().getMessage(), ex.hashCode());
        }
    }

    private static ResponseEntity<ErrorMessageRepresentation> montarBody(HttpStatus statusCode, String dsError
            , Integer hashCode) {
        return ResponseEntity.status(statusCode)
                .body(ErrorMessageRepresentation.builder()
                        .errorCode(hashCode)
                        .message(dsError)
                        .build());
    }
}
