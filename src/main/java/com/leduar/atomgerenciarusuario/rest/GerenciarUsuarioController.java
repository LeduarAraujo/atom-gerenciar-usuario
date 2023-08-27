package com.leduar.atomgerenciarusuario.rest;

import com.baeldung.openapi.api.ApiApi;
import com.baeldung.openapi.model.*;
import com.leduar.atomgerenciarusuario.service.GerenciarUsuarioService;
import com.leduar.atomgerenciarusuario.utils.ErrorFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GerenciarUsuarioController implements ApiApi {

    private final GerenciarUsuarioService service;

    @Override
    public ResponseEntity<ListUsersResponseRepresentation> listUsers() {
        try {
            return ResponseEntity.ok().body(service.listarUsuarios());
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> cadUsers(DadosUsuarioResponseRepresentation body) {
        try {
            return ResponseEntity.ok().body(service.cadastrarUsuario(body));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<DadosUsuarioResponseRepresentation> buscarUsuario(Long id) {
        try {
            return ResponseEntity.ok().body(service.consultaUsuario(id));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> removerUsuario(Long id) {
        try {
            return ResponseEntity.ok().body(service.removerUsuario(id));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> atualizarUsuario(Long id, DadosUsuarioResponseRepresentation body) {
        try {
            return ResponseEntity.ok().body(service.atualizarUsuario(id, body));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SigninUsuarioResponseRepresentation> signin(SigninUsuarioRequestRepresentation signinUsuarioRequestRepresentation) {
        try {
            return ResponseEntity.ok().body(service.iniciarSessao(signinUsuarioRequestRepresentation));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }




























    /*
    * - Endpoints que requer Autenticação
    */

    @Override
    public ResponseEntity<GetUsuarioLogadoResponseRepresentation> getUsuarioLogado(String tokenJwt) {
        try {
            return ResponseEntity.ok().body(service.getDadosUsuario(tokenJwt));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }
}
