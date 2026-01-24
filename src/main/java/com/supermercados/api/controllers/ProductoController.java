package com.supermercados.api.controllers;

import com.supermercados.api.dtos.producto.*;
import com.supermercados.api.models.ApiResponse;
import com.supermercados.api.models.Producto;
import com.supermercados.api.services.ProductoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Priority;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    //Muestra los productos existentes (solo productos activos)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> listar() {
        List<ProductoResponseDTO> productos = productoService.listar()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Lista de productos (activos)", productos));
    }

    //Permite crear un producto con los parametros establecidos
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> crear(@Valid @RequestBody ProductoRequestDTO dto){
        Producto productoCreado = productoService.crear(ProductoMapper.toModel(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Producto creado correctamente", ProductoMapper.toDTO(productoCreado)));
    }


    //Actualiza el producto
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO dto){
        Producto productoActualizar = productoService.actualizar(id, ProductoMapper.toModel(dto));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Producto actualizado correctamente", ProductoMapper.toDTO(productoActualizar)));
    }

    //Elimina un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"Producto eliminado correctamente", null));
    }

    //Reestock de un producto (solo para admin)
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")

    public ResponseEntity<ApiResponse<ProductoResponseDTO>> reponerStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequestDTO dto){
        Producto actualizarStock = productoService.reponerStock(id, dto.getCantidad());
        return ResponseEntity.ok(new ApiResponse<>(true, "Stock repuesto correctamente", ProductoMapper.toDTO(actualizarStock)));
    }

    //Listado de productos tanto activos como inactivos (solo para admin)
    @GetMapping("/todos-los-productos")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")

    public ResponseEntity<ApiResponse<List<ProductoAdminResponseDTO>>> listarAdmin(){
        List<ProductoAdminResponseDTO> allProducts = productoService.listarTodos()
                .stream()
                .map(ProductoMapper::toAdminDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista completa de productos", allProducts));
    }

    //Reactiva un producto eliminado
    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> reactivarProducto(@PathVariable Long id) {
        Producto reactivarProducto = productoService.reactivar(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Producto reactivado correctamente", ProductoMapper.toDTO(reactivarProducto))
        );
    }
}
