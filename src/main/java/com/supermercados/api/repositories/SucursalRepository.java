package com.supermercados.api.repositories;

import com.supermercados.api.models.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal,Long> {

    // aqui - Buscar sucursales por nombre
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);

    // aqui - Buscar solo sucursales activas
    List<Sucursal> findByActivaTrue();

    // aqui - Verificar si el nombre ya existe
    boolean existsByNombreIgnoreCase(String nombre);

    // aqui - Verificar si el nombre ya existe pero excluyendo un ID para el PUT
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre,Long id);

}
