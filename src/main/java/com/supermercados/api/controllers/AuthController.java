package com.supermercados.api.controllers;

import com.supermercados.api.dtos.auth.AuthResponseDTO;
import com.supermercados.api.dtos.auth.LoginRequestDTO;
import com.supermercados.api.dtos.auth.RegisterRequestDTO;
import com.supermercados.api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // aqui - Servicio de autenticacion (login y registro)
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // aqui - Registro de usuario (crea usuario y devuelve token)
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // aqui - Login de usuario (devuelve token)
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}