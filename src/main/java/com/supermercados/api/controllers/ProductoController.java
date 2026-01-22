package com.supermercados.api.controllers;

import com.supermercados.api.dtos.producto.ProductoMapper;
import com.supermercados.api.dtos.producto.ProductoRequestDTO;
import com.supermercados.api.dtos.producto.ProductoResponseDTO;
import com.supermercados.api.models.ApiResponse;
import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.models.Venta;
import com.supermercados.api.services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    //Muestra los productos existentes
    @GetMapping
    public ResponseEntity<List<Producto>> listar(){
        return ResponseEntity.ok(productoService.listar());
    }

    //Permite crear un producto con los parametros establecidos
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> crear(@Valid @RequestBody Producto producto){
        Producto productoCreado = productoService.crear(producto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Producto creado correctamente", ProductoMapper.toDTO(productoCreado)));
    }


    //Actualiza el producto
    @PutMapping("{/id}")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO producto){
        Producto productoActualizar = productoService.actualizar(id, ProductoMapper.toModel(producto));
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
}
