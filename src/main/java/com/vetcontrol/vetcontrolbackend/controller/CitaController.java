package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Cita;
import com.vetcontrol.vetcontrolbackend.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/citas")
@Transactional(readOnly = true)
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping
    public List<Cita> getAll() {
        return citaRepository.findAll();
    }

    @PostMapping
    @Transactional
    public Cita create(@RequestBody Cita cita) {
        return citaRepository.save(cita);
    }

    @GetMapping("/{id}")
    public Cita getById(@PathVariable Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Cita update(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setId(id);
        return citaRepository.save(cita);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        citaRepository.deleteById(id);
    }

    @GetMapping("/fecha/{fecha}")
    public List<Cita> getByFecha(@PathVariable String fecha) {
        return citaRepository.findByFecha(LocalDate.parse(fecha));
    }

    @GetMapping("/hoy")
    public List<Cita> getHoy() {
        return citaRepository.findByFecha(LocalDate.now());
    }
}
