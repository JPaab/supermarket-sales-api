package com.supermercados.api.repositories;

import com.supermercados.api.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    // aqui - Buscar productos por nombre (ignorando mayus)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // aqui - Buscar productos por categoria
    List<Producto> findByCategoria(String categoria);

    // aqui - Buscar productos que esten activos (en stock)
    List<Producto> findByActivoTrue();

    // aqui - Verificar si el nombre ya existe
    boolean existsByNombreIgnoreCase(String nombre);

    // aqui - Verificar si el nombre ya existe pero excluyendo un ID para el PUT
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre,Long id);

}
