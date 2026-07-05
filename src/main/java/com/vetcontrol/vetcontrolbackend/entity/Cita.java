package com.vetcontrol.vetcontrolbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    private String tipoCita;

    private LocalDate fecha;

    private LocalTime hora;

    private String motivo;

    private Integer duracion;

    private String veterinario;

    private String consultorio;

    private String notas;

    private Boolean recordatorio;

    private Boolean whatsapp;

    private Boolean email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public String getTipoCita() { return tipoCita; }
    public void setTipoCita(String tipoCita) { this.tipoCita = tipoCita; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
    public String getVeterinario() { return veterinario; }
    public void setVeterinario(String veterinario) { this.veterinario = veterinario; }
    public String getConsultorio() { return consultorio; }
    public void setConsultorio(String consultorio) { this.consultorio = consultorio; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Boolean getRecordatorio() { return recordatorio; }
    public void setRecordatorio(Boolean recordatorio) { this.recordatorio = recordatorio; }
    public Boolean getWhatsapp() { return whatsapp; }
    public void setWhatsapp(Boolean whatsapp) { this.whatsapp = whatsapp; }
    public Boolean getEmail() { return email; }
    public void setEmail(Boolean email) { this.email = email; }
}
