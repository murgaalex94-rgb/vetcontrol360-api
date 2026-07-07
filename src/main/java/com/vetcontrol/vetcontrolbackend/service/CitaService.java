package com.vetcontrol.vetcontrolbackend.service;

import com.vetcontrol.vetcontrolbackend.entity.Cita;
import com.vetcontrol.vetcontrolbackend.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional
    public Cita create(Cita cita) {
        Cita saved = citaRepository.save(cita);
        enviarRecordatorioSiCorresponde(saved);
        return saved;
    }

    @Transactional
    public Cita update(Long id, Cita cita) {
        cita.setId(id);
        Cita saved = citaRepository.save(cita);
        enviarRecordatorioSiCorresponde(saved);
        return saved;
    }

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> findByFecha(java.time.LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    private void enviarRecordatorioSiCorresponde(Cita cita) {
        if (cita.getWhatsapp() == null || !cita.getWhatsapp()) return;
        if (cita.getCliente() == null) return;

        String whatsappNumber = cita.getCliente().getWhatsapp();
        if (whatsappNumber == null || whatsappNumber.isEmpty()) {
            whatsappNumber = cita.getCliente().getTelefono();
        }
        if (whatsappNumber == null || whatsappNumber.isEmpty()) return;

        String numeroLimpio = whatsappNumber.replaceAll("[^0-9]", "");
        if (numeroLimpio.isEmpty()) return;

        String clienteNombre = cita.getCliente().getNombre() + " " + (cita.getCliente().getApellidos() != null ? cita.getCliente().getApellidos() : "");
        String mascotaNombre = cita.getMascota() != null ? cita.getMascota().getNombre() : "su mascota";
        String fecha = cita.getFecha() != null ? cita.getFecha().toString() : "";
        String hora = cita.getHora() != null ? cita.getHora().toString() : "";
        String veterinario = cita.getVeterinario() != null ? cita.getVeterinario() : "";
        String motivo = cita.getMotivo() != null ? cita.getMotivo() : "";

        try {
            whatsAppService.sendReminder(numeroLimpio, clienteNombre, mascotaNombre, fecha, hora, veterinario, motivo);
        } catch (Exception e) {
            auditoriaService.registrar(
                    "Sistema",
                    "ENVIO_WHATSAPP",
                    "CITAS",
                    "Error al enviar recordatorio de cita ID " + cita.getId() + ": " + e.getMessage(),
                    null,
                    null,
                    "ERROR",
                    "FALLIDO"
            );
        }
    }
}
