package com.supermercados.api.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username obligatorio")
    private String username;

    @NotBlank(message = "Password obligatorio")
    private String password;
}