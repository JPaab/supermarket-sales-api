package com.supermercados.api.controllers;

import com.supermercados.api.dtos.sucursal.SucursalMapper;
import com.supermercados.api.dtos.sucursal.SucursalRequestDTO;
import com.supermercados.api.dtos.sucursal.SucursalResponseDTO;
import com.supermercados.api.models.ApiResponse;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.services.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor

public class SucursalController {

    private final SucursalService sucursalService;

    //Muestra las sucursales existentes
    @GetMapping
    public ResponseEntity<ApiResponse<List<SucursalResponseDTO>>> listar() {
        List<SucursalResponseDTO> sucursales = sucursalService.listar()
                .stream()
                .map(SucursalMapper::toDTO)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Lista de sucursales", sucursales));
    }

    //Permite crear una sucursal con los parametros establecidos
    @PostMapping
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> crear(@Valid @RequestBody SucursalRequestDTO dto) {
        Sucursal sucursalCreada = sucursalService.crear(SucursalMapper.toModel(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Nueva sucursal creada", SucursalMapper.toDTO(sucursalCreada)));
    }

    //Permite buscar la sucursal por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Sucursal por ID", SucursalMapper.toDTO(sucursalService.obtenerPorId(id))));
    }

    //Actualiza la sucursal
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SucursalResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalRequestDTO dto) {

        Sucursal actualizada = sucursalService.actualizar(id, SucursalMapper.toModel(dto));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Sucursal actualizada correctamente", SucursalMapper.toDTO(actualizada)));
    }

    //Elimina una sucursal
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        sucursalService.eliminar(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Sucursal eliminada correctamente", null));
    }

}
