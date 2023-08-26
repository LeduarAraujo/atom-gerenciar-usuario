package com.leduar.atomgerenciarusuario.service;

import com.baeldung.openapi.model.GetUsuarioLogadoResponseRepresentation;
import com.baeldung.openapi.model.SigninUsuarioRequestRepresentation;
import com.baeldung.openapi.model.SigninUsuarioResponseRepresentation;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import com.leduar.atomgerenciarusuario.exceptions.LoginSenhaException;
import com.leduar.atomgerenciarusuario.repository.UsuarioRepository;
import com.leduar.atomgerenciarusuario.utils.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GerenciarUsuarioService {

    private final UsuarioRepository repository;

    public SigninUsuarioResponseRepresentation iniciarSessao(SigninUsuarioRequestRepresentation body) throws LoginSenhaException {
        log.info("=== Tentando iniciar sessão de login do usuario");

        List<UsuarioEntity> response = repository.findUsuarioEntityByLoginAndPassword(body.getLogin(), body.getSenha());

        if (!response.isEmpty()) {
            return SigninUsuarioResponseRepresentation.builder()
                    .tokenJwt(
                            Jwt.gerarToken(response.get(0).getFirstName(), response.get(0).getId())
                    )
                    .build();
        } else {
            throw new LoginSenhaException();
        }
    }

    public GetUsuarioLogadoResponseRepresentation getDadosUsuario(String tokenJwt) {
        Jwt.validateToken(tokenJwt);
        return GetUsuarioLogadoResponseRepresentation.builder().build();
    }
}
