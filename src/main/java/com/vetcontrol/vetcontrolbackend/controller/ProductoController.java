package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Producto;
import com.vetcontrol.vetcontrolbackend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public Producto getById(@PathVariable Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Producto update(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }

    @GetMapping("/buscar")
    public List<Producto> search(@RequestParam String q) {
        return productoRepository.findByNombreContainingIgnoreCase(q);
    }
}
