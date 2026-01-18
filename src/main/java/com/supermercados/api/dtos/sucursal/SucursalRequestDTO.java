package com.supermercados.api.dtos.sucursal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SucursalRequestDTO {

    @NotBlank(message = "Nombre obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "Dirección obligatoria")
    @Size(max = 200, message = "La direccion no puede exceder 200 caracteres")
    private String direccion;
}
