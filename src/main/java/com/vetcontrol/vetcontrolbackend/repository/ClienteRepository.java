package com.vetcontrol.vetcontrolbackend.repository;

import com.vetcontrol.vetcontrolbackend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
