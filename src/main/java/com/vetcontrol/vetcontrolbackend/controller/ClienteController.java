package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Cliente;
import com.vetcontrol.vetcontrolbackend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }
    
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id).orElse(null);
    }
    
    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }
    
    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}
