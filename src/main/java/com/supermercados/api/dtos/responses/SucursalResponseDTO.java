package com.supermercados.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SucursalResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private Boolean activa;
}
