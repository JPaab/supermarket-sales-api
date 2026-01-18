package com.supermercados.api.dtos.venta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VentaDetalleRequestDTO {

    @NotNull(message = "productoId obligatorio")
    private Long productoId;

    @NotNull(message = "Cantidad obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer cantidad;


}
