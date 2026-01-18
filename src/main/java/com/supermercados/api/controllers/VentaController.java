package com.supermercados.api.controllers;

import com.supermercados.api.dtos.requests.VentaRequestDTO;
import com.supermercados.api.dtos.responses.VentaResponseDTO;
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

    /* private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ventaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> obtenerVentas(
            @RequestParam(required = false)  Long id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDate fecha) {

        return ResponseEntity.ok(
                ventaService.findBySucursalAndFecha(sucursalId, fecha));
        }
    @DeleteMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> eliminarVenta(@PathVariable Long id){
        ventaService.deleteVenta(id);
        return ResponseEntity.ok().body();
    } */
    }
