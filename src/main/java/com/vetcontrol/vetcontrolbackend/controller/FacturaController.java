package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.config.SecurityUtil;
import com.vetcontrol.vetcontrolbackend.entity.Factura;
import com.vetcontrol.vetcontrolbackend.repository.FacturaRepository;
import com.vetcontrol.vetcontrolbackend.service.PDFGeneratorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        return ResponseEntity.ok(facturaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Factura factura, HttpServletRequest request) {
        log.info("=== RECIBIENDO NUEVA FACTURA ===");
        log.info("numero={}, fecha={}, cliente={}, mascota={}, total={}, estado={}, metodoPago={}, tipoComprobante={}",
                factura.getNumero(), factura.getFecha(), factura.getCliente(),
                factura.getMascota(), factura.getTotal(), factura.getEstado(),
                factura.getMetodoPago(), factura.getTipoComprobante());
        log.info("telefono={}, raza={}, clienteDoc={}, clienteDireccion={}",
                factura.getTelefono(), factura.getRaza(),
                factura.getClienteDoc(), factura.getClienteDireccion());
        try {
            if (factura.getEstado() == null) factura.setEstado("PENDIENTE");
            if (factura.getTipoComprobante() == null) factura.setTipoComprobante("FACTURA");
            var saved = facturaRepository.save(factura);
            log.info("Factura guardada correctamente con id={}", saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("Error al guardar factura en BD: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    java.util.Map.of("error", "Error al guardar la factura: " + e.getMessage()));
        }
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

    @GetMapping("/{id}/pdf")
    public void descargarPDF(@PathVariable Long id, HttpServletResponse response) {
        var factura = facturaRepository.findById(id).orElse(null);
        if (factura == null) {
            response.setStatus(404);
            return;
        }
        try {
            var pdf = pdfGeneratorService.generarFacturaPDF(factura);
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader("Content-Disposition", "inline; filename=Factura_" + factura.getNumero() + ".pdf");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}
