package com.supermercados.api.dtos.sucursal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SucursalEstadisticaDTO {

    private SucursalResponseDTO sucursal;
    private Long cantidadVentas;
    private BigDecimal ingresosTotales;

}
