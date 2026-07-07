package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Cita;
import com.vetcontrol.vetcontrolbackend.service.CitaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public List<Cita> getAll() {
        return citaService.findAll();
    }

    @PostMapping
    public Cita create(@RequestBody Cita cita) {
        return citaService.create(cita);
    }

    @GetMapping("/{id}")
    public Cita getById(@PathVariable Long id) {
        return citaService.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Cita cita, HttpServletRequest request) {
        if (SecurityUtil.getIdRol(request) == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        return ResponseEntity.ok(citaService.update(id, cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) {
            return SecurityUtil.forbidden();
        }
        citaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fecha/{fecha}")
    public List<Cita> getByFecha(@PathVariable String fecha) {
        return citaService.findByFecha(LocalDate.parse(fecha));
    }

    @GetMapping("/hoy")
    public List<Cita> getHoy() {
        return citaService.findByFecha(LocalDate.now());
    }
}
