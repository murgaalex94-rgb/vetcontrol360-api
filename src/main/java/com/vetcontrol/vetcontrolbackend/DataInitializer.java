package com.vetcontrol.vetcontrolbackend;

import com.vetcontrol.vetcontrolbackend.entity.*;
import com.vetcontrol.vetcontrolbackend.repository.*;
import com.vetcontrol.vetcontrolbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private MascotaRepository mascotaRepository;
    @Autowired private VacunaRepository vacunaRepository;
    @Autowired private CitaRepository citaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private FacturaRepository facturaRepository;

    @Override
    public void run(String... args) {
        // Usuarios
        if (usuarioRepository.count() == 0) {
            Usuario u = new Usuario();
            u.setUsuario("admin");
            u.setPasswordHash(PasswordHasher.hash("admin123"));
            u.setNombreCompleto("Administrador");
            u.setActivo(true);
            u.setIdRol(1);
            usuarioRepository.save(u);
            System.out.println(">>> Usuario admin creado (password: admin123)");
        } else {
            // Re-hash existing admin user if using legacy format
            usuarioRepository.findByUsuario("admin").ifPresent(u -> {
                String hash = u.getPasswordHash();
                if (hash == null || !hash.contains(":")) {
                    u.setPasswordHash(PasswordHasher.hash("admin123"));
                    usuarioRepository.save(u);
                    System.out.println(">>> Hash de admin actualizado a PBKDF2");
                }
            });
        }

        // Clientes
        if (clienteRepository.count() == 0) {
            Cliente c1 = new Cliente(); c1.setNombre("Juan Pérez"); c1.setTelefono("555-0101"); c1.setEmail("juan@email.com");
            Cliente c2 = new Cliente(); c2.setNombre("María García"); c2.setTelefono("555-0102"); c2.setEmail("maria@email.com");
            Cliente c3 = new Cliente(); c3.setNombre("Carlos López"); c3.setTelefono("555-0103"); c3.setEmail("carlos@email.com");
            Cliente c4 = new Cliente(); c4.setNombre("Ana Martínez"); c4.setTelefono("555-0104"); c4.setEmail("ana@email.com");
            Cliente c5 = new Cliente(); c5.setNombre("Roberto Sánchez"); c5.setTelefono("555-0105"); c5.setEmail("roberto@email.com");
            clienteRepository.save(c1); clienteRepository.save(c2); clienteRepository.save(c3);
            clienteRepository.save(c4); clienteRepository.save(c5);
            System.out.println(">>> Clientes de ejemplo creados");
        }

        // Mascotas
        if (mascotaRepository.count() == 0) {
            Cliente c1 = clienteRepository.findById(1L).orElse(null);
            Cliente c2 = clienteRepository.findById(2L).orElse(null);
            Cliente c3 = clienteRepository.findById(3L).orElse(null);
            Cliente c4 = clienteRepository.findById(4L).orElse(null);
            Cliente c5 = clienteRepository.findById(5L).orElse(null);

            Mascota m1 = new Mascota(); m1.setNombre("Max"); m1.setEspecie("Perro"); m1.setRaza("Golden Retriever"); m1.setSexo("Macho"); m1.setFechaNacimiento(LocalDate.of(2020, 5, 12)); m1.setColor("Dorado"); m1.setPeso(32.5); m1.setTipoPelaje("Largo"); m1.setTamano("Grande"); m1.setEsterilizado(true); m1.setEstado("Activo"); m1.setCliente(c1);
            Mascota m2 = new Mascota(); m2.setNombre("Luna"); m2.setEspecie("Gato"); m2.setRaza("Siamés"); m2.setSexo("Hembra"); m2.setFechaNacimiento(LocalDate.of(2021, 8, 3)); m2.setColor("Crema"); m2.setPeso(4.2); m2.setTipoPelaje("Corto"); m2.setTamano("Pequeño"); m2.setEsterilizado(true); m2.setEstado("Activo"); m2.setCliente(c2);
            Mascota m3 = new Mascota(); m3.setNombre("Rocky"); m3.setEspecie("Perro"); m3.setRaza("Pastor Alemán"); m3.setSexo("Macho"); m3.setFechaNacimiento(LocalDate.of(2019, 11, 20)); m3.setColor("Negro y fuego"); m3.setPeso(38.0); m3.setTipoPelaje("Corto"); m3.setTamano("Grande"); m3.setEsterilizado(false); m3.setEstado("Activo"); m3.setCliente(c3);
            Mascota m4 = new Mascota(); m4.setNombre("Mimi"); m4.setEspecie("Gato"); m4.setRaza("Persa"); m4.setSexo("Hembra"); m4.setFechaNacimiento(LocalDate.of(2022, 3, 15)); m4.setColor("Blanco"); m4.setPeso(3.8); m4.setTipoPelaje("Largo"); m4.setTamano("Pequeño"); m4.setEsterilizado(false); m4.setEstado("Activo"); m4.setCliente(c4);
            Mascota m5 = new Mascota(); m5.setNombre("Toby"); m5.setEspecie("Perro"); m5.setRaza("Labrador"); m5.setSexo("Macho"); m5.setFechaNacimiento(LocalDate.of(2023, 1, 8)); m5.setColor("Chocolate"); m5.setPeso(28.0); m5.setTipoPelaje("Corto"); m5.setTamano("Mediano"); m5.setEsterilizado(false); m5.setEstado("Activo"); m5.setCliente(c5);
            mascotaRepository.save(m1); mascotaRepository.save(m2); mascotaRepository.save(m3);
            mascotaRepository.save(m4); mascotaRepository.save(m5);
            System.out.println(">>> Mascotas de ejemplo creadas");
        }

        // Vacunas
        if (vacunaRepository.count() == 0) {
            Mascota m1 = mascotaRepository.findById(1L).orElse(null);
            Mascota m2 = mascotaRepository.findById(2L).orElse(null);
            Mascota m3 = mascotaRepository.findById(3L).orElse(null);

            Vacuna v1 = new Vacuna(); v1.setMascota(m1); v1.setVacuna("Rabia"); v1.setLote("LOT-001"); v1.setLaboratorio("Zoetis");
            v1.setFechaAplicacion(LocalDate.of(2024, 6, 15)); v1.setProximaDosis(LocalDate.of(2025, 6, 15));
            v1.setAplicadaPor("Dra. María García"); v1.setViaAplicacion("Subcutánea"); v1.setEstado("Aplicada");

            Vacuna v2 = new Vacuna(); v2.setMascota(m2); v2.setVacuna("Triple Felina"); v2.setLote("LOT-002"); v2.setLaboratorio("Merial");
            v2.setFechaAplicacion(LocalDate.of(2024, 3, 10)); v2.setProximaDosis(LocalDate.of(2025, 3, 10));
            v2.setAplicadaPor("Dr. Carlos López"); v2.setViaAplicacion("Subcutánea"); v2.setEstado("Aplicada");

            Vacuna v3 = new Vacuna(); v3.setMascota(m3); v3.setVacuna("Sextuple"); v3.setLote("LOT-003"); v3.setLaboratorio("Boehringer Ingelheim");
            v3.setFechaAplicacion(LocalDate.of(2024, 1, 20)); v3.setProximaDosis(LocalDate.of(2025, 1, 20));
            v3.setAplicadaPor("Dra. Ana Martínez"); v3.setViaAplicacion("Subcutánea"); v3.setEstado("Pendiente");

            vacunaRepository.save(v1); vacunaRepository.save(v2); vacunaRepository.save(v3);
            System.out.println(">>> Vacunas de ejemplo creadas");
        }

        // Citas
        if (citaRepository.count() == 0) {
            Cliente c1 = clienteRepository.findById(1L).orElse(null);
            Cliente c2 = clienteRepository.findById(2L).orElse(null);
            Mascota m1 = mascotaRepository.findById(1L).orElse(null);
            Mascota m2 = mascotaRepository.findById(2L).orElse(null);

            Cita ci1 = new Cita(); ci1.setCliente(c1); ci1.setMascota(m1); ci1.setTipoCita("Consulta General");
            ci1.setFecha(LocalDate.now()); ci1.setHora(LocalTime.of(9, 0)); ci1.setDuracion(30);
            ci1.setVeterinario("Dra. María García"); ci1.setConsultorio("Consultorio 1");
            ci1.setMotivo("Revisión anual de rutina"); ci1.setRecordatorio(true); ci1.setWhatsapp(true); ci1.setEmail(false);

            Cita ci2 = new Cita(); ci2.setCliente(c2); ci2.setMascota(m2); ci2.setTipoCita("Vacunación");
            ci2.setFecha(LocalDate.now()); ci2.setHora(LocalTime.of(10, 0)); ci2.setDuracion(30);
            ci2.setVeterinario("Dr. Carlos López"); ci2.setConsultorio("Consultorio 2");
            ci2.setMotivo("Vacuna antirrábica"); ci2.setRecordatorio(true); ci2.setWhatsapp(false); ci2.setEmail(true);

            Cita ci3 = new Cita(); ci3.setCliente(c1); ci3.setMascota(m1); ci3.setTipoCita("Control / Revisión");
            ci3.setFecha(LocalDate.now().plusDays(1)); ci3.setHora(LocalTime.of(11, 0)); ci3.setDuracion(45);
            ci3.setVeterinario("Dra. Ana Martínez"); ci3.setConsultorio("Consultorio 1");
            ci3.setMotivo("Control post-operatorio"); ci3.setRecordatorio(true); ci3.setWhatsapp(true); ci3.setEmail(true);

            citaRepository.save(ci1); citaRepository.save(ci2); citaRepository.save(ci3);
            System.out.println(">>> Citas de ejemplo creadas");
        }

        // Productos (Inventario)
        if (productoRepository.count() == 0) {
            Producto p1 = new Producto(); p1.setNombre("Antibiótico Pet 250 mg"); p1.setSku("MED-001"); p1.setCategoria("Medicamentos");
            p1.setTipo("Medicamento"); p1.setPresentacion("Tableta"); p1.setUnidad("mg"); p1.setProveedor("Vet Pharma");
            p1.setPrecioCompra(new BigDecimal("8.50")); p1.setPrecioVenta(new BigDecimal("15.00")); p1.setStockActual(45); p1.setStockMinimo(20); p1.setStockMaximo(100);

            Producto p2 = new Producto(); p2.setNombre("Royal Canin Adulto"); p2.setSku("ALI-001"); p2.setCategoria("Alimentos");
            p2.setTipo("Alimento"); p2.setPresentacion("Polvo"); p2.setUnidad("kg"); p2.setProveedor("Royal Canin");
            p2.setPrecioCompra(new BigDecimal("45.00")); p2.setPrecioVenta(new BigDecimal("65.00")); p2.setStockActual(120); p2.setStockMinimo(30); p2.setStockMaximo(200);

            Producto p3 = new Producto(); p3.setNombre("Collar Antipulgas"); p3.setSku("ACC-001"); p3.setCategoria("Accesorios");
            p3.setTipo("Accesorio"); p3.setPresentacion("Unidad"); p3.setUnidad("unidad"); p3.setProveedor("Bayer");
            p3.setPrecioCompra(new BigDecimal("12.00")); p3.setPrecioVenta(new BigDecimal("22.50")); p3.setStockActual(8); p3.setStockMinimo(10); p3.setStockMaximo(50);

            Producto p4 = new Producto(); p4.setNombre("Vacuna Triple Canina"); p4.setSku("VAC-001"); p4.setCategoria("Vacunas");
            p4.setTipo("Insumo"); p4.setPresentacion("Inyectable"); p4.setUnidad("ml"); p4.setProveedor("Zoetis");
            p4.setPrecioCompra(new BigDecimal("18.00")); p4.setPrecioVenta(new BigDecimal("32.00")); p4.setStockActual(35); p4.setStockMinimo(15); p4.setStockMaximo(80);

            Producto p5 = new Producto(); p5.setNombre("Shampoo Médico"); p5.setSku("HIG-001"); p5.setCategoria("Higiene");
            p5.setTipo("Insumo"); p5.setPresentacion("Solución"); p5.setUnidad("ml"); p5.setProveedor("Virbac");
            p5.setPrecioCompra(new BigDecimal("6.50")); p5.setPrecioVenta(new BigDecimal("12.00")); p5.setStockActual(60); p5.setStockMinimo(20); p5.setStockMaximo(120);

            productoRepository.save(p1); productoRepository.save(p2); productoRepository.save(p3);
            productoRepository.save(p4); productoRepository.save(p5);
            System.out.println(">>> Productos de ejemplo creados");
        }

        // Facturas
        if (facturaRepository.count() == 0) {
            Factura f1 = new Factura(); f1.setNumero("F-000128"); f1.setFecha(LocalDate.of(2024, 5, 22));
            f1.setCliente("Juan Pérez García"); f1.setTelefono("987 654 321"); f1.setMascota("Max"); f1.setRaza("Golden Retriever");
            f1.setTotal(new BigDecimal("350.00")); f1.setEstado("Pagada"); f1.setMetodoPago("Tarjeta");

            Factura f2 = new Factura(); f2.setNumero("F-000127"); f2.setFecha(LocalDate.of(2024, 5, 21));
            f2.setCliente("María García López"); f2.setTelefono("912 345 678"); f2.setMascota("Luna"); f2.setRaza("Siamés");
            f2.setTotal(new BigDecimal("180.00")); f2.setEstado("Pagada"); f2.setMetodoPago("Yape / Plin");

            Factura f3 = new Factura(); f3.setNumero("F-000126"); f3.setFecha(LocalDate.of(2024, 5, 20));
            f3.setCliente("Carlos López Martínez"); f3.setTelefono("945 678 123"); f3.setMascota("Rocky"); f3.setRaza("Pastor Alemán");
            f3.setTotal(new BigDecimal("220.00")); f3.setEstado("Pendiente"); f3.setMetodoPago("—");

            Factura f4 = new Factura(); f4.setNumero("F-000125"); f4.setFecha(LocalDate.of(2024, 5, 19));
            f4.setCliente("Ana Martínez Ruiz"); f4.setTelefono("978 123 456"); f4.setMascota("Mimi"); f4.setRaza("Persa");
            f4.setTotal(new BigDecimal("95.00")); f4.setEstado("Pagada"); f4.setMetodoPago("Efectivo");

            facturaRepository.save(f1); facturaRepository.save(f2); facturaRepository.save(f3); facturaRepository.save(f4);
            System.out.println(">>> Facturas de ejemplo creadas");
        }
    }
}
