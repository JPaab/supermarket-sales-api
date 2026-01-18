package com.supermercados.api.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String token; // aqui seria mas que todo para el JWT generado
    private String type = "Bearer"; // aqui se pone Bearer siempre para el HTTP
    private Long id;
    private String username;
    private String rol;
}
