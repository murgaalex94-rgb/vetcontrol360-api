package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.dto.EnvioFacturaRequest;
import com.vetcontrol.vetcontrolbackend.entity.ComprobanteElectronico;
import com.vetcontrol.vetcontrolbackend.entity.Factura;
import com.vetcontrol.vetcontrolbackend.repository.ComprobanteElectronicoRepository;
import com.vetcontrol.vetcontrolbackend.repository.FacturaRepository;
import com.vetcontrol.vetcontrolbackend.service.FacturaElectronicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
public class FacturaElectronicaController {

    private final FacturaElectronicaService service;
    private final ComprobanteElectronicoRepository comprobanteRepo;
    private final FacturaRepository facturaRepo;

    public FacturaElectronicaController(FacturaElectronicaService service,
                                        ComprobanteElectronicoRepository comprobanteRepo,
                                        FacturaRepository facturaRepo) {
        this.service = service;
        this.comprobanteRepo = comprobanteRepo;
        this.facturaRepo = facturaRepo;
    }

    @PostMapping("/{id}/enviar-sunat")
    public ResponseEntity<?> enviarSunat(@PathVariable Long id, @RequestBody EnvioFacturaRequest request) {
        var factura = facturaRepo.findById(id).orElse(null);
        if (factura == null) {
            return ResponseEntity.notFound().build();
        }
        request.setFacturaId(id);
        request.setNumero(factura.getNumero());

        try {
            ComprobanteElectronico resultado = service.enviar(request);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al enviar a SUNAT: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/comprobantes")
    public ResponseEntity<List<ComprobanteElectronico>> listarComprobantes(@PathVariable Long id) {
        return ResponseEntity.ok(comprobanteRepo.findAll());
    }

    @GetMapping("/electronica/todos")
    public ResponseEntity<List<ComprobanteElectronico>> listarTodos() {
        return ResponseEntity.ok(comprobanteRepo.findAll());
    }
}
