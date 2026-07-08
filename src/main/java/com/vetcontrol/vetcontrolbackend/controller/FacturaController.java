package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Factura;
import com.vetcontrol.vetcontrolbackend.repository.FacturaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        return ResponseEntity.ok(facturaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Factura factura, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(facturaRepository.findById(id).orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Factura factura, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        factura.setId(id);
        return ResponseEntity.ok(facturaRepository.save(factura));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        facturaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getByEstado(@PathVariable String estado, HttpServletRequest request) {
        return ResponseEntity.ok(facturaRepository.findByEstado(estado));
    }
}
