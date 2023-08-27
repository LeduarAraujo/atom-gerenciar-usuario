package com.leduar.atomgerenciarusuario.service;

import com.baeldung.openapi.model.*;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import com.leduar.atomgerenciarusuario.exceptions.EmailExistenteException;
import com.leduar.atomgerenciarusuario.exceptions.LoginExistenteException;
import com.leduar.atomgerenciarusuario.exceptions.LoginSenhaException;
import com.leduar.atomgerenciarusuario.exceptions.UsuarioNaoEncontradoException;
import com.leduar.atomgerenciarusuario.mapper.UsuarioMapper;
import com.leduar.atomgerenciarusuario.repository.UsuarioRepository;
import com.leduar.atomgerenciarusuario.utils.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (!repository.findUsuarioEntityByLogin(body.getLogin()).isEmpty()) {
            throw new LoginExistenteException();
        }

        if (!repository.findUsuarioEntityByEmail(body.getEmail()).isEmpty()) {
            throw new EmailExistenteException();
        }
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
        this.validarUpdate(id, body);
        Optional<UsuarioEntity> response = repository.findById(id);

        if (response.isPresent()){
            repository.save(UsuarioMapper.montarUsuarioAtualizado(response.get(), body));
            return SucessMessageRepresentation.builder()
                    .message("Removido com sucesso")
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
        if (!repository.findUsuarioEntityByLoginAndIdIsNot(body.getLogin(), id).isEmpty()) {
            throw new LoginExistenteException();
        }

        if (!repository.findUsuarioEntityByEmailAndIdIsNot(body.getEmail(), id).isEmpty()) {
            throw new EmailExistenteException();
        }
    }
































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

    public GetUsuarioLogadoResponseRepresentation getDadosUsuario(String tokenJwt) {
        Jwt.validateToken(tokenJwt);
        return GetUsuarioLogadoResponseRepresentation.builder().build();
    }



}
