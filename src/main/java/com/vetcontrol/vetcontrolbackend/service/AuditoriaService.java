package com.vetcontrol.vetcontrolbackend.service;

import com.vetcontrol.vetcontrolbackend.entity.Auditoria;
import com.vetcontrol.vetcontrolbackend.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public void registrar(String usuario, String accion, String modulo,
                          String descripcion, String ipDispositivo,
                          String navegador, String severidad, String estado) {
        Auditoria a = new Auditoria();
        a.setFechaHora(LocalDateTime.now());
        a.setUsuario(usuario);
        a.setAccion(accion);
        a.setModulo(modulo);
        a.setDescripcion(descripcion);
        a.setIpDispositivo(ipDispositivo);
        a.setNavegador(navegador);
        a.setSeveridad(severidad);
        a.setEstado(estado);
        auditoriaRepository.save(a);
    }

    public List<Auditoria> listarTodos() {
        return auditoriaRepository.findAllByOrderByFechaHoraDesc();
    }

    public List<Auditoria> ultimos5() {
        return auditoriaRepository.findTop5ByOrderByFechaHoraDesc();
    }
}
