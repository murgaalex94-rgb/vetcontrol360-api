package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Proveedor;
import com.vetcontrol.vetcontrolbackend.repository.ProveedorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(proveedorRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Proveedor proveedor, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(proveedorRepository.save(proveedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(proveedorRepository.findById(id).orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Proveedor proveedor, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        proveedor.setId(id);
        return ResponseEntity.ok(proveedorRepository.save(proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        proveedorRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
