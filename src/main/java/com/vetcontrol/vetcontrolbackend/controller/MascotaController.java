package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Mascota;
import com.vetcontrol.vetcontrolbackend.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {
    
    @Autowired
    private MascotaRepository mascotaRepository;
    
    @GetMapping
    public List<Mascota> getAllMascotas() {
        return mascotaRepository.findAll();
    }
    
    @PostMapping
    public Mascota createMascota(@RequestBody Mascota mascota) {
        return mascotaRepository.save(mascota);
    }
    
    @GetMapping("/{id}")
    public Mascota getMascotaById(@PathVariable Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }
    
    @PutMapping("/{id}")
    public Mascota updateMascota(@PathVariable Long id, @RequestBody Mascota mascota) {
        mascota.setId(id);
        return mascotaRepository.save(mascota);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMascota(@PathVariable Long id) {
        mascotaRepository.deleteById(id);
    }
    
    @GetMapping("/cliente/{clienteId}")
    public List<Mascota> getMascotasByCliente(@PathVariable Long clienteId) {
        return mascotaRepository.findAll().stream()
                .filter(m -> m.getCliente() != null && m.getCliente().getId().equals(clienteId))
                .toList();
    }
}
