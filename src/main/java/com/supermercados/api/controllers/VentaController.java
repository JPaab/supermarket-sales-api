package com.supermercados.api.controllers;

import com.supermercados.api.dtos.venta.VentaRequestDTO;
import com.supermercados.api.dtos.venta.VentaResponseDTO;
import com.supermercados.api.models.ApiResponse;
import com.supermercados.api.services.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor

public class VentaController {

    private final VentaService ventaService;

    //Post /api/ventas
    //Crea una nueva venta
    @PostMapping
    public ResponseEntity<ApiResponse<VentaResponseDTO>> crearVenta(@Valid @RequestBody VentaRequestDTO dto) {
        VentaResponseDTO ventaCreada = ventaService.registrarDTO(dto);
        //Llama al service para registar la venta, y  devuelve HTTO 201 si la venta se crea
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Nueva venta creada", ventaCreada));
    }

    //GET /api/ventas
    //Obtiene las ventas mediante filtros
    @GetMapping
    public ResponseEntity<ApiResponse<List<VentaResponseDTO>>> obtenerVentas(
            @RequestParam(required = false) Long sucursalId,
            //Se pide la fecha en formato ISO (YYYY-MM-DD/2002-04-03)
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<VentaResponseDTO> ventas = ventaService.buscarDTO(sucursalId, fecha);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Ventas encontradas", ventas));
    }

    //Delete /api/ventas/{id}
    //Borrado lógico
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<VentaResponseDTO>> eliminarVenta(@PathVariable Long id) {

        //Llama al service para anular la venta
        VentaResponseDTO ventaAnulada = ventaService.anularDTO(id);

        // Devuelve HTTP 200 OK sin contenido
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Venta eliminada correctamente", ventaAnulada));
    }
}
