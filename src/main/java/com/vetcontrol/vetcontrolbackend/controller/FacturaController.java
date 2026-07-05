package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Factura;
import com.vetcontrol.vetcontrolbackend.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @GetMapping
    public List<Factura> getAll() {
        return facturaRepository.findAll();
    }

    @PostMapping
    public Factura create(@RequestBody Factura factura) {
        return facturaRepository.save(factura);
    }

    @GetMapping("/{id}")
    public Factura getById(@PathVariable Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Factura update(@PathVariable Long id, @RequestBody Factura factura) {
        factura.setId(id);
        return facturaRepository.save(factura);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        facturaRepository.deleteById(id);
    }

    @GetMapping("/estado/{estado}")
    public List<Factura> getByEstado(@PathVariable String estado) {
        return facturaRepository.findByEstado(estado);
    }
}
