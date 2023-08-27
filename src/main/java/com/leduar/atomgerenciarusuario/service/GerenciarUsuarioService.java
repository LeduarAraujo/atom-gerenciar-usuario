package com.leduar.atomgerenciarusuario.service;

import com.baeldung.openapi.model.*;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import com.leduar.atomgerenciarusuario.exceptions.*;
import com.leduar.atomgerenciarusuario.mapper.UsuarioMapper;
import com.leduar.atomgerenciarusuario.repository.UsuarioRepository;
import com.leduar.atomgerenciarusuario.utils.Jwt;
import com.leduar.atomgerenciarusuario.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public SucessMessageRepresentation cadastrarUsuario(DadosUsuarioResponseRepresentation body) throws Exception {
        log.info("=== Cadastrando Usuário");

        this.validarCadastro(body);
        repository.save(UsuarioMapper.usuarioRepresentationToEntity(body));

        return SucessMessageRepresentation.builder()
                .message("Cadastrado com sucesso")
                .code(0)
                .build();
    }

    /**
     *  Tem a função de validar se o login ou o email ja conta na base de dados
     * @param body
     * @throws Exception
     */
    private void validarCadastro(DadosUsuarioResponseRepresentation body) throws Exception {
        this.validarCampos(body);

        if (!repository.findUsuarioEntityByLogin(body.getLogin()).isEmpty()) {
            throw new LoginExistenteException();
        }

        if (!repository.findUsuarioEntityByEmail(body.getEmail()).isEmpty()) {
            throw new EmailExistenteException();
        }
    }

    private void validarCampos(DadosUsuarioResponseRepresentation body) throws CampoVazioException {
        if (body == null ||
                !StringUtils.validarString(body.getEmail()) ||
                !StringUtils.validarString(body.getLogin()) ||
                !StringUtils.validarString(body.getFirstName()) ||
                !StringUtils.validarString(body.getBirthday()) ||
                !StringUtils.validarString(body.getPassword()) ||
                !StringUtils.validarString(body.getPhone()) ||
                !StringUtils.validarString(body.getLastName())
        )
            throw new CampoVazioException();
    }

    public DadosUsuarioResponseRepresentation consultaUsuario(Long id) throws Exception {
        log.info("=== Consultar usuário");
        Optional<UsuarioEntity> response = repository.findById(id);

        if (response.isPresent())
            return UsuarioMapper.consultarUsuarioEntityToRepresentation(response.get());

        throw new UsuarioNaoEncontradoException();
    }

    public SucessMessageRepresentation removerUsuario(Long id) throws Exception {
        log.info("=== Remover usuário");
        this.consultaUsuario(id);
        repository.deleteById(id);

        return SucessMessageRepresentation.builder()
                .message("Removido com sucesso")
                .code(0)
                .build();
    }

    public SucessMessageRepresentation atualizarUsuario(Long id, DadosUsuarioResponseRepresentation body) throws Exception {
        log.info("=== Atualizar usuário");
        Optional<UsuarioEntity> response = repository.findById(id);

        if (response.isPresent()){
            this.validarUpdate(id, body);

            repository.save(UsuarioMapper.montarUsuarioAtualizado(response.get(), body));
            return SucessMessageRepresentation.builder()
                    .message("Usuario atualizado com sucesso")
                    .code(0)
                    .build();
        }

        throw new UsuarioNaoEncontradoException();
    }

    /**
     *
     * Tem a função de validar se o login ou o email ja conta na base de dados
     *
     * @param id
     * @param body
     * @throws Exception
     */
    private void validarUpdate(Long id, DadosUsuarioResponseRepresentation body) throws Exception {
        this.validarCampos(body);

        if (!repository.findUsuarioEntityByLoginAndIdNot(body.getLogin(), id).isEmpty()) {
            throw new LoginExistenteException();
        }

        if (!repository.findUsuarioEntityByEmailAndIdNot(body.getEmail(), id).isEmpty()) {
            throw new EmailExistenteException();
        }
    }

    public SigninUsuarioResponseRepresentation iniciarSessao(SigninUsuarioRequestRepresentation body) throws LoginSenhaException {
        log.info("=== Tentando iniciar sessão de login do usuario");
        Optional<UsuarioEntity> optional = repository.findUsuarioEntityByLoginAndPassword(body.getLogin(), body.getSenha());

        if (!optional.isEmpty()) {
            UsuarioEntity response = optional.get();
            response.setLastLogin(LocalDateTime.now());
            UsuarioEntity usuario = repository.save(response);

            return SigninUsuarioResponseRepresentation.builder()
                    .tokenJwt(Jwt.gerarToken(usuario.getFirstName(), usuario.getId()))
                    .build();
        } else {
            throw new LoginSenhaException();
        }
    }


    /*
     * - Endpoints que requer Autenticação
     */


    public GetUsuarioLogadoResponseRepresentation getDadosUsuario(String tokenJwt) throws Exception {
        Jws<Claims> sessao = Jwt.validateToken(tokenJwt);
        return UsuarioMapper.getUsuarioLogado(repository.
                findById(Long.parseLong(
                        sessao.getBody().get("id").toString())
                ).get()
        );
    }
}
