package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Usuario;
import com.vetcontrol.vetcontrolbackend.repository.UsuarioRepository;
import com.vetcontrol.vetcontrolbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Map<String, LoginResponse> TOKENS = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByUsuario(req.username());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        Usuario u = opt.get();
        if (!Boolean.TRUE.equals(u.getActivo())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        String hash = u.getPasswordHash();
        boolean valid = false;
        if (hash != null && hash.contains(":")) {
            valid = PasswordHasher.verify(req.password(), hash);
        } else if (hash != null) {
            valid = PasswordHasher.verifyLegacy(req.password(), hash);
        }

        if (!valid) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        String token = UUID.randomUUID().toString();
        LoginResponse resp = new LoginResponse(token, u.getId(), u.getNombreCompleto(), u.getIdRol());
        TOKENS.put(token, resp);

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "No autorizado"));
        }
        String token = auth.substring(7);
        LoginResponse resp = TOKENS.get(token);
        if (resp == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Token inválido"));
        }
        return ResponseEntity.ok(resp);
    }

    record LoginRequest(String username, String password) {}
    record LoginResponse(String token, Integer userId, String nombreCompleto, Integer idRol) {}
}
