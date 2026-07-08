package com.vetcontrol.vetcontrolbackend.dto;

import java.math.BigDecimal;
import java.util.List;

public class EnvioFacturaRequest {

    private Long facturaId;
    private String tipoComprobante;
    private String serie;
    private String numero;

    private String clienteDoc;
    private String clienteTipoDoc;
    private String clienteNombre;
    private String clienteDireccion;

    private String moneda;
    private List<Item> items;

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }
    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getClienteDoc() { return clienteDoc; }
    public void setClienteDoc(String clienteDoc) { this.clienteDoc = clienteDoc; }
    public String getClienteTipoDoc() { return clienteTipoDoc; }
    public void setClienteTipoDoc(String clienteTipoDoc) { this.clienteTipoDoc = clienteTipoDoc; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public String getClienteDireccion() { return clienteDireccion; }
    public void setClienteDireccion(String clienteDireccion) { this.clienteDireccion = clienteDireccion; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public static class Item {
        private String descripcion;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal precioTotal;

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public BigDecimal getPrecioTotal() { return precioTotal; }
        public void setPrecioTotal(BigDecimal precioTotal) { this.precioTotal = precioTotal; }
    }
}
