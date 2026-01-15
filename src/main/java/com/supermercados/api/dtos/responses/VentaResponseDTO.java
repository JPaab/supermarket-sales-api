package com.supermercados.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class VentaResponseDTO {

    private Long id;
    private Long sucursalId;
    private String sucursalNombre;
    private LocalDate fechaVenta;
    private BigDecimal total;
    private Boolean anulada;
    private List<VentaProductoResponseDTO> productos;

    @Data
    @AllArgsConstructor
    public static class VentaProductoResponseDTO {
        private Long productoId;
        private String productoNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}