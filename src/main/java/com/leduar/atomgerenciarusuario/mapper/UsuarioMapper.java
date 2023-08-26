package com.leduar.atomgerenciarusuario.mapper;

import com.baeldung.openapi.model.CarRepresentation;
import com.baeldung.openapi.model.DadosUsuarioResponseRepresentation;
import com.leduar.atomgerenciarusuario.domain.entity.CarroEntity;
import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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
                    .firstName(resp.getFirstName())
                    .lastName(resp.getLastName())
                    .email(resp.getEmail())
                    .birthday(resp.getBirthday().toString())
                    .login(resp.getLogin())
                    .password(resp.getPassword())
                    .phone(resp.getPhone())
                    .cars(listCarToRepresentation(resp.getCars()))
                    .build());
        });

        return retorno;
    }

    /**
     * @param response
     * @return
     */
    private static List<CarRepresentation> listCarToRepresentation(List<CarroEntity> response) {
        List<CarRepresentation> retorno = new ArrayList<>();

        response.forEach( carroEntity -> {
            retorno.add(CarRepresentation.builder()
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
        retorno.setFirstName(request.getFirstName());
        retorno.setLastName(retorno.getLastName());
        retorno.setEmail(request.getEmail());
//        retorno.setBirthday(new LocalDate());
//        retorno.setLogin();
//        retorno.setPassword();
//        retorno.setPhone();
        retorno.setCars(listCarToEntity(request.getCars()));
        return retorno;
    }


    private static List<CarroEntity> listCarToEntity(List<CarRepresentation> request) {
        List<CarroEntity> retorno = new ArrayList<>();

        request.forEach( carroEntity -> {
            CarroEntity entity = new CarroEntity();

            retorno.add(entity);
        });

        return retorno;
    }

}
