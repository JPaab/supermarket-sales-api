package com.supermercados.api.dtos.producto;

import com.supermercados.api.models.Producto;

public class ProductoMapper {

    public static Producto toModel(ProductoRequestDTO dto) {
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setCategoria(dto.getCategoria());
        p.setStock(dto.getStock());
        return p;
    }

    public static ProductoResponseDTO toDTO(Producto p) {
        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getCategoria(),
                p.getStock()
        );
    }

    public static ProductoAdminResponseDTO toAdminDTO(Producto p) {
        return new ProductoAdminResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getCategoria(),
                p.getStock(),
                p.getActivo()
        );
    }
}