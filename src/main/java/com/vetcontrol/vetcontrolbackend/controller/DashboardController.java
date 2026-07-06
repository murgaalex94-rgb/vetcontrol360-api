package com.vetcontrol.vetcontrolbackend.controller;

import com.vetcontrol.vetcontrolbackend.entity.Factura;
import com.vetcontrol.vetcontrolbackend.entity.Producto;
import com.vetcontrol.vetcontrolbackend.repository.CitaRepository;
import com.vetcontrol.vetcontrolbackend.repository.FacturaRepository;
import com.vetcontrol.vetcontrolbackend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/resumen")
    public Map<String, Object> getResumen() {
        var hoy = LocalDate.now();
        var citasHoy = citaRepository.findByFecha(hoy).size();
        var pacientesAtendidos = citaRepository.findAll().stream()
                .map(c -> c.getMascota() != null ? c.getMascota().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        var facturasHoy = facturaRepository.findByFecha(hoy);
        var ventasDia = facturasHoy.stream()
                .filter(f -> f.getTotal() != null)
                .map(Factura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var todosProductos = productoRepository.findAll();
        var stockBajo = 0;
        var sinStock = 0;
        for (var p : todosProductos) {
            var stock = p.getStockActual();
            var minimo = p.getStockMinimo();
            if (stock == null || stock == 0) {
                sinStock++;
            } else if (minimo != null && stock < minimo) {
                stockBajo++;
            }
        }
        var pendientesPago = facturaRepository.findByEstado("Pendiente").size();

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("citasHoy", citasHoy);
        res.put("pacientesAtendidos", (int) pacientesAtendidos);
        res.put("ventasDia", ventasDia.doubleValue());
        res.put("stockBajo", stockBajo + sinStock);
        res.put("pendientesPago", pendientesPago);
        return res;
    }

    @GetMapping("/ventas")
    public List<Map<String, Object>> getVentas() {
        var hoy = LocalDate.now();
        var inicio = hoy.minusDays(6);
        var facturas = facturaRepository.findByFechaBetween(inicio, hoy);

        Map<LocalDate, BigDecimal> ventasPorDia = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            ventasPorDia.put(inicio.plusDays(i), BigDecimal.ZERO);
        }
        for (var f : facturas) {
            if (f.getTotal() != null) {
                ventasPorDia.merge(f.getFecha(), f.getTotal(), BigDecimal::add);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (var entry : ventasPorDia.entrySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("dia", String.valueOf(entry.getKey().getDayOfMonth()));
            item.put("ventas", entry.getValue().setScale(2, RoundingMode.HALF_UP).doubleValue());
            result.add(item);
        }
        return result;
    }

    @GetMapping("/inventario/resumen")
    public List<Map<String, Object>> getInventarioResumen() {
        var todos = productoRepository.findAll();
        var normal = 0;
        var bajo = 0;
        var sinStock = 0;
        for (var p : todos) {
            var stock = p.getStockActual();
            var minimo = p.getStockMinimo();
            if (stock == null || stock == 0) {
                sinStock++;
            } else if (minimo != null && stock < minimo) {
                bajo++;
            } else {
                normal++;
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        result.add(Map.of("nombre", "Stock Normal", "valor", normal, "color", "#10B981"));
        result.add(Map.of("nombre", "Stock Bajo", "valor", bajo, "color", "#F59E0B"));
        result.add(Map.of("nombre", "Sin Stock", "valor", sinStock, "color", "#6B7280"));
        return result;
    }

    @GetMapping("/inventario/proximos-a-vencer")
    public List<Map<String, Object>> getProximosAVencer() {
        var hoy = LocalDate.now();
        var fin = hoy.plusDays(90);
        var productos = productoRepository.findProximosAVencer(hoy, fin);

        List<Map<String, Object>> result = new ArrayList<>();
        for (var p : productos) {
            if (p.getFechaVencimiento() == null) continue;
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("nombre", p.getNombre());
            item.put("dias", (int) ChronoUnit.DAYS.between(hoy, p.getFechaVencimiento()));
            item.put("lote", p.getLote() != null ? p.getLote() : "—");
            result.add(item);
        }
        return result;
    }
}
