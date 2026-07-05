package com.vetcontrol.vetcontrolbackend.repository;

import com.vetcontrol.vetcontrolbackend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByProveedor(String proveedor);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
