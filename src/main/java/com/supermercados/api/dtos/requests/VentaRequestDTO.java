package com.supermercados.api.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VentaRequestDTO {

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long sucursalId;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<VentaProductoDTO> productos;

    // aqui creo una clase interna para cada producto en la venta
    @Data
    public static class VentaProductoDTO {
        private Long productoId;

        private Integer cantidad;
    }
}


