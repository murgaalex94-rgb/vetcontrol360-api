-- ============================================================================
-- VetControl 360 - Base de Datos Completa para PostgreSQL (Neon)
-- ============================================================================
-- Este script crea toda la estructura de la base de datos y carga datos iniciales
-- Compatible con: PostgreSQL 15+, Neon Database, Spring Boot, Hibernate/JPA
-- ============================================================================

-- ============================================================================
-- FASE 1: ELIMINACIÓN DE OBJETOS EXISTENTES
-- ============================================================================
-- Se eliminan las tablas en orden inverso de dependencias para evitar errores
-- por claves foráneas. CASCADE elimina objetos dependientes automáticamente.

DROP TABLE IF EXISTS auditoria CASCADE;
DROP TABLE IF EXISTS facturas CASCADE;
DROP TABLE IF EXISTS citas CASCADE;
DROP TABLE IF EXISTS vacunas CASCADE;
DROP TABLE IF EXISTS productos CASCADE;
DROP TABLE IF EXISTS mascotas CASCADE;
DROP TABLE IF EXISTS clientes CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- ============================================================================
-- FASE 2: CREACIÓN DEL ESQUEMA
-- ============================================================================

-- Tabla: usuarios
-- Sistema de autenticación. Los roles se almacenan como enteros:
-- 1 = ADMIN, 2 = VETERINARIO, 3 = RECEPCIONISTA
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    activo BOOLEAN,
    id_rol INTEGER NOT NULL
);

-- Tabla: clientes
-- Información completa de los clientes de la veterinaria
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255),
    dni VARCHAR(255) UNIQUE,
    telefono VARCHAR(255),
    whatsapp VARCHAR(255),
    email VARCHAR(255),
    fecha_nacimiento DATE,
    edad INTEGER,
    genero VARCHAR(50),
    estado_civil VARCHAR(50),
    direccion VARCHAR(255),
    departamento VARCHAR(100),
    provincia VARCHAR(100),
    distrito VARCHAR(100),
    referencia TEXT,
    notas TEXT,
    estado VARCHAR(50) NOT NULL DEFAULT 'Activo'
);

-- Tabla: mascotas
-- Mascotas de los clientes. Relacionada con clientes mediante cliente_id
CREATE TABLE mascotas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    especie VARCHAR(100),
    raza VARCHAR(100),
    sexo VARCHAR(50),
    fecha_nacimiento DATE,
    color VARCHAR(100),
    microchip VARCHAR(100),
    peso DOUBLE PRECISION,
    tipo_pelaje VARCHAR(100),
    tamano VARCHAR(50),
    esterilizado BOOLEAN,
    notas TEXT,
    estado VARCHAR(50) DEFAULT 'Activo',
    foto_url VARCHAR(500),
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

-- Tabla: productos
-- Inventario de productos de la veterinaria
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    sku VARCHAR(100) UNIQUE,
    categoria VARCHAR(100),
    tipo VARCHAR(100),
    presentacion VARCHAR(100),
    unidad VARCHAR(50),
    descripcion TEXT,
    proveedor VARCHAR(255),
    precio_compra DECIMAL(10, 2),
    precio_venta DECIMAL(10, 2),
    margen DECIMAL(10, 2),
    stock_actual INTEGER,
    stock_minimo INTEGER,
    stock_maximo INTEGER,
    ubicacion VARCHAR(100),
    fecha_vencimiento DATE,
    lote VARCHAR(100),
    fabricante VARCHAR(255),
    almacenamiento VARCHAR(100),
    registro_sanitario VARCHAR(100),
    notas TEXT,
    imagen VARCHAR(500)
);

-- Tabla: vacunas
-- Registro de vacunas aplicadas a las mascotas. Relacionada con mascotas
CREATE TABLE vacunas (
    id BIGSERIAL PRIMARY KEY,
    mascota_id BIGINT NOT NULL,
    vacuna VARCHAR(255) NOT NULL,
    lote VARCHAR(100),
    laboratorio VARCHAR(255),
    fecha_aplicacion DATE,
    proxima_dosis DATE,
    aplicada_por VARCHAR(255),
    via_aplicacion VARCHAR(100),
    sitio_aplicacion VARCHAR(100),
    estado VARCHAR(50),
    observaciones TEXT,
    tipo_recordatorio VARCHAR(50),
    dias_antes INTEGER,
    notas_adicionales TEXT,
    CONSTRAINT fk_vacuna_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE
);

-- Tabla: citas
-- Citas médicas programadas. Relacionada con clientes y mascotas
CREATE TABLE citas (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    mascota_id BIGINT NOT NULL,
    tipo_cita VARCHAR(100),
    fecha DATE,
    hora TIME,
    motivo TEXT,
    duracion INTEGER,
    veterinario VARCHAR(255),
    consultorio VARCHAR(100),
    notas TEXT,
    recordatorio BOOLEAN,
    whatsapp BOOLEAN,
    email BOOLEAN,
    CONSTRAINT fk_cita_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    CONSTRAINT fk_cita_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE
);

-- Tabla: facturas
-- Facturación de servicios y productos
CREATE TABLE facturas (
    id BIGSERIAL PRIMARY KEY,
    numero VARCHAR(100) UNIQUE,
    fecha DATE,
    cliente VARCHAR(255),
    telefono VARCHAR(255),
    mascota VARCHAR(255),
    raza VARCHAR(100),
    foto VARCHAR(500),
    total DECIMAL(10, 2),
    estado VARCHAR(50),
    metodo_pago VARCHAR(100)
);

-- Tabla: auditoria
-- Registro de acciones realizadas en el sistema para trazabilidad
CREATE TABLE auditoria (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    usuario VARCHAR(255) NOT NULL,
    accion VARCHAR(255) NOT NULL,
    modulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    ip_dispositivo VARCHAR(100),
    navegador VARCHAR(500),
    severidad VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL
);

-- ============================================================================
-- ÍNDICES PARA MEJORAR RENDIMIENTO
-- ============================================================================

CREATE INDEX idx_clientes_dni ON clientes(dni);
CREATE INDEX idx_clientes_estado ON clientes(estado);
CREATE INDEX idx_clientes_apellidos ON clientes(apellidos);

CREATE INDEX idx_mascotas_cliente ON mascotas(cliente_id);
CREATE INDEX idx_mascotas_estado ON mascotas(estado);
CREATE INDEX idx_mascotas_especie ON mascotas(especie);

CREATE INDEX idx_productos_sku ON productos(sku);
CREATE INDEX idx_productos_categoria ON productos(categoria);
CREATE INDEX idx_productos_estado ON productos(stock_actual);

CREATE INDEX idx_vacunas_mascota ON vacunas(mascota_id);
CREATE INDEX idx_vacunas_fecha ON vacunas(fecha_aplicacion);
CREATE INDEX idx_vacunas_proxima ON vacunas(proxima_dosis);

CREATE INDEX idx_citas_cliente ON citas(cliente_id);
CREATE INDEX idx_citas_mascota ON citas(mascota_id);
CREATE INDEX idx_citas_fecha ON citas(fecha);
CREATE INDEX idx_citas_estado ON citas(fecha);

CREATE INDEX idx_facturas_numero ON facturas(numero);
CREATE INDEX idx_facturas_fecha ON facturas(fecha);
CREATE INDEX idx_facturas_estado ON facturas(estado);

CREATE INDEX idx_auditoria_fecha ON auditoria(fecha_hora);
CREATE INDEX idx_auditoria_usuario ON auditoria(usuario);
CREATE INDEX idx_auditoria_modulo ON auditoria(modulo);

-- ============================================================================
-- FASE 3: DATOS INICIALES
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Usuarios del sistema
-- ----------------------------------------------------------------------------
-- Hash PBKDF2WithHmacSHA256 (100000 iteraciones, 256 bits)
-- Formato: ITERATIONS:SALT_BASE64:HASH_BASE64

-- Usuario: Alex Murga (Administrador)
-- Contraseña: AlexMurga.20*
INSERT INTO usuarios (usuario, password_hash, nombre_completo, activo, id_rol) VALUES
('Alex Murga', '100000:+sNtVQO3fPWPpOHLYHjvIg==:+Tq6GMmhiXfW7dw0Orp9rptKwSMUTfrX/LOiKLomiMY=', 'Alex Murga', true, 1);

-- Usuarios adicionales para prueba
INSERT INTO usuarios (usuario, password_hash, nombre_completo, activo, id_rol) VALUES
('veterinario1', '100000:ABC123XYZ789==:DEF456GHI789JKL012MNO345PQR678STU901VWX=', 'Dr. Carlos Ramírez Torres', true, 2),
('veterinario2', '100000:XYZ789ABC123==:GHI789JKL012MNO345PQR678STU901VWXDEF456=', 'Dra. María Fernanda López García', true, 2),
('veterinario3', '100000:DEF456GHI789==:JKL012MNO345PQR678STU901VWXDEF456GHI789=', 'Dr. Juan Diego Torres Rivas', true, 2),
('recepcionista1', '100000:GHI789JKL012==:MNO345PQR678STU901VWXDEF456GHI789JKL012=', 'Ana Gabriela Silva Paredes', true, 3),
('recepcionista2', '100000:JKL012MNO345==:PQR678STU901VWXDEF456GHI789JKL012MNO345=', 'Luis Enrique Herrera Campos', true, 3);

-- ----------------------------------------------------------------------------
-- Clientes (15 registros realistas)
-- ----------------------------------------------------------------------------
INSERT INTO clientes (nombre, apellidos, dni, telefono, whatsapp, email, fecha_nacimiento, edad, genero, estado_civil, direccion, departamento, provincia, distrito, referencia, notas, estado) VALUES
('Carlos Alberto', 'Ramírez Torres', '45678912', '987654321', '987654321', 'carlos.ramirez@gmail.com', '1985-03-15', 39, 'Masculino', 'Casado', 'Av. Los Olivos 123', 'Lima', 'Lima', 'San Isidro', 'Cerca al parque central', 'Cliente frecuente, muy puntual', 'Activo'),
('María Fernanda', 'López García', '78945612', '912345678', '912345678', 'maria.lopez@hotmail.com', '1990-07-22', 34, 'Femenino', 'Soltera', 'Jr. Las Flores 456', 'Lima', 'Lima', 'Miraflores', 'Altura cuadra 5', 'Prefiere citas por la mañana', 'Activo'),
('Juan Diego', 'Torres Rivas', '32165498', '923456789', NULL, 'juan.torres@gmail.com', '1982-11-08', 42, 'Masculino', 'Divorciado', 'Calle Real 789', 'Junín', 'Huancayo', 'Huancayo', 'Frente al mercado central', 'Tiene 3 mascotas', 'Activo'),
('Ana Gabriela', 'Silva Paredes', '65498732', '934567890', '934567890', 'ana.silva@gmail.com', '1995-05-30', 29, 'Femenino', 'Casada', 'Av. Primavera 321', 'Arequipa', 'Arequipa', 'Yanahuara', 'Cerca del mirador', NULL, 'Activo'),
('Luis Enrique', 'Herrera Campos', '98712345', '945678901', NULL, 'luis.herrera@gmail.com', '1978-09-14', 46, 'Masculino', 'Soltero', 'Pasaje Los Pinos 654', 'La Libertad', 'Trujillo', 'Víctor Larco', 'Edificio Los Laureles depto 402', 'Cliente VIP', 'Inactivo'),
('Patricia', 'Mendoza Rojas', '14725836', '956789012', '956789012', 'patricia.mendoza@gmail.com', '2000-01-25', 24, 'Femenino', 'Soltera', 'Jr. Las Palmeras 987', 'Cusco', 'Cusco', 'Cusco', 'Cerca de la plaza de armas', NULL, 'Activo'),
('Roberto Carlos', 'Sánchez Méndez', '85274196', '967890123', '967890123', 'roberto.sanchez@yahoo.com', '1988-06-18', 36, 'Masculino', 'Casado', 'Av. Brasil 159', 'Lima', 'Lima', 'Surco', 'Frente al centro comercial', 'Alergias a penicilina', 'Activo'),
('Cecilia Beatriz', 'Rojas Castillo', '36925814', '978901234', NULL, 'cecilia.rojas@gmail.com', '1992-12-03', 32, 'Femenino', 'Casada', 'Calle Los Robles 753', 'Piura', 'Piura', 'Piura', 'Urbanización San Martín', NULL, 'Activo'),
('Jorge Luis', 'Fernández Ortega', '74185296', '989012345', '989012345', 'jorge.fernandez@hotmail.com', '1980-04-10', 44, 'Masculino', 'Divorciado', 'Av. Panamericana 357', 'Lambayeque', 'Chiclayo', 'Chiclayo', 'Zona industrial', 'Veterinario de profesión', 'Activo'),
('Elena María', 'Gutiérrez Vega', '15935748', '991234567', '991234567', 'elena.gutierrez@gmail.com', '1997-08-25', 27, 'Femenino', 'Soltera', 'Jr. Los Claveles 246', 'Lima', 'Lima', 'San Borja', 'Cerca del colegio', NULL, 'Activo'),
('Miguel Ángel', 'Ruíz Soto', '95175346', '902345678', NULL, 'miguel.ruiz@gmail.com', '1975-02-28', 49, 'Masculino', 'Casado', 'Av. Benavides 864', 'Lima', 'Lima', 'Santiago de Surco', 'Esquina con Av. Primavera', 'Cliente desde 2018', 'Activo'),
('Sofía Alejandra', 'Morales Cordero', '35715964', '913456789', '913456789', 'sofia.morales@yahoo.com', '1993-11-12', 31, 'Femenino', 'Casada', 'Calle Los Eucaliptos 135', 'Ancash', 'Huaraz', 'Huaraz', 'Urbanización Santa Rosa', NULL, 'Activo'),
('Diego Armando', 'Navarro Peralta', '46825937', '924567890', '924567890', 'diego.navarro@gmail.com', '1986-07-07', 38, 'Masculino', 'Soltero', 'Av. Ejército 975', 'Lima', 'Lima', 'San Miguel', 'Cerca del parque Kennedy', 'Prefiere citas vespertinas', 'Activo'),
('Valentina', 'Castro Benavides', '57394826', '935678901', NULL, 'valentina.castro@hotmail.com', '1999-03-19', 25, 'Femenino', 'Soltera', 'Jr. Los Nogales 468', 'Ica', 'Ica', 'Ica', 'Zona residencial', NULL, 'Activo'),
('Andrés Felipe', 'Mendoza Quispe', '68415937', '946789012', '946789012', 'andres.mendoza@gmail.com', '1984-10-30', 40, 'Masculino', 'Casado', 'Av. La Marina 246', 'Lima', 'Lima', 'San Borja', 'Edificio Torre Central piso 8', 'Cliente corporativo', 'Activo');

-- ----------------------------------------------------------------------------
-- Mascotas (15 registros, una por cliente para mantener coherencia)
-- ----------------------------------------------------------------------------
INSERT INTO mascotas (nombre, especie, raza, sexo, fecha_nacimiento, color, microchip, peso, tipo_pelaje, tamano, esterilizado, notas, estado, foto_url, cliente_id) VALUES
('Max', 'Perro', 'Golden Retriever', 'Macho', '2020-05-12', 'Dorado', 'MC985632147012', 32.5, 'Largo', 'Grande', true, 'Muy activo y amigable', 'Activo', NULL, 1),
('Luna', 'Gato', 'Siamés', 'Hembra', '2021-08-03', 'Crema y gris', 'MC874521369025', 4.2, 'Corto', 'Pequeño', true, 'Un poco tímida', 'Activo', NULL, 2),
('Rocky', 'Perro', 'Pastor Alemán', 'Macho', '2019-11-20', 'Negro y fuego', 'MC763410258036', 38.0, 'Corto', 'Grande', false, 'Excelente guardián', 'Activo', NULL, 3),
('Mimi', 'Gato', 'Persa', 'Hembra', '2022-03-15', 'Blanco', 'MC652309147047', 3.8, 'Largo', 'Pequeño', false, NULL, 'Activo', NULL, 4),
('Toby', 'Perro', 'Labrador Retriever', 'Macho', '2023-01-08', 'Chocolate', 'MC541208036058', 28.0, 'Corto', 'Mediano', false, 'Muy juguetón', 'Activo', NULL, 5),
('Coco', 'Gato', 'Maine Coon', 'Hembra', '2021-06-22', 'Atigrado', 'MC430107925069', 6.5, 'Largo', 'Grande', true, NULL, 'Activo', NULL, 6),
('Buddy', 'Perro', 'Bulldog Francés', 'Macho', '2022-09-10', 'Café', 'MC329006814070', 11.2, 'Corto', 'Pequeño', true, 'Problemas respiratorios leves', 'Activo', NULL, 7),
('Nala', 'Gato', 'Bengalí', 'Hembra', '2020-12-05', 'Leopardo', 'MC218905703081', 5.1, 'Corto', 'Mediano', true, NULL, 'Activo', NULL, 8),
('Duke', 'Perro', 'Rottweiler', 'Macho', '2018-04-18', 'Negro con marcas marrón', 'MC107804692092', 45.0, 'Corto', 'Grande', true, 'Entrenado en obediencia', 'Activo', NULL, 9),
('Bella', 'Perro', 'Beagle', 'Hembra', '2021-02-14', 'Tricolor', 'MC996703581103', 12.8, 'Corto', 'Mediano', true, 'Muy vocal', 'Activo', NULL, 10),
('Simba', 'Gato', 'Sphynx', 'Macho', '2022-07-30', 'Rosa grisáceo', 'MC885602470114', 4.9, 'Sin pelo', 'Mediano', true, NULL, 'Activo', NULL, 11),
('Charlie', 'Perro', 'Border Collie', 'Macho', '2020-10-25', 'Negro y blanco', 'MC774501369125', 22.0, 'Mediano', 'Mediano', true, 'Muy inteligente', 'Activo', NULL, 12),
('Lola', 'Perro', 'Chihuahua', 'Hembra', '2023-05-18', 'Fawn', 'MC663400258136', 2.5, 'Corto', 'Pequeño', false, NULL, 'Activo', NULL, 13),
('Oliver', 'Gato', 'Ragdoll', 'Macho', '2021-11-09', 'Azul punto', 'MC552399147247', 7.2, 'Largo', 'Grande', true, NULL, 'Activo', NULL, 14),
('Zeus', 'Perro', 'Doberman', 'Macho', '2019-08-02', 'Negro con marcas marrón', 'MC441298036258', 42.5, 'Corto', 'Grande', true, NULL, 'Activo', NULL, 15);

-- ----------------------------------------------------------------------------
-- Productos (15 registros de inventario)
-- ----------------------------------------------------------------------------
INSERT INTO productos (nombre, sku, categoria, tipo, presentacion, unidad, descripcion, proveedor, precio_compra, precio_venta, margen, stock_actual, stock_minimo, stock_maximo, ubicacion, fecha_vencimiento, lote, fabricante, almacenamiento, registro_sanitario, notas, imagen) VALUES
('Amoxicilina 500 mg', 'MED-001', 'Medicamentos', 'Antibiótico', 'Tabletas', 'Caja 20', 'Antibiótico de amplio espectro para infecciones bacterianas', 'Laboratorios VetPharma', 25.00, 45.00, 20.00, 150, 30, 200, 'Estante A1', '2026-12-31', 'LOT-2024-001', 'VetPharma S.A.', 'Ambiente seco', 'REG-12345', NULL, NULL),
('Ivermectina 1%', 'MED-002', 'Medicamentos', 'Antiparasitario', 'Solución inyectable', 'Frasco 50 ml', 'Antiparasitario interno y externo', 'PetCare Solutions', 35.00, 65.00, 30.00, 80, 15, 100, 'Estante A2', '2027-06-30', 'LOT-2024-002', 'PetCare Inc.', 'Lugar fresco', 'REG-67890', NULL, NULL),
('Meloxicam 1.5 mg', 'MED-003', 'Medicamentos', 'Antiinflamatorio', 'Tabletas', 'Caja 10', 'Antiinflamatorio no esteroideo para dolor e inflamación', 'Zoetis Perú', 18.00, 32.00, 14.00, 120, 25, 150, 'Estante A3', '2026-03-15', 'LOT-2024-003', 'Zoetis Inc.', 'Ambiente seco', 'REG-24680', NULL, NULL),
('Royal Canin Adulto', 'ALI-001', 'Alimentos', 'Alimento balanceado', 'Bolsa', 'kg', 'Alimento premium para perros adultos', 'Royal Canin', 45.00, 75.00, 30.00, 200, 40, 300, 'Estante B1', '2025-08-20', 'LOT-2024-004', 'Royal Canin SAS', 'Lugar seco', 'REG-13579', NULL, NULL),
('Pro Plan Gato Adulto', 'ALI-002', 'Alimentos', 'Alimento balanceado', 'Bolsa', 'kg', 'Alimento premium para gatos adultos', 'Purina', 38.00, 62.00, 24.00, 180, 35, 250, 'Estante B2', '2025-10-15', 'LOT-2024-005', 'Nestlé Purina', 'Lugar seco', 'REG-97531', NULL, NULL),
('Collar Antipulgas Seresto', 'ACC-001', 'Accesorios', 'Antiparasitario externo', 'Collar', 'unidad', 'Protección contra pulgas y garrapatas por 8 meses', 'Bayer', 85.00, 145.00, 60.00, 45, 10, 80, 'Estante C1', '2028-01-01', 'LOT-2024-006', 'Bayer AG', 'Ambiente seco', 'REG-86420', NULL, NULL),
('Jaula Transporte Mediana', 'ACC-002', 'Accesorios', 'Transporte', 'Jaula', 'unidad', 'Jaula de transporte plástica para perros medianos', 'Petmate', 65.00, 110.00, 45.00, 25, 5, 50, 'Almacén 2', NULL, 'LOT-2024-007', 'Petmate Inc.', NULL, NULL, NULL, NULL),
('Vacuna Sextuple Canina', 'VAC-001', 'Vacunas', 'Vacuna', 'Frasco', 'dosis', 'Vacuna múltiple para cachorros', 'Boehringer Ingelheim', 28.00, 55.00, 27.00, 60, 15, 100, 'Refrigerador 1', '2025-04-30', 'LOT-2024-008', 'Boehringer Ingelheim Vetmedica', 'Refrigeración 2-8°C', 'REG-11223', NULL, NULL),
('Vacuna Antirrábica', 'VAC-002', 'Vacunas', 'Vacuna', 'Frasco', 'dosis', 'Vacuna contra la rabia', 'Merial', 22.00, 42.00, 20.00, 75, 20, 120, 'Refrigerador 1', '2025-06-15', 'LOT-2024-009', 'Merial SAS', 'Refrigeración 2-8°C', 'REG-33445', NULL, NULL),
('Shampoo Antipulgas', 'HIG-001', 'Higiene', 'Shampoo', 'Frasco', 'ml', 'Shampoo con piretrinas para control de pulgas', 'Virbac', 15.00, 28.00, 13.00, 90, 20, 150, 'Estante D1', '2026-09-01', 'LOT-2024-010', 'Virbac Perú', 'Ambiente seco', 'REG-55667', NULL, NULL),
('Pipeta Antiparasitaria Gato', 'PAR-001', 'Parasitarios', 'Antiparasitario externo', 'Pipeta', 'unidad', 'Pipeta spot-on para gatos', 'Elanco', 20.00, 38.00, 18.00, 100, 25, 150, 'Estante D2', '2027-02-28', 'LOT-2024-011', 'Elanco Animal Health', 'Lugar fresco', 'REG-77889', NULL, NULL),
('Suero Fisiológico 500 ml', 'QUI-001', 'Quirúrgico', 'Solución', 'Frasco', 'ml', 'Solución salina estéril para hidratación', 'Baxter', 8.00, 18.00, 10.00, 200, 50, 300, 'Estante E1', '2026-05-20', 'LOT-2024-012', 'Baxter International', 'Ambiente seco', 'REG-99001', NULL, NULL),
('Guantes de Nitrilo XL', 'QUI-002', 'Quirúrgico', 'Material desechable', 'Caja', 'pares', 'Guantes de examen desechables', 'Ansell', 12.00, 25.00, 13.00, 300, 80, 500, 'Estante E2', '2027-12-31', 'LOT-2024-013', 'Ansell Limited', 'Ambiente seco', NULL, NULL, NULL),
('Jeringa 5 ml', 'QUI-003', 'Quirúrgico', 'Material desechable', 'Paquete', 'unidades', 'Jeringas desechables con aguja', 'BD', 5.00, 12.00, 7.00, 500, 100, 800, 'Estante E3', '2028-06-30', 'LOT-2024-014', 'Becton Dickinson', 'Ambiente seco', NULL, NULL, NULL),
('Gasa Esterilizada 10x10', 'QUI-004', 'Quirúrgico', 'Material desechable', 'Paquete', 'unidades', 'Gasas esterilizadas para curaciones', 'Kimberly-Clark', 3.00, 8.00, 5.00, 400, 100, 600, 'Estante E4', '2027-08-15', 'LOT-2024-015', 'Kimberly-Clark', 'Ambiente seco', NULL, NULL, NULL);

-- ----------------------------------------------------------------------------
-- Vacunas (15 registros, relacionadas con mascotas)
-- ----------------------------------------------------------------------------
INSERT INTO vacunas (mascota_id, vacuna, lote, laboratorio, fecha_aplicacion, proxima_dosis, aplicada_por, via_aplicacion, sitio_aplicacion, estado, observaciones, tipo_recordatorio, dias_antes, notas_adicionales) VALUES
(1, 'Rabia', 'VAC-2024-001', 'Laboratorios BioVet', '2024-01-15', '2025-01-15', 'Dr. Carlos Ramírez Torres', 'Subcutánea', 'Cuello', 'Aplicada', 'Reacción normal', 'Email', 7, NULL),
(1, 'Séxtuple', 'VAC-2024-002', 'Laboratorios BioVet', '2024-02-20', '2025-02-20', 'Dr. Carlos Ramírez Torres', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(2, 'Triple Felina', 'VAC-2024-003', 'Laboratorios CatVet', '2024-03-10', '2025-03-10', 'Dra. María Fernanda López García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(3, 'Rabia', 'VAC-2024-004', 'Laboratorios BioVet', '2024-01-20', '2025-01-20', 'Dr. Carlos Ramírez Torres', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(4, 'Triple Felina', 'VAC-2024-005', 'Laboratorios CatVet', '2024-04-05', '2025-04-05', 'Dra. María Fernanda López García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(5, 'Séxtuple', 'VAC-2024-006', 'Laboratorios BioVet', '2024-05-12', '2025-05-12', 'Dr. Juan Diego Torres Rivas', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(6, 'Leucemia Felina', 'VAC-2024-007', 'Laboratorios CatVet', '2024-06-18', '2025-06-18', 'Dra. María Fernanda López García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(7, 'Rabia', 'VAC-2024-008', 'Laboratorios BioVet', '2024-03-25', '2025-03-25', 'Dr. Carlos Ramírez Torres', 'Subcutánea', 'Cuello', 'Aplicada', 'Leve inflamación local', 'Email', 7, NULL),
(8, 'Triple Felina', 'VAC-2024-009', 'Laboratorios CatVet', '2024-04-30', '2025-04-30', 'Dra. María Fernanda López García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(9, 'Séxtuple', 'VAC-2024-010', 'Laboratorios BioVet', '2024-02-14', '2025-02-14', 'Dr. Juan Diego Torres Rivas', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(10, 'Rabia', 'VAC-2024-011', 'Laboratorios BioVet', '2024-05-20', '2025-05-20', 'Dr. Carlos Ramírez Torres', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(11, 'Triple Felina', 'VAC-2024-012', 'Laboratorios CatVet', '2024-06-25', '2025-06-25', 'Dra. María Fernanda López García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(12, 'Séxtuple', 'VAC-2024-013', 'Laboratorios BioVet', '2024-04-10', '2025-04-10', 'Dr. Juan Diego Torres Rivas', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(13, 'Rabia', 'VAC-2024-014', 'Laboratorios BioVet', '2024-07-05', '2025-07-05', 'Dr. Carlos Ramírez Torres', 'Subcutánea', 'Cuello', 'Pendiente', NULL, 'Email', 7, NULL),
(14, 'Triple Felina', 'VAC-2024-015', 'Laboratorios CatVet', '2024-05-15', '2025-05-15', 'Dra. María Fernanda López García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(15, 'Séxtuple', 'VAC-2024-016', 'Laboratorios BioVet', '2024-06-20', '2025-06-20', 'Dr. Juan Diego Torres Rivas', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL);

-- ----------------------------------------------------------------------------
-- Citas (15 registros, relacionadas con clientes y mascotas)
-- ----------------------------------------------------------------------------
INSERT INTO citas (cliente_id, mascota_id, tipo_cita, fecha, hora, motivo, duracion, veterinario, consultorio, notas, recordatorio, whatsapp, email) VALUES
(1, 1, 'Consulta General', '2024-07-15', '09:30:00', 'Revisión anual de rutina', 30, 'Dr. Carlos Ramírez Torres', 'Consultorio 1', 'Mascota en buen estado de salud', true, true, true),
(2, 2, 'Vacunación', '2024-07-16', '10:00:00', 'Aplicación de vacuna anual', 15, 'Dra. María Fernanda López García', 'Consultorio 2', NULL, true, false, true),
(3, 3, 'Control / Revisión', '2024-07-17', '11:30:00', 'Seguimiento post-operatorio', 20, 'Dr. Juan Diego Torres Rivas', 'Consultorio 1', 'Cicatrización normal', true, true, false),
(4, 4, 'Consulta General', '2024-07-18', '14:00:00', 'Chequeo de rutina', 30, 'Dra. María Fernanda López García', 'Consultorio 2', NULL, true, true, true),
(5, 5, 'Desparasitación', '2024-07-19', '15:30:00', 'Desparasitación interna', 15, 'Dr. Carlos Ramírez Torres', 'Consultorio 1', NULL, false, true, false),
(6, 6, 'Consulta General', '2024-07-22', '09:00:00', 'Control de peso', 20, 'Dra. María Fernanda López García', 'Consultorio 2', NULL, true, true, true),
(7, 7, 'Cirugía', '2024-07-23', '10:30:00', 'Castración programada', 60, 'Dr. Juan Diego Torres Rivas', 'Quirófano 1', 'Ayuno de 12 horas requerido', true, true, true),
(8, 8, 'Vacunación', '2024-07-24', '11:00:00', 'Refuerzo anual', 15, 'Dra. María Fernanda López García', 'Consultorio 2', NULL, true, false, true),
(9, 9, 'Consulta General', '2024-07-25', '14:30:00', 'Revisión ortopédica', 30, 'Dr. Carlos Ramírez Torres', 'Consultorio 1', 'Historial de displasia de cadera', true, true, true),
(10, 10, 'Control / Revisión', '2024-07-26', '16:00:00', 'Control dermatológico', 25, 'Dra. María Fernanda López García', 'Consultorio 2', 'Alergias alimentarias', true, true, false),
(11, 11, 'Consulta General', '2024-07-29', '09:15:00', 'Chequeo de rutina', 30, 'Dr. Juan Diego Torres Rivas', 'Consultorio 1', NULL, true, true, true),
(12, 12, 'Vacunación', '2024-07-30', '10:45:00', 'Vacuna antirrábica', 15, 'Dr. Carlos Ramírez Torres', 'Consultorio 2', NULL, true, false, true),
(13, 13, 'Consulta General', '2024-07-31', '13:30:00', 'Primer chequeo', 30, 'Dra. María Fernanda López García', 'Consultorio 1', 'Cachorro de 3 meses', true, true, true),
(14, 14, 'Control / Revisión', '2024-08-01', '15:00:00', 'Seguimiento nutricional', 20, 'Dr. Juan Diego Torres Rivas', 'Consultorio 2', NULL, true, true, false),
(15, 15, 'Consulta General', '2024-08-02', '11:00:00', 'Revisión anual', 30, 'Dr. Carlos Ramírez Torres', 'Consultorio 1', NULL, true, true, true);

-- ----------------------------------------------------------------------------
-- Facturas (15 registros)
-- ----------------------------------------------------------------------------
INSERT INTO facturas (numero, fecha, cliente, telefono, mascota, raza, foto, total, estado, metodo_pago) VALUES
('F-000128', '2024-07-15', 'Carlos Alberto Ramírez Torres', '987654321', 'Max', 'Golden Retriever', NULL, 350.00, 'Pagada', 'Tarjeta'),
('F-000127', '2024-07-16', 'María Fernanda López García', '912345678', 'Luna', 'Siamés', NULL, 180.00, 'Pagada', 'Yape'),
('F-000126', '2024-07-17', 'Juan Diego Torres Rivas', '923456789', 'Rocky', 'Pastor Alemán', NULL, 220.00, 'Pendiente', 'Transferencia'),
('F-000125', '2024-07-18', 'Ana Gabriela Silva Paredes', '934567890', 'Mimi', 'Persa', NULL, 95.00, 'Pagada', 'Efectivo'),
('F-000124', '2024-07-19', 'Luis Enrique Herrera Campos', '945678901', 'Toby', 'Labrador Retriever', NULL, 145.00, 'Pagada', 'Plin'),
('F-000123', '2024-07-22', 'Patricia Mendoza Rojas', '956789012', 'Coco', 'Maine Coon', NULL, 275.00, 'Pagada', 'Tarjeta'),
('F-000122', '2024-07-23', 'Roberto Carlos Sánchez Méndez', '967890123', 'Buddy', 'Bulldog Francés', NULL, 580.00, 'Pagada', 'Transferencia'),
('F-000121', '2024-07-24', 'Cecilia Beatriz Rojas Castillo', '978901234', 'Nala', 'Bengalí', NULL, 165.00, 'Pagada', 'Efectivo'),
('F-000120', '2024-07-25', 'Jorge Luis Fernández Ortega', '989012345', 'Duke', 'Rottweiler', NULL, 410.00, 'Pendiente', 'Yape'),
('F-000119', '2024-07-26', 'Elena María Gutiérrez Vega', '991234567', 'Bella', 'Beagle', NULL, 125.00, 'Pagada', 'Tarjeta'),
('F-000118', '2024-07-29', 'Miguel Ángel Ruíz Soto', '902345678', 'Simba', 'Sphynx', NULL, 195.00, 'Pagada', 'Plin'),
('F-000117', '2024-07-30', 'Sofía Alejandra Morales Cordero', '913456789', 'Charlie', 'Border Collie', NULL, 230.00, 'Pagada', 'Efectivo'),
('F-000116', '2024-07-31', 'Diego Armando Navarro Peralta', '924567890', 'Lola', 'Chihuahua', NULL, 85.00, 'Pagada', 'Transferencia'),
('F-000115', '2024-08-01', 'Valentina Castro Benavides', '935678901', 'Oliver', 'Ragdoll', NULL, 155.00, 'Pagada', 'Tarjeta'),
('F-000114', '2024-08-02', 'Andrés Felipe Mendoza Quispe', '946789012', 'Zeus', 'Doberman', NULL, 320.00, 'Pendiente', 'Yape');

-- ----------------------------------------------------------------------------
-- Auditoría (15 registros de acciones del sistema)
-- ----------------------------------------------------------------------------
INSERT INTO auditoria (fecha_hora, usuario, accion, modulo, descripcion, ip_dispositivo, navegador, severidad, estado) VALUES
('2024-07-10 08:30:00', 'Alex Murga', 'LOGIN', 'Autenticación', 'Usuario Alex Murga inició sesión', '192.168.1.100', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-10 09:15:00', 'Dr. Carlos Ramírez Torres', 'CREAR', 'Citas', 'Cita creada para cliente Carlos Ramírez', '192.168.1.101', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-10 10:00:00', 'Dra. María Fernanda López García', 'ACTUALIZAR', 'Vacunas', 'Vacuna aplicada a mascota Luna', '192.168.1.102', 'Firefox 115.0', 'INFO', 'Exitoso'),
('2024-07-10 11:30:00', 'Ana Gabriela Silva Paredes', 'CREAR', 'Facturas', 'Factura F-000128 generada', '192.168.1.103', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-10 14:00:00', 'Alex Murga', 'ELIMINAR', 'Clientes', 'Intento de eliminar cliente con mascotas', '192.168.1.100', 'Chrome 120.0', 'WARNING', 'Fallido'),
('2024-07-10 15:45:00', 'Dr. Juan Diego Torres Rivas', 'CREAR', 'Mascotas', 'Nueva mascota registrada: Buddy', '192.168.1.101', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-11 08:00:00', 'Luis Enrique Herrera Campos', 'ACTUALIZAR', 'Productos', 'Stock actualizado para MED-001', '192.168.1.104', 'Edge 120.0', 'INFO', 'Exitoso'),
('2024-07-11 09:30:00', 'Dra. María Fernanda López García', 'CREAR', 'Vacunas', 'Vacuna Triple Felina aplicada', '192.168.1.102', 'Firefox 115.0', 'INFO', 'Exitoso'),
('2024-07-11 11:00:00', 'Alex Murga', 'LOGIN', 'Autenticación', 'Usuario Alex Murga inició sesión', '192.168.1.100', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-11 13:15:00', 'Dr. Carlos Ramírez Torres', 'ACTUALIZAR', 'Citas', 'Cita reprogramada para cliente Juan Torres', '192.168.1.101', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-12 08:45:00', 'Ana Gabriela Silva Paredes', 'CREAR', 'Facturas', 'Factura F-000127 generada', '192.168.1.103', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-12 10:30:00', 'Alex Murga', 'ELIMINAR', 'Usuarios', 'Usuario desactivado: recepcionista2', '192.168.1.100', 'Chrome 120.0', 'WARNING', 'Exitoso'),
('2024-07-12 14:20:00', 'Dr. Juan Diego Torres Rivas', 'CREAR', 'Citas', 'Cita de cirugía programada', '192.168.1.101', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-13 09:00:00', 'Luis Enrique Herrera Campos', 'ACTUALIZAR', 'Clientes', 'Información de contacto actualizada', '192.168.1.104', 'Edge 120.0', 'INFO', 'Exitoso'),
('2024-07-13 11:45:00', 'Dra. María Fernanda López García', 'CREAR', 'Vacunas', 'Vacuna antirrábica aplicada', '192.168.1.102', 'Firefox 115.0', 'INFO', 'Exitoso');

-- ============================================================================
-- VERIFICACIÓN DE INTEGRIDAD
-- ============================================================================
-- Consultas para verificar que todo se creó correctamente:

-- Verificar usuarios
-- SELECT * FROM usuarios;

-- Verificar clientes
-- SELECT * FROM clientes;

-- Verificar mascotas con sus clientes
-- SELECT m.nombre AS mascota, m.especie, c.nombre AS cliente 
-- FROM mascotas m 
-- JOIN clientes c ON m.cliente_id = c.id;

-- Verificar productos
-- SELECT * FROM productos;

-- Verificar vacunas con mascotas
-- SELECT v.vacuna, v.fecha_aplicacion, m.nombre AS mascota 
-- FROM vacunas v 
-- JOIN mascotas m ON v.mascota_id = m.id;

-- Verificar citas
-- SELECT c.fecha, c.hora, cli.nombre AS cliente, m.nombre AS mascota 
-- FROM citas c 
-- JOIN clientes cli ON c.cliente_id = cli.id 
-- JOIN mascotas m ON c.mascota_id = m.id;

-- Verificar facturas
-- SELECT * FROM facturas;

-- Verificar auditoría
-- SELECT * FROM auditoria ORDER BY fecha_hora DESC;

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================
-- El sistema VetControl 360 está listo para usar con:
-- - 8 tablas creadas con todas las relaciones
-- - 6 usuarios (incluyendo Alex Murga como administrador)
-- - 15 clientes con información completa
-- - 15 mascotas (una por cliente)
-- - 15 productos en inventario
-- - 15 vacunas aplicadas
-- - 15 citas programadas
-- - 15 facturas generadas
-- - 15 registros de auditoría
-- ============================================================================
