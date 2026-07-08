package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Mascota;
import com.vetcontrol.vetcontrolbackend.repository.MascotaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {
    
    @Autowired
    private MascotaRepository mascotaRepository;
    
    @GetMapping
    public List<Mascota> getAllMascotas(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false, defaultValue = "") String nombre) {
        Stream<Mascota> stream = mascotaRepository.findAll().stream();
        if (clienteId != null) {
            stream = stream.filter(m -> m.getCliente() != null && m.getCliente().getId().equals(clienteId));
        }
        if (!nombre.isEmpty()) {
            String q = nombre.toLowerCase();
            stream = stream.filter(m -> m.getNombre().toLowerCase().contains(q));
        }
        return stream.toList();
    }
    
    @PostMapping
    public ResponseEntity<?> createMascota(@RequestBody Mascota mascota, HttpServletRequest request) {
        if (SecurityUtil.getIdRol(request) == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        return ResponseEntity.ok(mascotaRepository.save(mascota));
    }
    
    @GetMapping("/{id}")
    public Mascota getMascotaById(@PathVariable Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMascota(@PathVariable Long id, @RequestBody Mascota mascota, HttpServletRequest request) {
        var rol = SecurityUtil.getIdRol(request);
        if (rol == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        mascota.setId(id);
        return ResponseEntity.ok(mascotaRepository.save(mascota));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMascota(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) {
            return SecurityUtil.forbidden();
        }
        mascotaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/buscar")
    public List<Mascota> search(@RequestParam String q) {
        return mascotaRepository.findByNombreContainingIgnoreCase(q);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Mascota> getMascotasByCliente(@PathVariable Long clienteId) {
        return mascotaRepository.findAll().stream()
                .filter(m -> m.getCliente() != null && m.getCliente().getId().equals(clienteId))
                .toList();
    }

}
