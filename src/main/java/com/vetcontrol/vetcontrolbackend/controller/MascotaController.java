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
        System.out.println("[DIAG BACKEND] GET /api/mascotas | clienteId recibido: " + clienteId + " | nombre: '" + nombre + "'");
        List<Mascota> todas = mascotaRepository.findAll();
        System.out.println("[DIAG BACKEND] Total mascotas en BD: " + todas.size());
        for (Mascota m : todas) {
            Long cId = (m.getCliente() != null) ? m.getCliente().getId() : null;
            String cNom = (m.getCliente() != null) ? m.getCliente().getNombre() : "null";
            System.out.println("[DIAG BACKEND]   mascota id=" + m.getId() + " nombre='" + m.getNombre() + "' cliente_id=" + cId + " cliente_nombre=" + cNom);
        }
        Stream<Mascota> stream = todas.stream();
        if (clienteId != null) {
            stream = stream.filter(m -> m.getCliente() != null && m.getCliente().getId().equals(clienteId));
        }
        if (!nombre.isEmpty()) {
            String q = nombre.toLowerCase();
            stream = stream.filter(m -> m.getNombre().toLowerCase().contains(q));
        }
        List<Mascota> resultado = stream.toList();
        System.out.println("[DIAG BACKEND] Resultado filtrado: " + resultado.size() + " mascotas");
        return resultado;
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
