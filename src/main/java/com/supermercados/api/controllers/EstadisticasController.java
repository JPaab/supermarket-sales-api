package com.supermercados.api.controllers;


import com.supermercados.api.dtos.producto.ProductoEstadisticaDTO;
import com.supermercados.api.dtos.producto.ProductoResponseDTO;
import com.supermercados.api.dtos.sucursal.SucursalEstadisticaDTO;
import com.supermercados.api.models.ApiResponse;
import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.services.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {
    private final EstadisticasService estadisticaService;

    @GetMapping("/producto-mas-vendido")
    public ResponseEntity<ProductoResponseDTO> productoMasVendido() {

        Producto producto = estadisticaService.productoMasVendido();

        ProductoResponseDTO dto = new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.getStock()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/ingresos-totales")
    public ResponseEntity<ApiResponse<BigDecimal>> ingresosTotales(@RequestParam(required = false) Long sucursalId) {
        BigDecimal total = estadisticaService.ingresosTotales(sucursalId);

        String msg = (sucursalId == null)
                ? "Ingresos totales (todas las sucursales)"
                : "Ingresos totales de la sucursal " + sucursalId;

        return ResponseEntity.ok(new ApiResponse<>(true, msg, total));
    }

    //TOP 5
    @GetMapping("/productos/top-5")
    public ResponseEntity<List<ProductoResponseDTO>> top5ProductosMasVendidos() {
        return ResponseEntity.ok(
                estadisticaService.top5ProductosMasVendidos()
        );
    }
//POR PRODUCTO
@GetMapping("/productos/{productoId}")
public ResponseEntity<ApiResponse<ProductoEstadisticaDTO>> estadisticasProducto(
        @PathVariable Long productoId) {
    return ResponseEntity.ok(
            new ApiResponse<>(true, "Estadísticas del producto",
                    estadisticaService.estadisticasProducto(productoId))
    );
}


    //POR SUCURSAL

    @GetMapping("/sucursales/{sucursalId}")
    public ResponseEntity<ApiResponse<SucursalEstadisticaDTO>> estadisticasSucursal(
            @PathVariable Long sucursalId) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Estadísticas de la sucursal",
                        estadisticaService.estadisticasSucursal(sucursalId))
        );
    }


}
