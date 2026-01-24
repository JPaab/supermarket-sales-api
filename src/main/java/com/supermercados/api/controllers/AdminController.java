package com.supermercados.api.controllers;

import com.supermercados.api.exceptions.BadRequestException;
import com.supermercados.api.exceptions.NotFoundException;
import com.supermercados.api.models.ApiResponse;
import com.supermercados.api.models.Usuario;
import com.supermercados.api.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final UsuarioRepository usuarioRepository;

    public AdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // aqui - Promover a usuario a ADMIN
    @PutMapping("/users/{username}/promote")
    public ResponseEntity<ApiResponse<Void>> promoteToAdmin(@PathVariable String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new BadRequestException("No se puede cambiar el rol de un usuario desactivado. Reactívalo primero.");
        }

        if ("ADMIN".equals(usuario.getRol())) {
            throw new BadRequestException("El usuario ya es ADMIN");
        }

        usuario.setRol("ADMIN");
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario promovido a ADMIN", null));
    }

    // aqui - Degradar a USER
    @PutMapping("/users/{username}/demote")
    public ResponseEntity<ApiResponse<Void>> demoteToUser(
            @PathVariable String username,
            Authentication auth) {

        if (auth.getName().equals(username)) {
            throw new BadRequestException("No puedes degradarte a ti mismo");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new BadRequestException("No se puede cambiar el rol de un usuario desactivado. Reactívalo primero.");
        }

        if (!"ADMIN".equals(usuario.getRol())) {
            throw new BadRequestException("El usuario no es ADMIN");
        }

        usuario.setRol("USER");
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario degradado a USER", null));
    }

    // aqui - Desactivar usuario (no puede hacer login)
    @PutMapping("/users/{username}/desactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(
            @PathVariable String username,
            Authentication auth) {

        if (auth.getName().equals(username)) {
            throw new BadRequestException("No puedes desactivarte a ti mismo");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new BadRequestException("El usuario ya esta desactivado");
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario desactivado", null));
    }

    // aqui - Reactivar usuario (puede hacer login de nuevo)
    @PutMapping("/users/{username}/reactivate")
    public ResponseEntity<ApiResponse<Void>> reactivateUser(@PathVariable String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (usuario.getActivo()) {
            throw new BadRequestException("El usuario ya esta activo");
        }

        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario reactivado", null));
    }

    // aqui - Ver detalles completos de un usuario
    @GetMapping("/users/{username}")
    public ResponseEntity<ApiResponse<UserDetailDTO>> getUserDetails(@PathVariable String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        UserDetailDTO details = new UserDetailDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getCreatedAt(),
                usuario.getLastLogin()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Detalles del usuario", details));
    }

    // aqui - Listar todos los usuarios
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserRoleDTO>>> listUsers() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UserRoleDTO> userRoles = usuarios.stream()
                .map(u -> new UserRoleDTO(u.getId(), u.getUsername(), u.getRol(), u.getActivo()))
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de usuarios", userRoles));
    }

    // aqui - DTO para lista de usuarios
    public record UserRoleDTO(Long id, String username, String rol, Boolean activo) {}

    // aqui - DTO para detalles completos
    public record UserDetailDTO(
            Long id,
            String username,
            String rol,
            Boolean activo,
            LocalDateTime createdAt,
            LocalDateTime lastLogin
    ) {}
}