package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Cliente;
import com.vetcontrol.vetcontrolbackend.repository.ClienteRepository;
import com.vetcontrol.vetcontrolbackend.repository.MascotaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;
    
    @GetMapping
    public List<Cliente> getAllClientes() {
        var clientes = clienteRepository.findAll();
        clientes.forEach(c -> c.setMascotasCount(mascotaRepository.countByClienteId(c.getId())));
        return clientes;
    }
    
    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente, HttpServletRequest request) {
        if (SecurityUtil.getIdRol(request) == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        return ResponseEntity.ok(clienteRepository.save(cliente));
    }
    
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id).orElse(null);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente, HttpServletRequest request) {
        var rol = SecurityUtil.getIdRol(request);
        if (rol == SecurityUtil.ROL_ASISTENTE) {
            return SecurityUtil.forbidden();
        }
        cliente.setId(id);
        return ResponseEntity.ok(clienteRepository.save(cliente));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) {
            return SecurityUtil.forbidden();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
