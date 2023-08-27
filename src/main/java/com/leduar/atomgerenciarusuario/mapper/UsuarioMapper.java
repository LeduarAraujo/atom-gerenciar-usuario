package com.leduar.atomgerenciarusuario.mapper;

import com.baeldung.openapi.model.*;
import com.leduar.atomgerenciarusuario.domain.entity.CarroEntity;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
* - Classe responsável por realizar os parses dos objetos entity para representation e vice-versa.
*/

public class UsuarioMapper {

    /**
     * @param response
     * @return
     */
    public static List<DadosUsuarioResponseRepresentation> usuarioEntityToRepresentation(List<UsuarioEntity> response) {
        List<DadosUsuarioResponseRepresentation> retorno = new ArrayList<>();

        response.forEach(resp -> {
            retorno.add(DadosUsuarioResponseRepresentation.builder()
                            .idUsuario(resp.getId())
                            .firstName(resp.getFirstName())
                            .lastName(resp.getLastName())
                            .email(resp.getEmail())
                            .birthday(resp.getBirthday().toString())
                            .login(resp.getLogin())
                            .password(resp.getPassword())
                            .phone(resp.getPhone())
                            .cars(listCarToRepresentation(resp.getCars()))
                            .createdAt(resp.getCreatedAt() != null ? resp.getCreatedAt().toString() : null)
                            .lastLogin(resp.getLastLogin() != null ? resp.getLastLogin().toString() : null)
                    .build());
        });

        return retorno;
    }

    /**
     * @param response
     * @return
     */
    private static @Valid List<CarResponseRepresentation> listCarToRepresentation(List<CarroEntity> response) {
        List<CarResponseRepresentation> retorno = new ArrayList<>();

        response.forEach( carroEntity -> {
            retorno.add(CarResponseRepresentation.builder()
                            .idCarro(carroEntity.getId())
                            .color(carroEntity.getColor())
                            .licensePlate(carroEntity.getLicensePlate())
                            .model(carroEntity.getModel())
                            .year(carroEntity.getCarYear())
                    .build());
        });

        return retorno;
    }

    public static UsuarioEntity usuarioRepresentationToEntity(DadosUsuarioResponseRepresentation request) {
        UsuarioEntity retorno = new UsuarioEntity();
        retorno.setId(request.getIdUsuario());
        retorno.setFirstName(request.getFirstName());
        retorno.setLastName(request.getLastName());
        retorno.setEmail(request.getEmail());
        retorno.setBirthday(LocalDate.parse(request.getBirthday()));
        retorno.setLogin(request.getLogin());
        retorno.setPassword(request.getPassword());
        retorno.setPhone(request.getPhone());
        retorno.setCars(listCarToEntity(request.getCars()));
        return retorno;
    }

    private static List<CarroEntity> listCarToEntity(@Valid List<CarResponseRepresentation> request) {
        List<CarroEntity> retorno = new ArrayList<>();

        request.forEach( carroEntity -> {
            CarroEntity entity = new CarroEntity();
            entity.setId(carroEntity.getIdCarro());
            entity.setCarYear(carroEntity.getYear());
            entity.setColor(carroEntity.getColor());
            entity.setModel(carroEntity.getModel());
            entity.setLicensePlate(carroEntity.getLicensePlate());

            retorno.add(entity);
        });

        return retorno;
    }

    public static DadosUsuarioResponseRepresentation consultarUsuarioEntityToRepresentation(UsuarioEntity usuarioEntity) {
        return DadosUsuarioResponseRepresentation.builder()
                .idUsuario(usuarioEntity.getId())
                .firstName(usuarioEntity.getFirstName())
                .lastName(usuarioEntity.getLastName())
                .email(usuarioEntity.getEmail())
                .birthday(usuarioEntity.getBirthday().toString())
                .login(usuarioEntity.getLogin())
                .password(usuarioEntity.getPassword())
                .phone(usuarioEntity.getPhone())
                .cars(listCarToRepresentation(usuarioEntity.getCars()))
                .createdAt(usuarioEntity.getCreatedAt() != null ? usuarioEntity.getCreatedAt().toString() : null)
                .lastLogin(usuarioEntity.getLastLogin() != null ? usuarioEntity.getLastLogin().toString() : null)
                .build();
    }

    public static UsuarioEntity montarUsuarioAtualizado(UsuarioEntity usuarioEntity,
                                                        DadosUsuarioResponseRepresentation body) {
        usuarioEntity.setFirstName(body.getFirstName());
        usuarioEntity.setLastName(body.getLastName());
        usuarioEntity.setEmail(body.getEmail());
        usuarioEntity.setBirthday(LocalDate.parse(body.getBirthday()));
        usuarioEntity.setLogin(body.getLogin());
        usuarioEntity.setPassword(body.getPassword());
        usuarioEntity.setPhone(body.getPhone());
        usuarioEntity.setCars(listCarToEntity(body.getCars()));

        return usuarioEntity;
    }

    public static GetUsuarioLogadoResponseRepresentation getUsuarioLogado(UsuarioEntity response) {
        return GetUsuarioLogadoResponseRepresentation.builder()
                .idUsuarioLogado(response.getId())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .email(response.getEmail())
                .birthday(response.getBirthday().toString())
                .login(response.getLogin())
                .phone(response.getPhone())
                .createdAt(response.getCreatedAt().toString())
                .lastLogin(response.getLastLogin().toString())
                .build();
    }

    public static ListaCarrosUsuarioLogadoResponseRepresentation getListaCarrosUsuarioLogado(UsuarioEntity response) {
        return ListaCarrosUsuarioLogadoResponseRepresentation.builder()
                .idUsuario(response.getId())
                .firstName(response.getFirstName())
                .carros(listCarToRepresentation(response.getCars()))
                .build();
    }

    public static CarroEntity montarDadosCarroNovo(CarRequestRepresentation carRequestRepresentation) {
        CarroEntity carroEntity = new CarroEntity();
        carroEntity.setModel(carRequestRepresentation.getModel());
        carroEntity.setColor(carRequestRepresentation.getColor());
        carroEntity.setLicensePlate(carRequestRepresentation.getLicensePlate());
        carroEntity.setCarYear(carRequestRepresentation.getYear());

        return carroEntity;
    }
}
