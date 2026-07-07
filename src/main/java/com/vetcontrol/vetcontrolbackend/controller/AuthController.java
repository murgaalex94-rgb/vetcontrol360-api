package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Usuario;
import com.vetcontrol.vetcontrolbackend.repository.UsuarioRepository;
import com.vetcontrol.vetcontrolbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Map<String, LoginResponse> TOKENS = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        try {
            String raw = request.getReader().lines().collect(Collectors.joining());
            String body = raw != null && !raw.isEmpty() ? raw : "{}";
            @SuppressWarnings("unchecked")
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var tmp = mapper.readValue(body, java.util.LinkedHashMap.class);
            Map<String, Object> map = tmp;
            String username = map != null ? String.valueOf(map.get("username")) : null;
            String password = map != null ? String.valueOf(map.get("password")) : null;
            if (username == null || username.isEmpty() || username.equals("null")) {
                return ResponseEntity.status(400).body(Map.of("message", "Falta el campo username"));
            }
            Optional<Usuario> opt = usuarioRepository.findByUsuario(username);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        Usuario u = opt.get();
        if (!Boolean.TRUE.equals(u.getActivo())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        String storedHash = u.getPasswordHash();
        String hashPrefix = storedHash != null ? storedHash.substring(0, Math.min(30, storedHash.length())) : "null";
        boolean valid = false;
        if (storedHash != null && storedHash.contains(":")) {
            try {
                valid = PasswordHasher.verify(password, storedHash);
            } catch (Exception e) {
                Map<String, Object> err = new java.util.LinkedHashMap<>();
                err.put("message", "Error verificando hash");
                err.put("error", e.getClass().getName() + ": " + e.getMessage());
                return ResponseEntity.status(500).body(err);
            }
        } else if (storedHash != null) {
            valid = PasswordHasher.verifyLegacy(password, storedHash);
        }

        if (!valid) {
            // Fallback: comparación directa para depuración
            boolean directMatch = storedHash != null && storedHash.contains(password);
            Map<String, Object> debug = new java.util.LinkedHashMap<>();
            debug.put("message", "Credenciales inválidas");
            debug.put("activo", u.getActivo());
            debug.put("hashPrefix", hashPrefix);
            debug.put("hashLength", storedHash != null ? storedHash.length() : 0);
            debug.put("directMatch", directMatch);
            return ResponseEntity.status(401).body(debug);
        }

        String token = UUID.randomUUID().toString();
        Long expiracion = System.currentTimeMillis() + (10 * 60 * 1000); // 10 minutos
        LoginResponse resp = new LoginResponse(token, u.getId(), u.getNombreCompleto(), u.getIdRol(), expiracion);
        TOKENS.put(token, resp);

        return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Map<String, Object> err = new java.util.LinkedHashMap<>();
            err.put("message", "Error interno: " + e.getMessage());
            err.put("type", e.getClass().getName());
            return ResponseEntity.status(500).body(err);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "No autorizado"));
        }
        String token = auth.substring(7);
        LoginResponse resp = TOKENS.get(token);
        if (resp == null || System.currentTimeMillis() > resp.expiracion()) {
            if (resp != null) TOKENS.remove(token);
            return ResponseEntity.status(401).body(Map.of("message", "Token inválido o expirado"));
        }
        return ResponseEntity.ok(resp);
    }

    public static String getNombreCompletoFromToken(String token) {
        LoginResponse resp = TOKENS.get(token);
        if (resp != null && System.currentTimeMillis() <= resp.expiracion()) {
            return resp.nombreCompleto();
        }
        return null;
    }

    public static boolean validarToken(String token) {
        if (token == null || token.isEmpty()) return false;
        LoginResponse resp = TOKENS.get(token);
        if (resp == null || System.currentTimeMillis() > resp.expiracion()) {
            if (resp != null) TOKENS.remove(token);
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 300000)
    public void limpiarTokensExpirados() {
        TOKENS.entrySet().removeIf(entry ->
            System.currentTimeMillis() > entry.getValue().expiracion()
        );
    }

    record LoginResponse(String token, Integer userId, String nombreCompleto, Integer idRol, Long expiracion) {}
}
