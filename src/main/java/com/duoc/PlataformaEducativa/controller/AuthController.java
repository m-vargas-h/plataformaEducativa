package com.duoc.PlataformaEducativa.controller;

import com.duoc.PlataformaEducativa.model.Rol;
import com.duoc.PlataformaEducativa.model.Usuario;
import com.duoc.PlataformaEducativa.repository.UsuarioRepository;
import com.duoc.PlataformaEducativa.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    

    // SOLO PARA PRUEBAS - ELIMINAR DESPUÉS
    //@GetMapping("/test-hash")
    //public String testHash() {
    //return passwordEncoder.encode("password123");
    //}

    // POST /auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El email ya está registrado"));
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Si no se especifica rol, por defecto es ALUMNO
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ALUMNO);
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Usuario registrado exitosamente",
                        "id", guardado.getId(),
                        "email", guardado.getEmail(),
                        "rol", guardado.getRol()
                ));
    }

    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.get("email"),
                        credentials.get("password")
                )
        );

        UserDetails userDetails = userDetailsService
                .loadUserByUsername(credentials.get("email"));

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", userDetails.getUsername(),
                "rol", userDetails.getAuthorities().toString()
        ));
    }
}