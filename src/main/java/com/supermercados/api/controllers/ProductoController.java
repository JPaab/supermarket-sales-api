package com.supermercados.api.controllers;

import com.supermercados.api.models.Producto;
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
/*
    //Permite crear un producto con los parametros establecidos
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto){
        Producto productoCreado = productoService.crear(null, producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

 */

    //Permite obtener el producto por ID
    @PostMapping("{/id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.obtenerPorId(id));
    }

    //Actualiza el producto
    @PutMapping("{/id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.actualizar(id, producto));
    }

    //Elimina un producto
    @DeleteMapping
    public ResponseEntity<Producto> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.status(HttpStatus.OK).body(productoService.obtenerPorId(id));
    }

}
