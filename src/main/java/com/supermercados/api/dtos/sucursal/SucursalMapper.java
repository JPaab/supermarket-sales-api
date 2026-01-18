package com.supermercados.api.dtos.sucursal;

import com.supermercados.api.models.Sucursal;

public class SucursalMapper {

    public static Sucursal toModel(SucursalRequestDTO dto) {
        Sucursal s = new Sucursal();
        s.setNombre(dto.getNombre());
        s.setDireccion(dto.getDireccion());
        return s;
    }

    public static SucursalResponseDTO toDTO(Sucursal s) {
        return new SucursalResponseDTO(s.getId(), s.getNombre(), s.getDireccion(), s.getActiva());
    }
}
