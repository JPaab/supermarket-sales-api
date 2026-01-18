package com.supermercados.api.controllers;

import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.services.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursal")
@RequiredArgsConstructor

public class SurcursalController {

    private final SucursalService sucursalService;

    //Muestra las sucursales existentes
    @GetMapping
    public ResponseEntity<List<Sucursal>> listar(){
        return ResponseEntity.ok(sucursalService.listar());
    }

    //Permite crear una sucursal con los parametros establecidos
    @PostMapping
    public ResponseEntity<Sucursal> crear(@Valid @RequestBody Producto producto){
        Sucursal sucursalCreado = sucursalService.crear(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalCreado);
    }

    //Permite buscar la sucursal por ID
    @PostMapping("{/id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.obtenerPorId(id));
    }

    //Actualiza la sucursal
    @PutMapping("{/id}")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Long id, @Valid @RequestBody Sucursal sucursal){
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.actualizar(id, sucursal));
    }

    //Elimina una sucursal
    @DeleteMapping
    public ResponseEntity<Sucursal> eliminar(@PathVariable Long id){
        sucursalService.eliminar(id);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalService.obtenerPorId(id));
    }

}
