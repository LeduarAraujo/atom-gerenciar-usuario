package com.leduar.atomgerenciarusuario.repository;

import com.leduar.atomgerenciarusuario.domain.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    List<UsuarioEntity> findUsuarioEntityByLoginAndPassword(String login, String password);

    List<UsuarioEntity> findUsuarioEntityByLogin(String login);

    List<UsuarioEntity> findUsuarioEntityByEmail(String email);

    List<UsuarioEntity> findUsuarioEntityByLoginAndIdNot(String login, Long id);

    List<UsuarioEntity> findUsuarioEntityByEmailAndIdNot(String email, Long id);

}
