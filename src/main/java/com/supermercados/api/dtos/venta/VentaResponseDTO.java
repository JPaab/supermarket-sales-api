package com.supermercados.api.dtos.venta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VentaResponseDTO {

    private Long id;
    private Long sucursalId;
    private String sucursalNombre;
    private LocalDate fecha;
    private Boolean anulada;
    private List<VentaDetalleResponseDTO> productos;
    private BigDecimal total;
}