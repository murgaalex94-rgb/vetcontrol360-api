package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.ConsultaMedica;
import com.vetcontrol.vetcontrolbackend.repository.ConsultaMedicaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas-medicas")
public class ConsultaMedicaController {

    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @GetMapping
    public List<ConsultaMedica> getAll(@RequestParam(required = false) Long mascotaId) {
        if (mascotaId != null) {
            return consultaMedicaRepository.findByMascotaIdOrderByFechaDesc(mascotaId);
        }
        return consultaMedicaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ConsultaMedica getById(@PathVariable Long id) {
        return consultaMedicaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ConsultaMedica create(@RequestBody ConsultaMedica consultaMedica) {
        return consultaMedicaRepository.save(consultaMedica);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ConsultaMedica consultaMedica, HttpServletRequest request) {
        if (SecurityUtil.getIdRol(request) == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        consultaMedica.setId(id);
        return ResponseEntity.ok(consultaMedicaRepository.save(consultaMedica));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) {
            return SecurityUtil.forbidden();
        }
        consultaMedicaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
