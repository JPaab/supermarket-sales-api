package com.supermercados.api.dtos.venta;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VentaRequestDTO {

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long sucursalId;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<VentaDetalleRequestDTO> detalle;
}


