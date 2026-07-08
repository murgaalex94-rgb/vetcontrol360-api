package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Proveedor;
import com.vetcontrol.vetcontrolbackend.repository.ProductoRepository;
import com.vetcontrol.vetcontrolbackend.repository.ProveedorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        List<Map<String, Object>> result = proveedores.stream().map(p -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", p.getId());
            item.put("codigo", p.getCodigo());
            item.put("nombre", p.getNombre());
            item.put("ruc", p.getRuc());
            item.put("rubro", p.getRubro());
            item.put("telefono", p.getTelefono());
            item.put("email", p.getEmail());
            item.put("estado", p.getEstado());
            item.put("direccion", p.getDireccion());
            item.put("totalProductos", productoRepository.findByProveedor(p.getNombre()).size());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Proveedor proveedor, HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        return ResponseEntity.ok(proveedorRepository.save(proveedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Proveedor prov = proveedorRepository.findById(id).orElse(null);
        if (prov == null) return ResponseEntity.notFound().build();
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", prov.getId());
        item.put("codigo", prov.getCodigo());
        item.put("nombre", prov.getNombre());
        item.put("ruc", prov.getRuc());
        item.put("rubro", prov.getRubro());
        item.put("telefono", prov.getTelefono());
        item.put("email", prov.getEmail());
        item.put("estado", prov.getEstado());
        item.put("direccion", prov.getDireccion());
        item.put("totalProductos", productoRepository.findByProveedor(prov.getNombre()).size());
        return ResponseEntity.ok(item);
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

    @PostMapping("/sincronizar")
    public ResponseEntity<?> sincronizarProveedores(HttpServletRequest request) {
        if (!SecurityUtil.isAdmin(request)) return SecurityUtil.forbidden();
        
        // Obtener todos los productos con proveedores
        List<String> proveedoresEnProductos = productoRepository.findAll()
            .stream()
            .map(p -> p.getProveedor())
            .filter(p -> p != null && !p.trim().isEmpty())
            .distinct()
            .collect(Collectors.toList());
        
        // Obtener todos los proveedores existentes
        List<String> proveedoresExistentes = proveedorRepository.findAll()
            .stream()
            .map(p -> p.getNombre().toLowerCase().trim())
            .collect(Collectors.toList());
        
        // Crear proveedores que no existen
        List<Proveedor> nuevosProveedores = new java.util.ArrayList<>();
        int contador = 1;
        
        for (String nombreProv : proveedoresEnProductos) {
            String nombreNormalizado = nombreProv.trim();
            if (!proveedoresExistentes.contains(nombreNormalizado.toLowerCase())) {
                Proveedor nuevo = new Proveedor();
                nuevo.setCodigo("PROV-" + String.format("%04d", contador++));
                nuevo.setNombre(nombreNormalizado);
                nuevo.setRuc(generarRucFicticio());
                nuevo.setRubro("Farmacia Veterinaria");
                nuevo.setEstado("Activo");
                nuevosProveedores.add(nuevo);
            }
        }
        
        // Guardar los nuevos proveedores
        List<Proveedor> proveedoresGuardados = proveedorRepository.saveAll(nuevosProveedores);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mensaje", "Sincronización completada");
        response.put("proveedores_creados", proveedoresGuardados.size());
        response.put("proveedores", proveedoresGuardados);
        
        return ResponseEntity.ok(response);
    }
    
    private String generarRucFicticio() {
        // Generar RUC ficticio de 11 dígitos comenzando con 20 (persona jurídica)
        java.util.Random random = new java.util.Random();
        return "20" + String.format("%08d", random.nextInt(100000000));
    }
}
