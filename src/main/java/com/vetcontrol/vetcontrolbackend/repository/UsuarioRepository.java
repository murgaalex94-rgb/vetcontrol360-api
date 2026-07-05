package com.vetcontrol.vetcontrolbackend.repository;

import com.vetcontrol.vetcontrolbackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuario(String usuario);
}
