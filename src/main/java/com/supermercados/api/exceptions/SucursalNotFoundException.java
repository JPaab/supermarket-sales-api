package com.supermercados.api.exceptions;

public class SucursalNotFoundException extends RuntimeException {
    public SucursalNotFoundException(Long id) {
        super("Sucursal no encontrada con ID: " + id);
    }
}
