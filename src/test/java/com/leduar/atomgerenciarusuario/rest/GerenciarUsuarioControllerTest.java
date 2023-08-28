package com.leduar.atomgerenciarusuario.rest;

import com.baeldung.openapi.model.CarRequestRepresentation;
import com.baeldung.openapi.model.DadosUsuarioResponseRepresentation;
import com.baeldung.openapi.model.SigninUsuarioRequestRepresentation;
import com.leduar.atomgerenciarusuario.service.GerenciarUsuarioService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GerenciarUsuarioControllerTest {

    @InjectMocks
    GerenciarUsuarioController controller;

    @Mock
    GerenciarUsuarioService service;

    EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        easyRandom = new EasyRandom();
    }

    @Test
    void listarUsuarios() {
        assertNotNull(controller.listarUsuarios());
    }

    @Test
    void cadastrarUsuario() {
        var body = easyRandom.nextObject(DadosUsuarioResponseRepresentation.class);
        assertNotNull(controller.cadastrarUsuario(body));
    }

    @Test
    void buscarUsuario() {
        assertNotNull(controller.buscarUsuario(1L));
    }

    @Test
    void removerUsuario() {
        assertNotNull(controller.removerUsuario(1L));
    }

    @Test
    void atualizarUsuario() {
        var body = easyRandom.nextObject(DadosUsuarioResponseRepresentation.class);
        assertNotNull(controller.atualizarUsuario(1L, body));
    }

    @Test
    void iniciarSessao() {
        var body = easyRandom.nextObject(SigninUsuarioRequestRepresentation.class);
        assertNotNull(controller.iniciarSessao(body));
    }

    @Test
    void getDadosUsuarioLogado() {
        var tokenJwt = UUID.randomUUID().toString();
        assertNotNull(controller.getDadosUsuarioLogado(tokenJwt));
    }

    @Test
    void listarCarrosUsuarioLogado() {
        var tokenJwt = UUID.randomUUID().toString();
        assertNotNull(controller.listarCarrosUsuarioLogado(tokenJwt));
    }

    @Test
    void cadastrarCarroUsuarioLogado() {
        var tokenJwt = UUID.randomUUID().toString();
        var body = easyRandom.nextObject(CarRequestRepresentation.class);
        assertNotNull(controller.cadastrarCarroUsuarioLogado(tokenJwt, body));
    }

    @Test
    void buscarCarrosUsuarioLogado() {
        var tokenJwt = UUID.randomUUID().toString();
        assertNotNull(controller.buscarCarrosUsuarioLogado(tokenJwt, 1L));
    }

    @Test
    void removerCarroUsuarioLogado() {
        var tokenJwt = UUID.randomUUID().toString();
        assertNotNull(controller.removerCarroUsuarioLogado(tokenJwt, 1L));
    }

    @Test
    void atualizarCarroUsuarioLogado() {
        var tokenJwt = UUID.randomUUID().toString();
        var body = easyRandom.nextObject(CarRequestRepresentation.class);
        assertNotNull(controller.atualizarCarroUsuarioLogado(tokenJwt, 1L, body));
    }
}