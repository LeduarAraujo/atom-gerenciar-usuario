package com.leduar.atomgerenciarusuario.service;

import com.baeldung.openapi.model.CarRequestRepresentation;
import com.baeldung.openapi.model.DadosUsuarioResponseRepresentation;
import com.baeldung.openapi.model.SigninUsuarioRequestRepresentation;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import com.leduar.atomgerenciarusuario.exceptions.CarroNaoExistenteException;
import com.leduar.atomgerenciarusuario.exceptions.LoginSenhaException;
import com.leduar.atomgerenciarusuario.exceptions.UsuarioNaoEncontradoException;
import com.leduar.atomgerenciarusuario.repository.CarroRepository;
import com.leduar.atomgerenciarusuario.repository.UsuarioRepository;
import com.leduar.atomgerenciarusuario.utils.Jwt;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GerenciarUsuarioServiceTest {

    @InjectMocks
    GerenciarUsuarioService service;

    @Mock
    UsuarioRepository repository;

    @Mock
    CarroRepository carroRepository;

    EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        easyRandom = new EasyRandom();
    }

    @Test
    void listarUsuarios() {
        assertNotNull(service.listarUsuarios());
    }

    @Test
    void cadastrarUsuario() throws Exception {
        var body = easyRandom.nextObject(DadosUsuarioResponseRepresentation.class);
        body.setBirthday("1990-05-01");
        assertNotNull(service.cadastrarUsuario(body));
    }

    @Test
    void buscarUsuario_SUCESSO() throws Exception {
        Optional<UsuarioEntity> response = Optional.of(easyRandom.nextObject(UsuarioEntity.class));
        Mockito.when(repository.findById(1L)).thenReturn(response);
        assertNotNull(service.buscarUsuario(1L));
    }

    @Test
    void buscarUsuario_ERRO() throws Exception {
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.buscarUsuario(1L));
    }

    @Test
    void removerUsuario_ERRO() throws Exception {
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.removerUsuario(1L));
    }

    @Test
    void removerUsuario_SUCESSO() throws Exception {
        Optional<UsuarioEntity> response = Optional.of(easyRandom.nextObject(UsuarioEntity.class));
        Mockito.when(repository.findById(1L)).thenReturn(response);
        assertNotNull(service.removerUsuario(1L));
    }

    @Test
    void atualizarUsuario_ERRO() throws Exception {
        var body = easyRandom.nextObject(DadosUsuarioResponseRepresentation.class);
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.atualizarUsuario(1L, body));
    }

    @Test
    void atualizarUsuario_SUCESSO() throws Exception {
        var body = easyRandom.nextObject(DadosUsuarioResponseRepresentation.class);
        body.setBirthday("1990-05-01");

        var usuario = easyRandom.nextObject(UsuarioEntity.class);
        usuario.setBirthday(LocalDate.parse("1990-05-01"));
        usuario.setLastLogin(LocalDateTime.now());
        usuario.setCreatedAt(LocalDateTime.now());
        Optional<UsuarioEntity> response = Optional.of(usuario);
        Mockito.when(repository.findById(1L)).thenReturn(response);
        assertNotNull(service.atualizarUsuario(1L, body));
    }

    @Test
    void iniciarSessao_ERRO() {
        var body = easyRandom.nextObject(SigninUsuarioRequestRepresentation.class);
        assertThrows(LoginSenhaException.class, () -> service.iniciarSessao(body));
    }

    @Test
    void iniciarSessao_SUCESSO() throws LoginSenhaException {
        var body = easyRandom.nextObject(SigninUsuarioRequestRepresentation.class);
        var usuarioEntity = easyRandom.nextObject(UsuarioEntity.class);

        Optional<UsuarioEntity> response = Optional.of(usuarioEntity);
        Mockito.when(repository.findUsuarioEntityByLoginAndPassword(body.getLogin(), body.getSenha()))
                .thenReturn(response);

        Mockito.when(repository.save(response.get())).thenReturn(usuarioEntity);
        assertNotNull(service.iniciarSessao(body));
    }

    @Test
    void getDadosUsuarioLogado() throws Exception {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(easyRandom.nextObject(UsuarioEntity.class)));
        assertNotNull(service.getDadosUsuarioLogado(Jwt.gerarToken("nome", 1L)));
    }

    @Test
    void listarCarrosUsuarioLogado() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(easyRandom.nextObject(UsuarioEntity.class)));
        assertNotNull(service.listarCarrosUsuarioLogado(Jwt.gerarToken("nome", 1L)));
    }

    @Test
    void cadastrarCarroUsuarioLogado() throws Exception {
        var body = easyRandom.nextObject(CarRequestRepresentation.class);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(easyRandom.nextObject(UsuarioEntity.class)));
        assertNotNull(service.cadastrarCarroUsuarioLogado(Jwt.gerarToken("nome", 1L), body));
    }

    @Test
    void buscarCarrosUsuarioLogado_ERRO() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(easyRandom.nextObject(UsuarioEntity.class)));
        assertThrows(CarroNaoExistenteException.class ,() -> service.buscarCarrosUsuarioLogado(Jwt.gerarToken("nome", 1L), 1L));
    }

    @Test
    void buscarCarrosUsuarioLogado_SUCESSO() throws Exception {
        var usuario = Optional.of(easyRandom.nextObject(UsuarioEntity.class));
        usuario.get().getCars().get(0).setId(1L);
        Mockito.when(repository.findById(1L)).thenReturn(usuario);
        assertNotNull(service.buscarCarrosUsuarioLogado(Jwt.gerarToken("nome", 1L), 1L));
    }

    @Test
    void removerCarroUsuarioLogado() {
        var usuario = Optional.of(easyRandom.nextObject(UsuarioEntity.class));
        usuario.get().getCars().get(0).setId(1L);
        Mockito.when(repository.findById(1L)).thenReturn(usuario);
        assertNotNull(service.removerCarroUsuarioLogado(Jwt.gerarToken("nome", 1L), 1L));
    }

    @Test
    void atualizarCarroUsuarioLogado() throws Exception {
        var body = easyRandom.nextObject(CarRequestRepresentation.class);
        var usuario = Optional.of(easyRandom.nextObject(UsuarioEntity.class));
        usuario.get().getCars().get(0).setId(1L);
        Mockito.when(repository.findById(1L)).thenReturn(usuario);
        assertNotNull(service.atualizarCarroUsuarioLogado(Jwt.gerarToken("nome", 1L), 1L, body));
    }
}