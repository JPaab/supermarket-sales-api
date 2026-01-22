package com.supermercados.api.services;

import com.supermercados.api.dtos.auth.AuthResponseDTO;
import com.supermercados.api.dtos.auth.LoginRequestDTO;
import com.supermercados.api.dtos.auth.RegisterRequestDTO;
import com.supermercados.api.exceptions.BadRequestException;
import com.supermercados.api.models.Usuario;
import com.supermercados.api.repositories.UsuarioRepository;
import com.supermercados.api.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    /// aqui - AuthenticationManager se usa para autenticar username/password con Spring Security
    private final AuthenticationManager authenticationManager;

    /// aqui - Repository para crear y buscar usuarios
    private final UsuarioRepository usuarioRepository;

    /// aqui - PasswordEncoder para guardar password encriptado
    private final PasswordEncoder passwordEncoder;

    /// aqui - JwtUtil para generar token
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /// aqui - Registro: crea usuario y devuelve token
    public AuthResponseDTO register(RegisterRequestDTO request) {

        /// aqui - Validar username duplicado
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El username ya existe");
        }

        /// aqui - Crear usuario nuevo (rol USER por defecto)
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol("USER");
        usuario.setActivo(true);
        usuario.setCreatedAt(LocalDateTime.now());  /// aqui - Fecha de creacion

        Usuario saved = usuarioRepository.save(usuario);

        /// aqui - Genera el token
        String token = jwtUtil.generateToken(saved.getUsername(), saved.getRol());

        return new AuthResponseDTO(token, "Bearer", saved.getId(), saved.getUsername(), saved.getRol());
    }

    /// aqui - Login: autentica con Spring y devuelve token
    public AuthResponseDTO login(LoginRequestDTO request) {

        /// aqui - Autenticar con Spring Security (si falla, te salta BadCredentialsException -> 401)
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        /// aqui - Obtiene el username autenticado
        String username = authentication.getName();

        /// aqui - Carga el usuario para obtener id y rol
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas"));

        /// aqui - Actualizar ultimo login
        usuario.setLastLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);  /// aqui - el save del update

        /// aqui - Genera el token
        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol());

        return new AuthResponseDTO(token, "Bearer", usuario.getId(), usuario.getUsername(), usuario.getRol());
    }
}