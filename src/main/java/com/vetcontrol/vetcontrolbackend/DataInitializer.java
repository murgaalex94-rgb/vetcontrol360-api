package com.vetcontrol.vetcontrolbackend;

import com.vetcontrol.vetcontrolbackend.entity.*;
import com.vetcontrol.vetcontrolbackend.repository.*;
import com.vetcontrol.vetcontrolbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private MascotaRepository mascotaRepository;
    @Autowired private VacunaRepository vacunaRepository;
    @Autowired private CitaRepository citaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private FacturaRepository facturaRepository;

    @Override
    public void run(String... args) {
        // Solo crear usuario admin para login rápido
        if (usuarioRepository.count() == 0) {
            Usuario u = new Usuario();
            u.setUsuario("admin");
            u.setPasswordHash(PasswordHasher.hash("admin123"));
            u.setNombreCompleto("Administrador");
            u.setActivo(true);
            u.setIdRol(1);
            usuarioRepository.save(u);
            System.out.println(">>> Usuario admin creado (password: admin123)");
        } else {
            // Always ensure admin has correct PBKDF2 hash
            usuarioRepository.findByUsuario("admin").ifPresent(u -> {
                u.setPasswordHash(PasswordHasher.hash("admin123"));
                usuarioRepository.save(u);
                System.out.println(">>> Hash de admin re-generado con PBKDF2");
            });
        }
        
        // Resto desactivado - usar populate_vetcontrol.sql
        System.out.println(">>> DataInitializer: Solo usuario admin creado. Ejecuta populate_vetcontrol.sql para datos completos.");
    }
}
