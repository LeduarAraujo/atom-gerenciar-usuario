package com.leduar.atomgerenciarusuario.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "tbUsuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String firstName;
    @Column
    String lastName;
    @Column
    String email;
    @Column
    LocalDate birthday;
    @Column
    String login;
    @Column
    String password;
    @Column
    String phone;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<CarroEntity> cars;
}
