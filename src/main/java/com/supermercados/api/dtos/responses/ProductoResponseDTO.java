package com.supermercados.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String categoria;
    private Integer stock;
    private Boolean activo;
}