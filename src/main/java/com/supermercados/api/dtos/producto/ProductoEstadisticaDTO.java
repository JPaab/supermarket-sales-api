package com.supermercados.api.dtos.producto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductoEstadisticaDTO {

    private Long id;
    private String nombre;
    private Integer cantidadVendida;
    private BigDecimal ingresosGenerados;

}
