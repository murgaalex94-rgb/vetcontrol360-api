package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Auditoria;
import com.vetcontrol.vetcontrolbackend.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    public List<Auditoria> getAllAuditoria() {
        return auditoriaService.listarTodos();
    }

    @GetMapping("/recientes")
    public List<Auditoria> getUltimos5() {
        return auditoriaService.ultimos5();
    }
}
