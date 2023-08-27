package com.leduar.atomgerenciarusuario.repository;

import com.leduar.atomgerenciarusuario.domain.entity.CarroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<CarroEntity, Long> {

    Boolean existsCarroEntitiesByLicensePlate(String licensePlate);
}
