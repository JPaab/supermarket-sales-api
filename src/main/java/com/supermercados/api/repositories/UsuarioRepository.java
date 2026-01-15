package com.supermercados.api.repositories;

import com.supermercados.api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // aqui - Buscar usuario por username (para login)
    Optional<Usuario> findByUsername(String username);

    // aqui - Verificar si username ya existe
    boolean existsByUsername(String username);

    // aqui - Verificar si username ya existe (excluyendo un ID)
    boolean existsByUsernameAndIdNot(String username, Long id);

    // aqui - Buscar usuarios activos
    List<Usuario> findByActivoTrue();

}

