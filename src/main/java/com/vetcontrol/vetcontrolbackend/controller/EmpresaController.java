package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Empresa;
import com.vetcontrol.vetcontrolbackend.service.EmpresaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> obtener(HttpServletRequest request) {
        return ResponseEntity.ok(service.obtener());
    }

    @PutMapping
    public ResponseEntity<?> actualizar(@RequestBody Empresa empresa, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        try {
            return ResponseEntity.ok(service.actualizar(empresa));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al guardar datos de la empresa: " + e.getMessage()));
        }
    }
}
