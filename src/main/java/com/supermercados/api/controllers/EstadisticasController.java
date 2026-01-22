package com.supermercados.api.controllers;


import com.supermercados.api.dtos.producto.ProductoResponseDTO;
import com.supermercados.api.models.Producto;
import com.supermercados.api.services.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
