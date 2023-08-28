package com.leduar.atomgerenciarusuario.service;

import com.baeldung.openapi.model.*;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import com.leduar.atomgerenciarusuario.exceptions.*;
import com.leduar.atomgerenciarusuario.mapper.UsuarioMapper;
import com.leduar.atomgerenciarusuario.repository.CarroRepository;
import com.leduar.atomgerenciarusuario.repository.UsuarioRepository;
import com.leduar.atomgerenciarusuario.utils.Jwt;
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

    private final CarroRepository carroRepository;

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

    public DadosUsuarioResponseRepresentation buscarUsuario(Long id) throws Exception {
        log.info("=== Consultar usuário");
        Optional<UsuarioEntity> response = repository.findById(id);

        if (response.isPresent())
            return UsuarioMapper.consultarUsuarioEntityToRepresentation(response.get());

        throw new UsuarioNaoEncontradoException();
    }

    public SucessMessageRepresentation removerUsuario(Long id) throws Exception {
        log.info("=== Remover usuário");
        this.buscarUsuario(id);
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


    public GetUsuarioLogadoResponseRepresentation getDadosUsuarioLogado(String tokenJwt) throws Exception {
        log.info("=== Consultando dados do usuário logado");
        return UsuarioMapper.getUsuarioLogado(getUsuarioLogado(tokenJwt));
    }

    public ListaCarrosUsuarioLogadoResponseRepresentation listarCarrosUsuarioLogado(String tokenJwt) {
        log.info("=== Listando os carros do usuário logado");
        return UsuarioMapper.getListaCarrosUsuarioLogado(getUsuarioLogado(tokenJwt));
    }

    public SucessMessageRepresentation cadastrarCarroUsuarioLogado(String tokenJwt
            , CarRequestRepresentation carRequestRepresentation) throws Exception {
        log.info("=== Cadastrando o carro para o usuário logado");

        UsuarioMapper.validarDadosCadastroCarro(carRequestRepresentation);
        UsuarioEntity usuarioEntity = getUsuarioLogado(tokenJwt);
        this.validarDadosCadastroCarroExistente(carRequestRepresentation);

        usuarioEntity.getCars().add(UsuarioMapper.montarDadosCarroNovo(carRequestRepresentation));
        repository.save(usuarioEntity);

        return SucessMessageRepresentation.builder()
                .message("Sucesso ao incluir o novo veículo")
                .code(0)
                .build();
    }

    public CarResponseRepresentation buscarCarrosUsuarioLogado(String tokenJwt, Long idCarro) throws Exception {
        log.info("=== Consultando o carro do usuário logado");
        UsuarioEntity usuarioEntity = getUsuarioLogado(tokenJwt);

        return UsuarioMapper.montarCarroEntityBuscarUsuarioLogado(getUsuarioLogado(tokenJwt), idCarro);
    }

    public SucessMessageRepresentation removerCarroUsuarioLogado(String tokenJwt, Long idCarro) {
        log.info("=== Removendo o carro do usuário logado");

        UsuarioEntity usuarioEntity = getUsuarioLogado(tokenJwt);
        usuarioEntity.getCars().removeIf(carro -> carro.getId() == idCarro);
        repository.save(usuarioEntity);

        return SucessMessageRepresentation.builder()
                .message("Sucesso ao remover o veículo")
                .code(0)
                .build();
    }

    public SucessMessageRepresentation atualizarCarroUsuarioLogado(String tokenJwt, Long idCarro, CarRequestRepresentation carRequestRepresentation) throws Exception {
        log.info("=== Atualizando o carro do usuário logado");

        UsuarioMapper.validarDadosCadastroCarro(carRequestRepresentation);
        UsuarioEntity usuarioEntity = getUsuarioLogado(tokenJwt);
        this.validarDadosCadastroCarroExistente(carRequestRepresentation);

        usuarioEntity.getCars().forEach(carro -> {
            if (carro.getId() == idCarro) {
                carro.setCarYear(carRequestRepresentation.getYear());
                carro.setColor(carRequestRepresentation.getColor());
                carro.setLicensePlate(carRequestRepresentation.getLicensePlate());
                carro.setModel(carRequestRepresentation.getModel());
            }
        });

        repository.save(usuarioEntity);

        return SucessMessageRepresentation.builder()
                .message("Sucesso ao atualizar o veículo")
                .code(0)
                .build();
    }


    /*
    *
    *  Metodos privados abaixo.
    *
    */


    private void validarDadosCadastroCarroExistente(CarRequestRepresentation body) throws Exception {
        if(carroRepository.existsCarroEntitiesByLicensePlate(body.getLicensePlate()))
            throw new PlacaExistenteException();
    }

    /**
     *  Validar se todos os campos foram preenchidos e se o login ou o email ja conta na base de dados
     * @param body
     * @throws Exception
     */
    private void validarCadastro(DadosUsuarioResponseRepresentation body) throws Exception {
        UsuarioMapper.validarCampos(body);

        if (!repository.findUsuarioEntityByLogin(body.getLogin()).isEmpty()) {
            throw new LoginExistenteException();
        }

        if (!repository.findUsuarioEntityByEmail(body.getEmail()).isEmpty()) {
            throw new EmailExistenteException();
        }
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
        UsuarioMapper.validarCampos(body);

        if (!repository.findUsuarioEntityByLoginAndIdNot(body.getLogin(), id).isEmpty()) {
            throw new LoginExistenteException();
        }

        if (!repository.findUsuarioEntityByEmailAndIdNot(body.getEmail(), id).isEmpty()) {
            throw new EmailExistenteException();
        }
    }


    /**
     *  Valida se o Token JWT é valido e busca da base os dados desse usuario logado
     *
     * @param tokenJwt
     * @return
     */
    private UsuarioEntity getUsuarioLogado(String tokenJwt) {
        Jws<Claims> sessao = Jwt.validateToken(tokenJwt);
        return repository.findById(Long.parseLong(
                sessao.getBody().get("id").toString())
        ).get();
    }
}
