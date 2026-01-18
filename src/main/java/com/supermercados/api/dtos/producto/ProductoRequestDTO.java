package com.supermercados.api.dtos.producto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductoRequestDTO {

    @NotBlank(message = "Nombre obligatorio")
    private String nombre;

    @NotNull(message = "Precio obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser mayor a 0$")
    private BigDecimal precio;

    @NotBlank(message = "Categoria obligatoria")
    private String categoria;

    @NotNull(message = "Stock obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor a 0$")
    private Integer stock;
}
