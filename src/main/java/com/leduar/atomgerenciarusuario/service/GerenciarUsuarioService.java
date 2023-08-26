package com.leduar.atomgerenciarusuario.service;

import com.baeldung.openapi.model.*;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import com.leduar.atomgerenciarusuario.exceptions.LoginSenhaException;
import com.leduar.atomgerenciarusuario.mapper.UsuarioMapper;
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

    /**
     * @return Listar dados usuários
     */
    public ListUsersResponseRepresentation listarUsuarios() {
        log.info("=== Listando Usuarios da Tabela de Usuario");
        return ListUsersResponseRepresentation.builder()
                .listUser(UsuarioMapper.usuarioEntityToRepresentation(repository.findAll()))
                .build();
    }

    /**
     * @param body
     * @return Token da sessão
     * @throws LoginSenhaException
     */
    public SigninUsuarioResponseRepresentation iniciarSessao(SigninUsuarioRequestRepresentation body) throws LoginSenhaException {
        log.info("=== Tentando iniciar sessão de login do usuario");

        List<UsuarioEntity> response = repository.findUsuarioEntityByLoginAndPassword(body.getLogin(), body.getSenha());

        if (!response.isEmpty()) {
            return SigninUsuarioResponseRepresentation.builder()
                    .tokenJwt(Jwt.gerarToken(response.get(0).getFirstName(), response.get(0).getId()))
                    .build();
        } else {
            throw new LoginSenhaException();
        }
    }

    public SucessMessageRepresentation cadastrarUsuario(DadosUsuarioResponseRepresentation body) {
        log.info("=== Cadastrando Usuário");

        repository.save(UsuarioMapper.usuarioRepresentationToEntity(body));

        return null;
    }










    public GetUsuarioLogadoResponseRepresentation getDadosUsuario(String tokenJwt) {
        Jwt.validateToken(tokenJwt);
        return GetUsuarioLogadoResponseRepresentation.builder().build();
    }
}
