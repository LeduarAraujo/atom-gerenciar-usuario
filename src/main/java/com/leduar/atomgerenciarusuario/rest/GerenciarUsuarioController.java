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
    public ResponseEntity<ListUsersResponseRepresentation> listarUsuarios() {
        try {
            return ResponseEntity.ok().body(service.listarUsuarios());
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> cadastrarUsuario(DadosUsuarioResponseRepresentation body) {
        try {
            return ResponseEntity.ok().body(service.cadastrarUsuario(body));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<DadosUsuarioResponseRepresentation> buscarUsuario(Long id) {
        try {
            return ResponseEntity.ok().body(service.buscarUsuario(id));
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
    public ResponseEntity<SigninUsuarioResponseRepresentation> iniciarSessao(SigninUsuarioRequestRepresentation signinUsuarioRequestRepresentation) {
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
    public ResponseEntity<GetUsuarioLogadoResponseRepresentation> getDadosUsuarioLogado(String tokenJwt) {
        try {
            return ResponseEntity.ok().body(service.getDadosUsuarioLogado(tokenJwt));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<ListaCarrosUsuarioLogadoResponseRepresentation> listarCarrosUsuarioLogado(String tokenJwt) {
        try {
            return ResponseEntity.ok().body(service.listarCarrosUsuarioLogado(tokenJwt));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> cadastrarCarroUsuarioLogado(String tokenJwt
            , CarRequestRepresentation carRequestRepresentation) {
        try {
            return ResponseEntity.ok().body(service.cadastrarCarroUsuarioLogado(tokenJwt, carRequestRepresentation));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<CarResponseRepresentation> buscarCarrosUsuarioLogado(String tokenJwt, Long idCarro) {
        try {
            return ResponseEntity.ok().body(service.buscarCarrosUsuarioLogado(tokenJwt, idCarro));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> removerCarroUsuarioLogado(String tokenJwt, Long idCarro) {
        try {
            return ResponseEntity.ok().body(service.removerCarroUsuarioLogado(tokenJwt, idCarro));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }

    @Override
    public ResponseEntity<SucessMessageRepresentation> atualizarCarroUsuarioLogado(String tokenJwt, Long idCarro, CarRequestRepresentation carRequestRepresentation) {
        try {
            return ResponseEntity.ok().body(service.atualizarCarroUsuarioLogado(tokenJwt, idCarro, carRequestRepresentation));
        } catch (Exception ex) {
            return (ResponseEntity) ErrorFormat.convertToEntity(ex);
        }
    }
}
