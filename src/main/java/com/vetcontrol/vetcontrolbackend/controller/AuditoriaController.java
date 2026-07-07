package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Auditoria;
import com.vetcontrol.vetcontrolbackend.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<?> getAllAuditoria(HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(auditoriaService.listarTodos());
    }

    @GetMapping("/recientes")
    public ResponseEntity<?> getUltimos5(HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(auditoriaService.ultimos5());
    }
}
