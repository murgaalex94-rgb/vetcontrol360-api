package com.vetcontrol.vetcontrolbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacunas")
public class Vacuna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(nullable = false)
    private String vacuna;

    private String lote;

    private String laboratorio;

    private LocalDate fechaAplicacion;

    private LocalDate proximaDosis;

    private String aplicadaPor;

    private String viaAplicacion;

    private String sitioAplicacion;

    private String estado;

    private String observaciones;

    private String tipoRecordatorio;

    private Integer diasAntes;

    private String notasAdicionales;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public String getVacuna() { return vacuna; }
    public void setVacuna(String vacuna) { this.vacuna = vacuna; }
    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDate fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
    public LocalDate getProximaDosis() { return proximaDosis; }
    public void setProximaDosis(LocalDate proximaDosis) { this.proximaDosis = proximaDosis; }
    public String getAplicadaPor() { return aplicadaPor; }
    public void setAplicadaPor(String aplicadaPor) { this.aplicadaPor = aplicadaPor; }
    public String getViaAplicacion() { return viaAplicacion; }
    public void setViaAplicacion(String viaAplicacion) { this.viaAplicacion = viaAplicacion; }
    public String getSitioAplicacion() { return sitioAplicacion; }
    public void setSitioAplicacion(String sitioAplicacion) { this.sitioAplicacion = sitioAplicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public String getTipoRecordatorio() { return tipoRecordatorio; }
    public void setTipoRecordatorio(String tipoRecordatorio) { this.tipoRecordatorio = tipoRecordatorio; }
    public Integer getDiasAntes() { return diasAntes; }
    public void setDiasAntes(Integer diasAntes) { this.diasAntes = diasAntes; }
    public String getNotasAdicionales() { return notasAdicionales; }
    public void setNotasAdicionales(String notasAdicionales) { this.notasAdicionales = notasAdicionales; }
}
