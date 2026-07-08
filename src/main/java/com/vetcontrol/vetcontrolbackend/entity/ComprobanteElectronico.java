package com.vetcontrol.vetcontrolbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobantes_electronicos")
public class ComprobanteElectronico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long facturaId;

    private String tipoComprobante;

    private String serie;

    private String numero;

    private String rucEmisor;

    private String clienteDoc;

    private String clienteNombre;

    @Column(columnDefinition = "TEXT")
    private String xmlFirmado;

    @Column(columnDefinition = "TEXT")
    private String cdrXml;

    private String cdrCodigo;

    private String cdrDescripcion;

    private String sunatEstado;

    private LocalDateTime fechaEnvio;

    private LocalDateTime fechaRespuesta;

    private String modo;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getFacturaId() { return facturaId; }

    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public String getTipoComprobante() { return tipoComprobante; }

    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getSerie() { return serie; }

    public void setSerie(String serie) { this.serie = serie; }

    public String getNumero() { return numero; }

    public void setNumero(String numero) { this.numero = numero; }

    public String getRucEmisor() { return rucEmisor; }

    public void setRucEmisor(String rucEmisor) { this.rucEmisor = rucEmisor; }

    public String getClienteDoc() { return clienteDoc; }

    public void setClienteDoc(String clienteDoc) { this.clienteDoc = clienteDoc; }

    public String getClienteNombre() { return clienteNombre; }

    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getXmlFirmado() { return xmlFirmado; }

    public void setXmlFirmado(String xmlFirmado) { this.xmlFirmado = xmlFirmado; }

    public String getCdrXml() { return cdrXml; }

    public void setCdrXml(String cdrXml) { this.cdrXml = cdrXml; }

    public String getCdrCodigo() { return cdrCodigo; }

    public void setCdrCodigo(String cdrCodigo) { this.cdrCodigo = cdrCodigo; }

    public String getCdrDescripcion() { return cdrDescripcion; }

    public void setCdrDescripcion(String cdrDescripcion) { this.cdrDescripcion = cdrDescripcion; }

    public String getSunatEstado() { return sunatEstado; }

    public void setSunatEstado(String sunatEstado) { this.sunatEstado = sunatEstado; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }

    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }

    public String getModo() { return modo; }

    public void setModo(String modo) { this.modo = modo; }
}
