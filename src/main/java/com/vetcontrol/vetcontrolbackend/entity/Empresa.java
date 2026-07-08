package com.vetcontrol.vetcontrolbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 11)
    private String ruc;

    @Column(nullable = false)
    private String razonSocial;

    private String nombreComercial;

    @Column(nullable = false)
    private String direccion;

    private String email;

    private String telefono;

    private String departamento;

    private String provincia;

    private String distrito;

    private String ubigeo;

    private String logoUrl;

    private String certificadoPath;

    private String certificadoPassword;

    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getCertificadoPath() { return certificadoPath; }
    public void setCertificadoPath(String certificadoPath) { this.certificadoPath = certificadoPath; }
    public String getCertificadoPassword() { return certificadoPassword; }
    public void setCertificadoPassword(String certificadoPassword) { this.certificadoPassword = certificadoPassword; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
