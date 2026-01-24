package com.supermercados.api.dtos.producto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
public class ProductoAdminResponseDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String categoria;
    private Integer stock;
    private Boolean activo;
}