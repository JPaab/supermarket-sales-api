package com.supermercados.api.controllers;

import com.supermercados.api.dtos.sucursal.SucursalMapper;
import com.supermercados.api.dtos.sucursal.SucursalResponseDTO;
import com.supermercados.api.models.ApiResponse;
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
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> crear(@Valid @RequestBody Producto producto){
        Sucursal sucursalCreada = sucursalService.crear(null);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Nueva sucursal creada", SucursalMapper.toDTO(sucursalCreada)));
    }

    //Permite buscar la sucursal por ID
    @PostMapping("{/id}")
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> obtenerPorId(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Sucursal por ID", SucursalMapper.toDTO(sucursalService.obtenerPorId(id))));
    }

    //Actualiza la sucursal
    @PutMapping("{/id}")
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody Sucursal sucursal){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Sucursal actualizada correctamente", SucursalMapper.toDTO(sucursalService.actualizar(id, sucursal))));
    }

    //Elimina una sucursal
    @DeleteMapping
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> eliminar(@PathVariable Long id){
        sucursalService.eliminar(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Sucursal eliminada correctamente", SucursalMapper.toDTO(sucursalService.obtenerPorId(id))));
    }

}
