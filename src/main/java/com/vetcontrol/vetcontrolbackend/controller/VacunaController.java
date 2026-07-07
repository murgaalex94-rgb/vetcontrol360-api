package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Vacuna;
import com.vetcontrol.vetcontrolbackend.repository.VacunaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Vacuna vacuna, HttpServletRequest request) {
        if (SecurityUtil.getIdRol(request) == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        vacuna.setId(id);
        return ResponseEntity.ok(vacunaRepository.save(vacuna));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) {
            return SecurityUtil.forbidden();
        }
        vacunaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<Vacuna> getByMascotaId(@PathVariable Long mascotaId) {
        return vacunaRepository.findByMascotaId(mascotaId);
    }
}
