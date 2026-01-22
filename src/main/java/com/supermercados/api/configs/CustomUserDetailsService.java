package com.supermercados.api.configs;

import com.supermercados.api.models.Usuario;
import com.supermercados.api.repositories.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // aqui - Repositorio para buscar usuarios en BD por username
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // aqui - Buscar usuario en BD (si no existe, Spring Security no permite autenticar)
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // aqui - Convertir nuestro Usuario a un UserDetails que Spring Security entiende
        // aqui - Importante: el rol debe llevar prefijo ROLE_ para que hasRole/hasAnyRole funcione bien
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())))
                // aqui - Si el usuario esta inactivo, se marca como disabled y no podra autenticarse
                .disabled(usuario.getActivo() != null && !usuario.getActivo())
                .build();
    }
}