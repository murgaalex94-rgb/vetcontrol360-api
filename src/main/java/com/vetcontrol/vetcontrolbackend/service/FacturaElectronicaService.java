package com.vetcontrol.vetcontrolbackend.service;

import com.vetcontrol.vetcontrolbackend.dto.EnvioFacturaRequest;
import com.vetcontrol.vetcontrolbackend.entity.ComprobanteElectronico;
import com.vetcontrol.vetcontrolbackend.entity.Empresa;
import com.vetcontrol.vetcontrolbackend.repository.ComprobanteElectronicoRepository;
import com.vetcontrol.vetcontrolbackend.repository.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FacturaElectronicaService {

    private static final Logger log = LoggerFactory.getLogger(FacturaElectronicaService.class);

    private final ComprobanteElectronicoRepository repo;
    private final EmpresaRepository empresaRepo;

    @Value("${SUNAT_DEMO:true}")
    private boolean sunatDemo;

    @Value("${SUNAT_USUARIO_SOL:MODDATOS}")
    private String sunatUsuarioSol;

    @Value("${SUNAT_CLAVE_SOL:MODDATOS}")
    private String sunatClaveSol;

    @Value("${SUNAT_URL_ENVIO:https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService}")
    private String sunatUrlEnvio;

    public FacturaElectronicaService(ComprobanteElectronicoRepository repo, EmpresaRepository empresaRepo) {
        this.repo = repo;
        this.empresaRepo = empresaRepo;
    }

    private Empresa getEmpresa() {
        var todas = empresaRepo.findAll();
        if (todas.isEmpty()) {
            var def = new Empresa();
            def.setRuc("20600085510");
            def.setRazonSocial("MI EMPRESA SAC");
            def.setDireccion("Av. Principal 123");
            def.setEstado("Activo");
            return empresaRepo.save(def);
        }
        return todas.get(0);
    }

    public ComprobanteElectronico enviar(EnvioFacturaRequest request) throws Exception {
        String tipo = "FACTURA".equalsIgnoreCase(request.getTipoComprobante()) ? "01" : "03";
        var comprobante = new ComprobanteElectronico();
        comprobante.setFacturaId(request.getFacturaId());
        comprobante.setTipoComprobante(tipo);
        comprobante.setSerie(request.getSerie());
        comprobante.setNumero(request.getNumero());
        comprobante.setRucEmisor(getEmpresa().getRuc());
        comprobante.setClienteDoc(request.getClienteDoc());
        comprobante.setClienteNombre(request.getClienteNombre());
        comprobante.setModo(sunatDemo ? "demo" : "produccion");
        comprobante.setFechaEnvio(LocalDateTime.now());

        var empresa = getEmpresa();
        String xml = generarXML(request, empresa);
        log.info("XML UBL 2.1 generado (tipo={})", "01".equals(tipo) ? "FACTURA" : "BOLETA");

        String xmlFirmado = firmar(xml, empresa);
        log.info("XML firmado");

        String respuesta = enviarSUNAT(xmlFirmado, request.getSerie(), request.getNumero(), empresa, tipo);
        log.info("Respuesta SUNAT recibida");

        comprobante = guardarCDR(comprobante, respuesta);

        return repo.save(comprobante);
    }

    public String generarXML(EnvioFacturaRequest request, Empresa empresa) {
        boolean esFactura = "FACTURA".equalsIgnoreCase(request.getTipoComprobante());
        String sunatTipoDoc = esFactura ? "01" : "03";
        String customId = esFactura ? "2.0" : "1.0";

        var sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        sb.append("<Invoice xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2\"");
        sb.append(" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\"");
        sb.append(" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\">");
        sb.append("<cbc:UBLVersionID>2.1</cbc:UBLVersionID>");
        sb.append("<cbc:CustomizationID>").append(customId).append("</cbc:CustomizationID>");
        sb.append("<cbc:ID>").append(request.getSerie()).append("-").append(request.getNumero()).append("</cbc:ID>");
        sb.append("<cbc:IssueDate>").append(LocalDate.now()).append("</cbc:IssueDate>");
        sb.append("<cbc:IssueTime>").append(LocalTime.now().withNano(0)).append("</cbc:IssueTime>");
        sb.append("<cbc:InvoiceTypeCode listID=\"0101\">").append(sunatTipoDoc).append("</cbc:InvoiceTypeCode>");

        String docTipo = "6".equals(request.getClienteTipoDoc()) ? "6" : (esFactura ? "6" : "1");
        sb.append("<cac:AccountingSupplierParty><cac:Party>");
        sb.append("<cac:PartyIdentification><cbc:ID schemeID=\"6\">").append(escXml(empresa.getRuc())).append("</cbc:ID></cac:PartyIdentification>");
        sb.append("<cac:PartyName><cbc:Name>").append(escXml(empresa.getRazonSocial())).append("</cbc:Name></cac:PartyName>");
        sb.append("<cac:PartyLegalEntity><cbc:RegistrationName>").append(escXml(empresa.getRazonSocial())).append("</cbc:RegistrationName></cac:PartyLegalEntity>");
        sb.append("</cac:Party></cac:AccountingSupplierParty>");

        sb.append("<cac:AccountingCustomerParty><cac:Party>");
        sb.append("<cac:PartyIdentification><cbc:ID schemeID=\"").append(docTipo).append("\">").append(escXml(request.getClienteDoc())).append("</cbc:ID></cac:PartyIdentification>");
        sb.append("<cac:PartyLegalEntity><cbc:RegistrationName>").append(escXml(request.getClienteNombre())).append("</cbc:RegistrationName></cac:PartyLegalEntity>");
        if (request.getClienteDireccion() != null && !request.getClienteDireccion().isBlank()) {
            sb.append("<cac:RegistrationAddress>");
            sb.append("<cac:AddressLine><cbc:Line>").append(escXml(request.getClienteDireccion())).append("</cbc:Line></cac:AddressLine>");
            sb.append("</cac:RegistrationAddress>");
        }
        sb.append("</cac:Party></cac:AccountingCustomerParty>");

        String moneda = request.getMoneda() != null ? request.getMoneda() : "PEN";
        BigDecimal total = BigDecimal.ZERO;
        if (request.getItems() != null) {
            for (int i = 0; i < request.getItems().size(); i++) {
                var item = request.getItems().get(i);
                sb.append("<cac:InvoiceLine>");
                sb.append("<cbc:ID>").append(i + 1).append("</cbc:ID>");
                sb.append("<cbc:InvoicedQuantity unitCode=\"NIU\">").append(item.getCantidad()).append("</cbc:InvoicedQuantity>");
                sb.append("<cbc:LineExtensionAmount currencyID=\"").append(moneda).append("\">").append(item.getPrecioTotal()).append("</cbc:LineExtensionAmount>");
                sb.append("<cac:PricingReference><cac:AlternativeConditionPrice>");
                sb.append("<cbc:PriceAmount currencyID=\"").append(moneda).append("\">").append(item.getPrecioUnitario()).append("</cbc:PriceAmount>");
                sb.append("<cbc:PriceTypeCode>01</cbc:PriceTypeCode>");
                sb.append("</cac:AlternativeConditionPrice></cac:PricingReference>");
                sb.append("<cac:Item><cbc:Description>").append(escXml(item.getDescripcion())).append("</cbc:Description></cac:Item>");
                sb.append("<cac:Price><cbc:PriceAmount currencyID=\"").append(moneda).append("\">").append(item.getPrecioUnitario()).append("</cbc:PriceAmount></cac:Price>");
                sb.append("</cac:InvoiceLine>");
                total = total.add(item.getPrecioTotal());
            }
        }

        BigDecimal igv = total.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalConIgv = total.add(igv);

        sb.append("<cac:TaxTotal>");
        sb.append("<cbc:TaxAmount currencyID=\"").append(moneda).append("\">").append(igv).append("</cbc:TaxAmount>");
        sb.append("<cac:TaxSubtotal>");
        sb.append("<cbc:TaxableAmount currencyID=\"").append(moneda).append("\">").append(total).append("</cbc:TaxableAmount>");
        sb.append("<cbc:TaxAmount currencyID=\"").append(moneda).append("\">").append(igv).append("</cbc:TaxAmount>");
        sb.append("<cac:TaxCategory>");
        sb.append("<cbc:Percent>18.00</cbc:Percent>");
        sb.append("<cbc:TaxExemptionReasonCode>10</cbc:TaxExemptionReasonCode>");
        sb.append("<cac:TaxScheme><cbc:ID>1000</cbc:ID><cbc:Name>IGV</cbc:Name><cbc:TaxTypeCode>VAT</cbc:TaxTypeCode></cac:TaxScheme>");
        sb.append("</cac:TaxCategory>");
        sb.append("</cac:TaxSubtotal>");
        sb.append("</cac:TaxTotal>");

        sb.append("<cac:LegalMonetaryTotal>");
        sb.append("<cbc:PayableAmount currencyID=\"").append(moneda).append("\">").append(totalConIgv).append("</cbc:PayableAmount>");
        sb.append("</cac:LegalMonetaryTotal>");

        sb.append("</Invoice>");
        return sb.toString();
    }

    public String firmar(String xml, Empresa empresa) throws Exception {
        if (sunatDemo || empresa.getCertificadoPath() == null || empresa.getCertificadoPath().isBlank()) {
            return xml.replace("</Invoice>",
                    "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
                    "<ds:SignedInfo>" +
                    "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" />" +
                    "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" />" +
                    "<ds:Reference URI=\"\">" +
                    "<ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /></ds:Transforms>" +
                    "<ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" />" +
                    "<ds:DigestValue>MOCK</ds:DigestValue>" +
                    "</ds:Reference></ds:SignedInfo>" +
                    "<ds:SignatureValue>MOCK</ds:SignatureValue>" +
                    "<ds:KeyInfo><ds:X509Data><ds:X509SubjectName>CN=DEMO</ds:X509SubjectName></ds:X509Data></ds:KeyInfo>" +
                    "</ds:Signature></Invoice>");
        }

        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        var doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        var ks = KeyStore.getInstance("PKCS12");
        try (var fis = new FileInputStream(empresa.getCertificadoPath())) {
            ks.load(fis, empresa.getCertificadoPassword().toCharArray());
        }
        var alias = ks.aliases().nextElement();
        var privateKey = (PrivateKey) ks.getKey(alias, empresa.getCertificadoPassword().toCharArray());
        var cert = (X509Certificate) ks.getCertificate(alias);

        var mef = javax.xml.crypto.dsig.XMLSignatureFactory.getInstance("DOM");
        var ref = mef.newReference("",
                mef.newDigestMethod(javax.xml.crypto.dsig.DigestMethod.SHA1, null),
                java.util.List.of(mef.newTransform(javax.xml.crypto.dsig.Transform.ENVELOPED,
                        (javax.xml.crypto.dsig.spec.TransformParameterSpec) null)), null, null);
        var signedInfo = mef.newSignedInfo(
                mef.newCanonicalizationMethod(javax.xml.crypto.dsig.CanonicalizationMethod.INCLUSIVE,
                        (javax.xml.crypto.dsig.spec.C14NMethodParameterSpec) null),
                mef.newSignatureMethod(javax.xml.crypto.dsig.SignatureMethod.RSA_SHA1, null),
                java.util.List.of(ref));
        var kif = mef.getKeyInfoFactory();
        var ki = kif.newKeyInfo(java.util.List.of(kif.newX509Data(java.util.List.of(cert))));

        var dsc = new javax.xml.crypto.dsig.dom.DOMSignContext(privateKey, doc.getDocumentElement());
        mef.newXMLSignature(signedInfo, ki).sign(dsc);

        var sw = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
    }

    public String enviarSUNAT(String xmlFirmado, String serie, String numero, Empresa empresa, String sunatTipoDoc) throws Exception {
        if (sunatTipoDoc == null) sunatTipoDoc = "01";
        String tipoLabel = "01".equals(sunatTipoDoc) ? "Factura" : "Boleta";

        if (sunatDemo) {
            return "<SunatResponse>" +
                    "<StatusCode>0</StatusCode>" +
                    "<Cdr><ResponseCode>0</ResponseCode>" +
                    "<Description>La " + tipoLabel + " " + serie + "-" + numero + " ha sido aceptada</Description>" +
                    "</Cdr></SunatResponse>";
        }

        var fileName = empresa.getRuc() + "-" + sunatTipoDoc + "-" + numero;
        var zipB64 = comprimirZip(xmlFirmado, fileName);

        var soap = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
                + " xmlns:ser=\"http://service.sunat.gob.pe\">"
                + "<soap:Header><ser:credentials>"
                + "<ser:usuario>" + escXml(sunatUsuarioSol) + "</ser:usuario>"
                + "<ser:contrasena>" + escXml(sunatClaveSol) + "</ser:contrasena>"
                + "</ser:credentials></soap:Header>"
                + "<soap:Body><ser:sendBill>"
                + "<ser:fileName>" + fileName + ".zip</ser:fileName>"
                + "<ser:contentFile>" + zipB64 + "</ser:contentFile>"
                + "</ser:sendBill></soap:Body></soap:Envelope>";

        var conn = (java.net.HttpURLConnection) new java.net.URI(sunatUrlEnvio).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml; charset=ISO-8859-1");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);
        try (var os = conn.getOutputStream()) {
            os.write(soap.getBytes("ISO-8859-1"));
        }
        try (var is = conn.getResponseCode() < 400 ? conn.getInputStream() : conn.getErrorStream()) {
            return new String(is.readAllBytes(), "ISO-8859-1");
        }
    }

    public ComprobanteElectronico guardarCDR(ComprobanteElectronico comprobante, String respuesta) {
        comprobante.setCdrXml(respuesta);
        comprobante.setFechaRespuesta(LocalDateTime.now());

        var codigo = extraerTexto(respuesta, "<ResponseCode>", "</ResponseCode>");
        var descripcion = extraerTexto(respuesta, "<Description>", "</Description>");
        comprobante.setCdrCodigo(codigo);
        comprobante.setCdrDescripcion(descripcion);

        if ("0".equals(codigo)) {
            comprobante.setSunatEstado("ACEPTADA");
        } else if (codigo.isEmpty()) {
            comprobante.setSunatEstado("ERROR_SIN_CDR");
            comprobante.setCdrDescripcion("No se recibio CDR valido de SUNAT");
        } else {
            comprobante.setSunatEstado("RECHAZADA");
        }

        return comprobante;
    }

    private String comprimirZip(String xml, String nombreArchivo) {
        var baos = new java.io.ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(baos)) {
            zos.putNextEntry(new ZipEntry(nombreArchivo + ".xml"));
            zos.write(xml.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        } catch (Exception e) {
            throw new RuntimeException("Error al comprimir XML", e);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private String extraerTexto(String xml, String open, String close) {
        int i = xml.indexOf(open);
        if (i == -1) return "";
        i += open.length();
        int j = xml.indexOf(close, i);
        return j == -1 ? xml.substring(i) : xml.substring(i, j);
    }

    private String escXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
