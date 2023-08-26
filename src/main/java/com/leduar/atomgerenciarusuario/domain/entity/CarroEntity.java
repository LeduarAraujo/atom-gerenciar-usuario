package com.leduar.atomgerenciarusuario.domain.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "tbCarro")
public class CarroEntity {

    @Id
    Long id;
    @Column
    Integer carYear;
    @Column
    String licensePlate;
    @Column
    String model;
    @Column
    String color;
}
