package com.vetcontrol.vetcontrolbackend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vetcontrol.vetcontrolbackend.entity.Empresa;
import com.vetcontrol.vetcontrolbackend.entity.Factura;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

@Service
public class PDFGeneratorService {

    private final EmpresaService empresaService;

    public PDFGeneratorService(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    private String getRuc() { return empresaService.obtener().getRuc(); }
    private String getRazonSocial() { return empresaService.obtener().getRazonSocial(); }

    public byte[] generarFacturaPDF(Factura factura) {
        var empresa = empresaService.obtener();
        var baos = new ByteArrayOutputStream();
        var documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, baos);
        documento.open();

        var helv = FontFactory.getFont(FontFactory.HELVETICA, 10);
        var helvBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        var helvTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        var helvSmall = FontFactory.getFont(FontFactory.HELVETICA, 8);

        addCabecera(documento, helvTitle, helvBold, helv, factura, empresa);
        addDatosCliente(documento, helvBold, helv, factura);
        addTablaItems(documento, helvBold, helv, helvSmall, factura);
        addTotales(documento, helvBold, helv, factura);
        addMontoLetras(documento, helv, factura);

        documento.close();
        return baos.toByteArray();
    }

    private void addCabecera(Document doc, Font title, Font bold, Font normal, Factura f, Empresa empresa) {
        var tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingAfter(15);

        var cellIzq = new PdfPCell();
        cellIzq.setBorder(Rectangle.NO_BORDER);
        cellIzq.addElement(new Paragraph(empresa.getNombreComercial() != null ? empresa.getNombreComercial() : empresa.getRazonSocial(), title));
        cellIzq.addElement(new Paragraph(empresa.getRazonSocial(), bold));
        cellIzq.addElement(new Paragraph("RUC: " + empresa.getRuc(), normal));
        cellIzq.addElement(new Paragraph("Dirección: " + (empresa.getDireccion() != null ? empresa.getDireccion() : "—"), normal));
        cellIzq.addElement(new Paragraph("Teléfono: " + (empresa.getTelefono() != null ? empresa.getTelefono() : "—"), normal));
        cellIzq.addElement(new Paragraph("Email: " + (empresa.getEmail() != null ? empresa.getEmail() : "—"), normal));

        var cellDer = new PdfPCell();
        cellDer.setBorder(Rectangle.NO_BORDER);
        cellDer.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDer.addElement(new Paragraph("FACTURA ELECTRÓNICA", title));
        cellDer.addElement(new Paragraph("RUC: " + empresa.getRuc(), bold));
        cellDer.addElement(new Paragraph(""));
        cellDer.addElement(new Paragraph("N° " + f.getNumero(), bold));
        var fmt = new SimpleDateFormat("dd/MM/yyyy");
        var fechaStr = f.getFecha() != null ? fmt.format(java.sql.Date.valueOf(f.getFecha())) : "—";
        cellDer.addElement(new Paragraph("Fecha de Emisión: " + fechaStr, normal));

        tabla.addCell(cellIzq);
        tabla.addCell(cellDer);
        doc.add(tabla);
    }

    private void addDatosCliente(Document doc, Font bold, Font normal, Factura f) {
        var tabla = new PdfPTable(1);
        tabla.setWidthPercentage(100);
        tabla.setSpacingAfter(15);

        var cell = new PdfPCell();
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(8);
        cell.setBackgroundColor(new com.lowagie.text.Color(245, 245, 245));
        cell.addElement(new Paragraph("DATOS DEL CLIENTE", bold));
        cell.addElement(new Paragraph("Cliente: " + (f.getCliente() != null ? f.getCliente() : "—"), normal));
        cell.addElement(new Paragraph("Teléfono: " + (f.getTelefono() != null ? f.getTelefono() : "—"), normal));
        cell.addElement(new Paragraph("Mascota: " + (f.getMascota() != null ? f.getMascota() : "—") +
                (f.getRaza() != null ? " (" + f.getRaza() + ")" : ""), normal));

        tabla.addCell(cell);
        doc.add(tabla);
    }

    private void addTablaItems(Document doc, Font bold, Font normal, Font small, Factura f) {
        var tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{0.5f, 3f, 1.5f, 1.5f});
        tabla.setSpacingAfter(15);

        var hdr = new com.lowagie.text.Color(90, 105, 120);
        var hdrFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, com.lowagie.text.Color.WHITE);
        addHeaderCell(tabla, "Cant.", hdrFont, hdr);
        addHeaderCell(tabla, "Descripción", hdrFont, hdr);
        addHeaderCell(tabla, "P. Unitario", hdrFont, hdr);
        addHeaderCell(tabla, "Subtotal", hdrFont, hdr);

        var total = f.getTotal() != null ? f.getTotal() : BigDecimal.ZERO;
        var igv = total.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        var subtotal = total.subtract(igv);

        addCell(tabla, "1", normal);
        addCell(tabla, "Servicios Veterinarios - " + (f.getMascota() != null ? f.getMascota() : "General"), normal);
        addCell(tabla, "S/ " + total.setScale(2).toString(), normal);
        addCell(tabla, "S/ " + total.setScale(2).toString(), normal);

        doc.add(tabla);
    }

    private void addTotales(Document doc, Font bold, Font normal, Factura f) {
        var total = f.getTotal() != null ? f.getTotal() : BigDecimal.ZERO;
        var igv = total.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        var subtotal = total.subtract(igv);

        var tabla = new PdfPTable(2);
        tabla.setWidthPercentage(40);
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.setSpacingAfter(10);

        addTotalRow(tabla, "Subtotal:", "S/ " + subtotal.setScale(2).toString(), normal, false);
        addTotalRow(tabla, "IGV (18%):", "S/ " + igv.setScale(2).toString(), normal, false);
        addTotalRow(tabla, "TOTAL:", "S/ " + total.setScale(2).toString(), bold, true);

        doc.add(tabla);
    }

    private void addMontoLetras(Document doc, Font normal, Factura f) {
        var total = f.getTotal() != null ? f.getTotal() : BigDecimal.ZERO;
        var letras = numeroALetras(total);
        var p = new Paragraph("SON: " + letras + " SOLES", normal);
        p.setSpacingBefore(5);
        doc.add(p);

        doc.add(new Paragraph(""));
        doc.add(new Paragraph("----------------------------------------", normal));
        doc.add(new Paragraph("Representación impresa de la factura electrónica.", FontFactory.getFont(FontFactory.HELVETICA, 7, com.lowagie.text.Color.GRAY)));
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, com.lowagie.text.Color bg) {
        var cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        var cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, String label, String value, Font font, boolean highlight) {
        var cellLabel = new PdfPCell(new Phrase(label, font));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(3);
        cellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

        var cellValue = new PdfPCell(new Phrase(value, font));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setPadding(3);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        if (highlight) {
            cellLabel.setBackgroundColor(new com.lowagie.text.Color(230, 247, 230));
            cellValue.setBackgroundColor(new com.lowagie.text.Color(230, 247, 230));
        }

        table.addCell(cellLabel);
        table.addCell(cellValue);
    }

    private String numeroALetras(BigDecimal monto) {
        var entero = monto.setScale(0, RoundingMode.DOWN).intValue();
        var decimal = monto.remainder(BigDecimal.ONE).multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).intValue();
        var unidades = new String[]{"", "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ",
                "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"};
        var decenas = new String[]{"", "", "VEINTI", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
        var centenas = new String[]{"", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"};

        if (entero == 0) return "CERO";
        var sb = new StringBuilder();

        int millon = entero / 1000000;
        int miles = (entero % 1000000) / 1000;
        int resto = entero % 1000;

        if (millon == 1) sb.append("UN MILLÓN ");
        else if (millon > 1) sb.append(convertirGrupo(millon, unidades, decenas, centenas)).append(" MILLONES ");

        if (miles == 1) sb.append("MIL ");
        else if (miles > 1) sb.append(convertirGrupo(miles, unidades, decenas, centenas)).append(" MIL ");

        if (resto == 100) sb.append("CIEN");
        else if (resto > 0) sb.append(convertirGrupo(resto, unidades, decenas, centenas));

        if (decimal > 0) {
            var decStr = decimal < 10 ? "0" + decimal : String.valueOf(decimal);
            sb.append(" CON ").append(decStr).append("/100");
        }

        return sb.toString().trim();
    }

    private String convertirGrupo(int n, String[] unidades, String[] decenas, String[] centenas) {
        var sb = new StringBuilder();
        int c = n / 100;
        int d = (n % 100) / 10;
        int u = n % 10;

        if (c > 0) sb.append(centenas[c]).append(" ");

        if (d == 0) {
            if (u > 0) sb.append(unidades[u]);
        } else if (d == 1) {
            sb.append(unidades[10 + u]);
        } else {
            sb.append(decenas[d]);
            if (u > 0) sb.append(" Y ").append(unidades[u]);
        }

        return sb.toString().trim();
    }
}
