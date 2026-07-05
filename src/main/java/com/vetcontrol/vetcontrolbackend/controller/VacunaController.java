package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Vacuna;
import com.vetcontrol.vetcontrolbackend.repository.VacunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacunas")
@Transactional(readOnly = true)
public class VacunaController {

    @Autowired
    private VacunaRepository vacunaRepository;

    @GetMapping
    public List<Vacuna> getAll() {
        return vacunaRepository.findAll();
    }

    @PostMapping
    @Transactional
    public Vacuna create(@RequestBody Vacuna vacuna) {
        return vacunaRepository.save(vacuna);
    }

    @GetMapping("/{id}")
    public Vacuna getById(@PathVariable Long id) {
        return vacunaRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Vacuna update(@PathVariable Long id, @RequestBody Vacuna vacuna) {
        vacuna.setId(id);
        return vacunaRepository.save(vacuna);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vacunaRepository.deleteById(id);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<Vacuna> getByMascotaId(@PathVariable Long mascotaId) {
        return vacunaRepository.findByMascotaId(mascotaId);
    }
}
