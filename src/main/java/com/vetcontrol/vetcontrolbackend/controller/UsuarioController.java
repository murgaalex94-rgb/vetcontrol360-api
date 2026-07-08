package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Usuario;
import com.vetcontrol.vetcontrolbackend.repository.UsuarioRepository;
import com.vetcontrol.vetcontrolbackend.security.PasswordHasher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private com.vetcontrol.vetcontrolbackend.DataInitializer dataInitializer;

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        String username = (String) body.get("usuario");
        String password = (String) body.get("password");
        String nombreCompleto = (String) body.get("nombreCompleto");
        Integer idRol = body.get("idRol") != null ? Integer.valueOf(body.get("idRol").toString()) : 2;

        if (username == null || password == null || nombreCompleto == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos obligatorios"));
        }

        if (usuarioRepository.findByUsuario(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El nombre de usuario ya existe"));
        }

        Usuario u = new Usuario();
        u.setUsuario(username);
        u.setPasswordHash(PasswordHasher.hash(password));
        u.setNombreCompleto(nombreCompleto);
        u.setActivo(true);
        u.setIdRol(idRol);
        usuarioRepository.save(u);
        return ResponseEntity.ok(u);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, Object> body, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return usuarioRepository.findById(id).map(u -> {
            if (body.containsKey("usuario")) u.setUsuario((String) body.get("usuario"));
            if (body.containsKey("nombreCompleto")) u.setNombreCompleto((String) body.get("nombreCompleto"));
            if (body.containsKey("idRol")) u.setIdRol(Integer.valueOf(body.get("idRol").toString()));
            if (body.containsKey("activo")) u.setActivo(Boolean.valueOf(body.get("activo").toString()));
            if (body.containsKey("password") && body.get("password") != null && !((String) body.get("password")).isEmpty()) {
                u.setPasswordHash(PasswordHasher.hash((String) body.get("password")));
            }
            usuarioRepository.save(u);
            return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Usuario eliminado"));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/populate-data")
    public ResponseEntity<?> populateData(HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        try {
            dataInitializer.forcePopulateData();
            return ResponseEntity.ok(Map.of("message", "Datos de ejemplo poblados exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
