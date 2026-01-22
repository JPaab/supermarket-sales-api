package com.supermercados.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String rol = "USER";

    private Boolean activo = true;

    /// aqui - Fecha de creacion (se hace autosave al crear)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /// aqui - Ultimo login (se hace update cada vez que hace login)
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
