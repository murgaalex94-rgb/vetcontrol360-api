package com.vetcontrol.vetcontrolbackend.repository;

import com.vetcontrol.vetcontrolbackend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByProveedor(String proveedor);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByStockActualLessThan(Integer stockMinimo);
    List<Producto> findByStockActual(Integer stock);
    @Query("SELECT p FROM Producto p WHERE p.fechaVencimiento IS NOT NULL AND p.fechaVencimiento BETWEEN :start AND :end ORDER BY p.fechaVencimiento ASC")
    List<Producto> findProximosAVencer(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
