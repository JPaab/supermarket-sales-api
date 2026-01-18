package com.supermercados.api.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username obligatorio")
    private String username;

    @NotBlank(message = "Password obligatorio")
    private String password;

}
