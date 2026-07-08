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
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private AuditoriaRepository auditoriaRepository;

    @Override
    public void run(String... args) {
        // Always ensure admin has correct PBKDF2 hash
        usuarioRepository.findByUsuario("admin").ifPresentOrElse(u -> {
            u.setPasswordHash(PasswordHasher.hash("admin123"));
            usuarioRepository.save(u);
            System.out.println(">>> Hash de admin re-generado con PBKDF2");
        }, () -> {
            Usuario u = new Usuario();
            u.setUsuario("admin");
            u.setPasswordHash(PasswordHasher.hash("admin123"));
            u.setNombreCompleto("Administrador");
            u.setActivo(true);
            u.setIdRol(1);
            usuarioRepository.save(u);
            System.out.println(">>> Usuario admin creado (password: admin123)");
        });
        
        // Ensure alexmurga has correct PBKDF2 hash
        usuarioRepository.findByUsuario("alexmurga").ifPresentOrElse(u -> {
            u.setPasswordHash(PasswordHasher.hash("AlexMurga.20*"));
            usuarioRepository.save(u);
            System.out.println(">>> Hash de alexmurga re-generado con PBKDF2");
        }, () -> {
            // Create alexmurga user if doesn't exist
            Usuario u = new Usuario();
            u.setUsuario("alexmurga");
            u.setPasswordHash(PasswordHasher.hash("AlexMurga.20*"));
            u.setNombreCompleto("Alex Murga");
            u.setActivo(true);
            u.setIdRol(1);
            usuarioRepository.save(u);
            System.out.println(">>> Usuario alexmurga creado");
        });
        
        // Populate sample data if tables are empty
        if (clienteRepository.count() == 0) {
            populateSampleData();
        }
    }
    
    public void forcePopulateData() {
        populateSampleData();
    }
    
    private void populateSampleData() {
        System.out.println(">>> Poblando base de datos con datos de ejemplo...");
        
        // Create Empresa
        Empresa empresa = new Empresa();
        empresa.setRuc("20600085510");
        empresa.setRazonSocial("VetCare Clínica Veterinarias SAC");
        empresa.setNombreComercial("VetCare Clínica Veterinaria");
        empresa.setDireccion("Av. Principal 1234, San Miguel, Lima");
        empresa.setEmail("contacto@vetcare.pe");
        empresa.setTelefono("511-555-9999");
        empresa.setDepartamento("Lima");
        empresa.setProvincia("Lima");
        empresa.setDistrito("San Miguel");
        empresa.setUbigeo("150101");
        empresa.setLogoUrl("https://vetcare.pe/logo.png");
        empresa.setEstado("Activo");
        empresaRepository.save(empresa);
        
        // Create Usuarios
        String[] usuarios = {"veterinario1", "veterinario2", "veterinario3", "veterinario4", "veterinario5",
                            "asistente1", "asistente2", "asistente3", "asistente4", "asistente5",
                            "recepcionista1", "recepcionista2", "recepcionista3", "recepcionista4", "recepcionista5"};
        String[] nombres = {"Dr. Carlos Rodríguez", "Dra. María González", "Dr. Juan Pérez", "Dra. Ana Martínez", "Dr. Luis Sánchez",
                           "Laura García", "Pedro López", "Sofía Fernández", "Diego Torres", "Carmen Ruiz",
                           "Mónica Díaz", "Javier Castro", "Patricia Jiménez", "Roberto Moreno", "Isabel Navarro"};
        int[] roles = {2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
        String passwordHash = PasswordHasher.hash("password123");
        
        for (int i = 0; i < usuarios.length; i++) {
            Usuario u = new Usuario();
            u.setUsuario(usuarios[i]);
            u.setPasswordHash(passwordHash);
            u.setNombreCompleto(nombres[i]);
            u.setActivo(true);
            u.setIdRol(roles[i]);
            usuarioRepository.save(u);
        }
        
        // Create Clientes
        String[] nombresClientes = {"Carlos", "María", "Juan", "Ana", "Luis", "Sofía", "Pedro", "Carmen", "Diego", "Patricia",
                                    "Roberto", "Isabel", "Miguel", "Elena", "Fernando"};
        String[] apellidosClientes = {"Mendoza Quispe", "Flores Sánchez", "Ramírez Torres", "Gutiérrez López", "Castro Mendez",
                                     "Díaz Ramos", "Ortiz Vega", "Rojas Silva", "Morales Cordero", "Navarro Fierro",
                                     "Herrera Castillo", "Soto Benavides", "Álvarez Ríos", "Romero Campos", "Vargas Medina"};
        
        for (int i = 0; i < nombresClientes.length; i++) {
            Cliente c = new Cliente();
            c.setNombre(nombresClientes[i]);
            c.setApellidos(apellidosClientes[i]);
            c.setDni(String.valueOf(12345678 + i));
            c.setTelefono("98765432" + i);
            c.setWhatsapp("98765432" + i);
            c.setEmail(nombresClientes[i].toLowerCase() + "." + apellidosClientes[i].toLowerCase().replace(" ", "") + "@email.com");
            c.setFechaNacimiento(LocalDate.of(1980 + i, (i % 12) + 1, (i % 28) + 1));
            c.setEdad(30 + (i % 20));
            c.setGenero(i % 2 == 0 ? "Masculino" : "Femenino");
            c.setEstadoCivil(i % 3 == 0 ? "Casado" : "Soltero");
            c.setDireccion("Av. Principal " + (i * 100));
            c.setDepartamento("Lima");
            c.setProvincia("Lima");
            c.setDistrito("San Miguel");
            c.setEstado("Activo");
            clienteRepository.save(c);
        }
        
        // Create Mascotas
        String[] nombresMascotas = {"Max", "Luna", "Rocky", "Simba", "Coco", "Nala", "Thor", "Mia", "Bruno", "Bella",
                                   "Zeus", "Lola", "Rex", "Cleo", "Duke"};
        String[] especies = {"Perro", "Gato", "Perro", "Gato", "Perro", "Gato", "Perro", "Gato", "Perro", "Gato",
                            "Perro", "Gato", "Perro", "Gato", "Perro"};
        String[] razas = {"Golden Retriever", "Siamés", "Pastor Alemán", "Persa", "Bulldog Francés", "Maine Coon",
                         "Husky Siberiano", "Ragdoll", "Labrador", "Bengalí", "Doberman", "Sphynx", "Boxer", "Azul Ruso", "Rottweiler"};
        
        for (int i = 0; i < nombresMascotas.length; i++) {
            Mascota m = new Mascota();
            m.setNombre(nombresMascotas[i]);
            m.setEspecie(especies[i]);
            m.setRaza(razas[i]);
            m.setSexo(i % 2 == 0 ? "Macho" : "Hembra");
            m.setFechaNacimiento(LocalDate.of(2019 + i, (i % 12) + 1, (i % 28) + 1));
            m.setColor(i % 2 == 0 ? "Negro" : "Blanco");
            m.setMicrochip("MIC" + String.format("%03d", i + 1));
            m.setPeso((double) (5 + i * 2));
            m.setTipoPelaje(i % 2 == 0 ? "Largo" : "Corto");
            m.setTamano(i % 3 == 0 ? "Grande" : "Mediano");
            m.setEsterilizado(i % 2 == 0);
            m.setEstado("Activo");
            mascotaRepository.save(m);
        }
        
        // Create Productos
        String[] nombresProductos = {"Doxivet 100mg", "Amoxicilina 500mg", "Ivermectina 1%", "Royal Canin Adult", "Purina Pro Plan",
                                    "Hill's Science Diet", "Vacuna Rabia", "Vacuna Sextuple", "Correa Nylon", "Juguete Peluche",
                                    "Cama Ortopédica", "Champú Antipulgas", "Cepillo Doble", "Collar Antipulgas", "Suero Fisiológico"};
        String[] categorias = {"Medicamentos", "Medicamentos", "Medicamentos", "Alimentos", "Alimentos", "Alimentos",
                               "Vacunas", "Vacunas", "Accesorios", "Accesorios", "Accesorios", "Higiene", "Higiene", "Higiene", "Medicamentos"};
        
        for (int i = 0; i < nombresProductos.length; i++) {
            Producto p = new Producto();
            p.setNombre(nombresProductos[i]);
            p.setSku("PROD-" + String.format("%03d", i + 1));
            p.setCategoria(categorias[i]);
            p.setTipo(categorias[i].equals("Medicamentos") ? "Medicamento" : "Insumo");
            p.setPresentacion("Unidad");
            p.setUnidad("unidad");
            p.setDescripcion("Producto de alta calidad");
            p.setProveedor("Proveedor " + (i + 1));
            p.setPrecioCompra(new BigDecimal(10 + i * 5));
            p.setPrecioVenta(new BigDecimal(20 + i * 10));
            p.setMargen(new BigDecimal(50 + i * 5));
            p.setStockActual(50 + i * 10);
            p.setStockMinimo(10);
            p.setStockMaximo(100);
            p.setUbicacion("Almacén");
            p.setFechaVencimiento(LocalDate.of(2025 + i, 12, 31));
            p.setLote("L-2024-" + String.format("%03d", i + 1));
            p.setFabricante("Fabricante " + (i + 1));
            p.setAlmacenamiento("Temperatura ambiente");
            productoRepository.save(p);
        }
        
        // Create Proveedores
        String[] nombresProveedores = {"VetPharma Perú", "Royal Canin Perú", "Zoetis Perú", "Bayer Animal Health", "Purina Pro Plan",
                                       "Merial Perú", "Hill's Pet Nutrition", "Elanco Animal Health", "Virbac Perú", "PetCare Solutions",
                                       "NutriDog Perú", "VetLab Diagnostics", "Animal Health International", "Quality Pet Products", "BioVet Laboratories"};
        
        for (int i = 0; i < nombresProveedores.length; i++) {
            Proveedor p = new Proveedor();
            p.setCodigo("PROV-" + String.format("%04d", i + 1));
            p.setNombre(nombresProveedores[i]);
            p.setRuc("20" + String.format("%010d", 123456789L + i));
            p.setRubro(i % 2 == 0 ? "Medicamentos" : "Alimentos");
            p.setTelefono("511-555-" + String.format("%04d", 100 + i));
            p.setEmail("contacto@proveedor" + (i + 1) + ".com");
            p.setEstado("Activo");
            p.setDireccion("Av. Industrial " + (i * 100));
            proveedorRepository.save(p);
        }
        
        // Create Citas
        for (int i = 0; i < 15; i++) {
            Cita c = new Cita();
            Cliente cliente = clienteRepository.findById((long) (i + 1)).orElse(null);
            Mascota mascota = mascotaRepository.findById((long) (i + 1)).orElse(null);
            c.setCliente(cliente);
            c.setMascota(mascota);
            c.setTipoCita(i % 2 == 0 ? "Consulta General" : "Vacunación");
            c.setFecha(LocalDate.now().plusDays(i));
            c.setHora(LocalTime.of(9 + i, 0));
            c.setMotivo("Revisión de rutina");
            c.setDuracion(30);
            c.setVeterinario("Dr. Carlos Rodríguez");
            c.setConsultorio("Consultorio " + ((i % 3) + 1));
            c.setRecordatorio(true);
            c.setWhatsapp(true);
            c.setEmail(true);
            citaRepository.save(c);
        }
        
        // Create Vacunas
        for (int i = 0; i < 15; i++) {
            Vacuna v = new Vacuna();
            Mascota mascota = mascotaRepository.findById((long) (i + 1)).orElse(null);
            v.setMascota(mascota);
            v.setVacuna(i % 2 == 0 ? "Vacuna Sextuple" : "Antirrábica");
            v.setLote("L-2024-" + String.format("%03d", i + 1));
            v.setLaboratorio("BioVet Laboratories");
            v.setFechaAplicacion(LocalDate.now().minusDays(i * 10));
            v.setProximaDosis(LocalDate.now().plusDays(365 - i * 10));
            v.setAplicadaPor("Dr. Carlos Rodríguez");
            v.setViaAplicacion("Subcutánea");
            v.setSitioAplicacion("Cuello");
            v.setEstado("Aplicada");
            v.setTipoRecordatorio("Email");
            v.setDiasAntes(7);
            vacunaRepository.save(v);
        }
        
        // Create Facturas
        for (int i = 0; i < 15; i++) {
            Factura f = new Factura();
            f.setNumero("F-" + String.format("%06d", i + 1));
            f.setFecha(LocalDate.now().minusDays(i));
            f.setCliente("Cliente " + (i + 1));
            f.setTelefono("98765432" + i);
            f.setMascota("Mascota " + (i + 1));
            f.setRaza("Raza " + (i + 1));
            f.setTotal(new BigDecimal(100 + i * 10));
            f.setEstado(i % 3 == 0 ? "Pendiente" : "Pagada");
            f.setMetodoPago(i % 2 == 0 ? "Efectivo" : "Tarjeta");
            facturaRepository.save(f);
        }
        
        // Create Auditoria
        for (int i = 0; i < 15; i++) {
            Auditoria a = new Auditoria();
            a.setFechaHora(LocalDate.now().atTime(8 + i, 0));
            a.setUsuario("alexmurga");
            a.setAccion(i % 2 == 0 ? "CREAR" : "EDITAR");
            a.setModulo(i % 3 == 0 ? "CLIENTES" : i % 3 == 1 ? "MASCOTAS" : "CITAS");
            a.setDescripcion("Acción de prueba " + (i + 1));
            a.setIpDispositivo("192.168.1." + (100 + i));
            a.setNavegador("Chrome 120.0");
            a.setSeveridad("INFO");
            a.setEstado("EXITOSO");
            auditoriaRepository.save(a);
        }
        
        System.out.println(">>> Datos de ejemplo creados exitosamente");
    }
}
