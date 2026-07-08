package com.vetcontrol.vetcontrolbackend.service;

import com.vetcontrol.vetcontrolbackend.entity.Empresa;
import com.vetcontrol.vetcontrolbackend.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository repo;

    public EmpresaService(EmpresaRepository repo) {
        this.repo = repo;
    }

    public Empresa obtener() {
        List<Empresa> todas = repo.findAll();
        if (todas.isEmpty()) {
            var porDefecto = new Empresa();
            porDefecto.setRuc("20600085510");
            porDefecto.setRazonSocial("MI EMPRESA SAC");
            porDefecto.setNombreComercial("VetControl 360");
            porDefecto.setDireccion("Av. Principal 123");
            porDefecto.setEstado("Activo");
            return repo.save(porDefecto);
        }
        return todas.get(0);
    }

    public Empresa actualizar(Empresa empresa) {
        Empresa existente = obtener();
        empresa.setId(existente.getId());
        return repo.save(empresa);
    }
}
