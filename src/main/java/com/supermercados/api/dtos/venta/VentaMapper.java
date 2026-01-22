package com.supermercados.api.dtos.venta;

import com.supermercados.api.models.Venta;
import com.supermercados.api.models.VentaDetalle;

import java.math.BigDecimal;
import java.util.List;

public class VentaMapper {

    public static VentaResponseDTO toDTO(Venta v) {
        List<VentaDetalleResponseDTO> det = v.getDetalle().stream().map(d -> toDetalleDTO(d)).toList();

        BigDecimal total = det.stream()
                .map(VentaDetalleResponseDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponseDTO(
                v.getId(),
                v.getSucursal().getId(),
                v.getSucursal().getNombre(),
                v.getFecha(),
                v.isAnulada(),
                det,
                total
        );
    }

    private static VentaDetalleResponseDTO toDetalleDTO(VentaDetalle d) {
        BigDecimal precioUnitario = d.getPrecioUnitario();
        BigDecimal subtotal = d.getSubtotal();
        if (subtotal == null && precioUnitario != null && d.getCantidad() != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(d.getCantidad()));
        }
        return new VentaDetalleResponseDTO(
                d.getProducto().getId(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                precioUnitario,
                subtotal
        );
    }
}
