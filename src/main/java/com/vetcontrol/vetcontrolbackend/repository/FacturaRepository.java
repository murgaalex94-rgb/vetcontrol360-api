package com.vetcontrol.vetcontrolbackend.repository;

import com.vetcontrol.vetcontrolbackend.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByEstado(String estado);
    List<Factura> findByClienteContainingIgnoreCase(String cliente);
    List<Factura> findByFecha(LocalDate fecha);
    List<Factura> findByFechaBetween(LocalDate start, LocalDate end);
}
