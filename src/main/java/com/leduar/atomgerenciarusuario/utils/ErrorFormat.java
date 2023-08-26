package com.leduar.atomgerenciarusuario.utils;


import com.baeldung.openapi.model.ErrorMessageRepresentation;
import com.leduar.atomgerenciarusuario.enums.ExceptionReasonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ErrorFormat {
    public static ResponseEntity<ErrorMessageRepresentation> convertToEntity(Exception ex) {
        log.info("Ocorreu um erro ao tentar executar o processo! {{}}", ex);
        ExceptionReasonEnum reasonEnum = ExceptionReasonEnum.getEnum(ex.getClass());
        return ResponseEntity.status(reasonEnum.getStatusCode())
                .body(ErrorMessageRepresentation.builder()
                        .errorCode(reasonEnum.hashCode())
                        .message(reasonEnum.getDsError())
                        .build());
    }
}
