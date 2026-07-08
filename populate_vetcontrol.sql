-- Script SQL para poblar VetControl 360 con datos de ejemplo
-- Base de datos PostgreSQL (Neon)
-- Genera 16 registros por cada módulo con claves foráneas coherentes

-- ============================================================
-- 1. TABLA USUARIOS (16 usuarios)
-- Roles: 1 Admin, 5 Veterinarios, 5 Asistentes, 5 Recepcionistas
-- ============================================================

-- Limpiar datos existentes para evitar duplicados
DELETE FROM auditoria;
DELETE FROM facturas;
DELETE FROM citas;
DELETE FROM vacunas;
DELETE FROM productos;
DELETE FROM mascotas;
DELETE FROM clientes;
DELETE FROM proveedores;
DELETE FROM empresa;
DELETE FROM usuarios;

-- Insertar usuarios
-- Nota: Los hashes son BCrypt. Para generar nuevos hashes usar: https://bcrypt-generator.com/
-- Contraseña "AlexMurga.20*" -> $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- Contraseña "password123" -> $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO usuarios (usuario, password_hash, nombre_completo, activo, id_rol) VALUES
('alexmurga', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alex Murga', true, 1),
('veterinario1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Carlos Rodríguez', true, 2),
('veterinario2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dra. María González', true, 2),
('veterinario3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Juan Pérez', true, 2),
('veterinario4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dra. Ana Martínez', true, 2),
('veterinario5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Luis Sánchez', true, 2),
('asistente1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Laura García', true, 3),
('asistente2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pedro López', true, 3),
('asistente3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sofía Fernández', true, 3),
('asistente4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Diego Torres', true, 3),
('asistente5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Carmen Ruiz', true, 3),
('recepcionista1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mónica Díaz', true, 4),
('recepcionista2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Javier Castro', true, 4),
('recepcionista3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Patricia Jiménez', true, 4),
('recepcionista4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Roberto Moreno', true, 4),
('recepcionista5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Isabel Navarro', true, 4);

-- ============================================================
-- 2. TABLA CLIENTES (16 clientes)
-- ============================================================

INSERT INTO clientes (nombre, apellidos, dni, telefono, whatsapp, email, fecha_nacimiento, edad, genero, estado_civil, direccion, departamento, provincia, distrito, referencia, notas, estado) VALUES
('Carlos', 'Mendoza Quispe', '12345678', '987654321', '987654321', 'carlos.mendoza@email.com', '1985-03-15', 39, 'Masculino', 'Casado', 'Av. Brasil 1234, Depto 402', 'Lima', 'Lima', 'San Miguel', 'Cerca al parque', 'Cliente frecuente', 'Activo'),
('María', 'Flores Sánchez', '23456789', '987654322', '987654322', 'maria.flores@email.com', '1990-07-22', 34, 'Femenino', 'Soltera', 'Jr. Los Olivos 456', 'Lima', 'Lima', 'Los Olivos', 'Frente al mercado', NULL, 'Activo'),
('Juan', 'Ramírez Torres', '34567890', '987654323', '987654323', 'juan.ramirez@email.com', '1982-11-08', 42, 'Masculino', 'Casado', 'Av. Principal 789', 'Lima', 'Lima', 'Miraflores', 'Edificio Central', 'Alergia a penicilina', 'Activo'),
('Ana', 'Gutiérrez López', '45678901', '987654324', '987654324', 'ana.gutierrez@email.com', '1988-05-30', 36, 'Femenino', 'Casada', 'Calle 10 101', 'Lima', 'Lima', 'Surco', 'Urb. San Borja', NULL, 'Activo'),
('Luis', 'Castro Mendez', '56789012', '987654325', '987654325', 'luis.castro@email.com', '1975-09-12', 49, 'Masculino', 'Divorciado', 'Av. La Marina 234', 'Lima', 'Lima', 'San Borja', 'Cerca al colegio', 'Prefiere visitas en casa', 'Activo'),
('Sofía', 'Díaz Ramos', '67890123', '987654326', '987654326', 'sofia.diaz@email.com', '1995-02-18', 29, 'Femenino', 'Soltera', 'Jr. Amazonas 567', 'Lima', 'Lima', 'Pueblo Libre', 'Frente a la plaza', NULL, 'Activo'),
('Pedro', 'Ortiz Vega', '78901234', '987654327', '987654327', 'pedro.ortiz@email.com', '1980-06-25', 44, 'Masculino', 'Casado', 'Av. Benavides 890', 'Lima', 'Lima', 'Surquillo', 'Urb. Monterrico', NULL, 'Activo'),
('Carmen', 'Rojas Silva', '89012345', '987654328', '987654328', 'carmen.rojas@email.com', '1983-12-03', 41, 'Femenino', 'Casada', 'Calle Las Flores 321', 'Lima', 'Lima', 'La Molina', 'Cerca al club', 'Cliente VIP', 'Activo'),
('Diego', 'Morales Cordero', '90123456', '987654329', '987654329', 'diego.morales@email.com', '1992-04-10', 32, 'Masculino', 'Soltero', 'Av. El Sol 654', 'Lima', 'Lima', 'Jesús María', 'Frente al hospital', NULL, 'Activo'),
('Patricia', 'Navarro Fierro', '01234567', '987654330', '987654330', 'patricia.navarro@email.com', '1987-08-14', 37, 'Femenino', 'Casada', 'Jr. 28 de Julio 987', 'Lima', 'Lima', 'Magdalena', 'Cerca al mar', NULL, 'Activo'),
('Roberto', 'Herrera Castillo', '12340987', '987654331', '987654331', 'roberto.herrera@email.com', '1978-01-20', 46, 'Masculino', 'Casado', 'Av. Brasil 135', 'Lima', 'Lima', 'San Miguel', 'Edificio A', NULL, 'Activo'),
('Isabel', 'Soto Benavides', '23410987', '987654332', '987654332', 'isabel.soto@email.com', '1991-10-05', 33, 'Femenino', 'Soltera', 'Calle 7 246', 'Lima', 'Lima', 'Miraflores', 'Kennedy', NULL, 'Activo'),
('Miguel', 'Álvarez Ríos', '34510987', '987654333', '987654333', 'miguel.alvarez@email.com', '1984-07-28', 40, 'Masculino', 'Casado', 'Av. Angamos 579', 'Lima', 'Lima', 'San Isidro', 'Cerca al banco', NULL, 'Activo'),
('Elena', 'Romero Campos', '45610987', '987654334', '987654334', 'elena.romero@email.com', '1989-03-17', 35, 'Femenino', 'Casada', 'Jr. Prado 802', 'Lima', 'Lima', 'Lince', 'Frente al estadio', NULL, 'Activo'),
('Fernando', 'Vargas Medina', '56710987', '987654335', '987654335', 'fernando.vargas@email.com', '1976-11-22', 48, 'Masculino', 'Divorciado', 'Av. Tacna 913', 'Lima', 'Lima', 'Cercado', 'Centro histórico', NULL, 'Activo'),
('Lucía', 'Mendoza Ortiz', '67810987', '987654336', '987654336', 'lucia.mendoza@email.com', '1994-06-09', 30, 'Femenino', 'Soltera', 'Calle 4 135', 'Lima', 'Lima', 'Barranco', 'Cerca al puente', NULL, 'Activo');

-- ============================================================
-- 3. TABLA MASCOTAS (16 mascotas)
-- Cada mascota asociada a un cliente
-- ============================================================

INSERT INTO mascotas (nombre, especie, raza, sexo, fecha_nacimiento, color, microchip, peso, tipo_pelaje, tamano, esterilizado, notas, estado, cliente_id) VALUES
('Max', 'Perro', 'Golden Retriever', 'Macho', '2020-05-15', 'Dorado', 'MIC001', 32.5, 'Largo', 'Grande', true, 'Muy activo', 'Activo', 1),
('Luna', 'Gato', 'Siamés', 'Hembra', '2021-08-20', 'Crema', 'MIC002', 4.2, 'Corto', 'Pequeño', true, NULL, 'Activo', 2),
('Rocky', 'Perro', 'Pastor Alemán', 'Macho', '2019-03-10', 'Negro y café', 'MIC003', 40.0, 'Largo', 'Grande', true, 'Guardián', 'Activo', 3),
('Simba', 'Gato', 'Persa', 'Macho', '2022-01-25', 'Blanco', 'MIC004', 5.8, 'Largo', 'Mediano', false, NULL, 'Activo', 4),
('Coco', 'Perro', 'Bulldog Francés', 'Hembra', '2021-11-05', 'Atigrado', 'MIC005', 12.3, 'Corto', 'Pequeño', true, NULL, 'Activo', 5),
('Nala', 'Gato', 'Maine Coon', 'Hembra', '2020-07-30', 'Atigrado', 'MIC006', 7.5, 'Largo', 'Grande', true, NULL, 'Activo', 6),
('Thor', 'Perro', 'Husky Siberiano', 'Macho', '2019-09-18', 'Gris y blanco', 'MIC007', 28.0, 'Doble', 'Grande', false, 'Necesita ejercicio', 'Activo', 7),
('Mia', 'Gato', 'Ragdoll', 'Hembra', '2022-04-12', 'Blanco y crema', 'MIC008', 6.2, 'Largo', 'Grande', true, NULL, 'Activo', 8),
('Bruno', 'Perro', 'Labrador', 'Macho', '2020-12-08', 'Negro', 'MIC009', 35.0, 'Corto', 'Grande', true, NULL, 'Activo', 9),
('Bella', 'Gato', 'Bengalí', 'Hembra', '2021-06-22', 'Atigrado dorado', 'MIC010', 5.0, 'Corto', 'Mediano', true, NULL, 'Activo', 10),
('Zeus', 'Perro', 'Doberman', 'Macho', '2018-02-14', 'Negro y café', 'MIC011', 42.0, 'Corto', 'Grande', true, 'Entrenado', 'Activo', 11),
('Lola', 'Gato', 'Sphynx', 'Hembra', '2022-09-05', 'Rosa', 'MIC012', 3.8, 'Sin pelo', 'Pequeño', true, NULL, 'Activo', 12),
('Rex', 'Perro', 'Boxer', 'Macho', '2019-07-28', 'Café', 'MIC013', 30.0, 'Corto', 'Grande', true, NULL, 'Activo', 13),
('Cleo', 'Gato', 'Azul Ruso', 'Hembra', '2021-03-19', 'Gris azulado', 'MIC014', 4.5, 'Corto', 'Mediano', true, NULL, 'Activo', 14),
('Duke', 'Perro', 'Rottweiler', 'Macho', '2018-10-30', 'Negro y café', 'MIC015', 48.0, 'Corto', 'Grande', true, 'Protector', 'Activo', 15),
('Oreo', 'Conejo', 'Cabeza de León', 'Macho', '2023-01-10', 'Negro y blanco', 'MIC016', 2.5, 'Largo', 'Mediano', false, NULL, 'Activo', 16);

-- ============================================================
-- 4. TABLA PROVEEDORES (16 proveedores)
-- ============================================================

INSERT INTO proveedores (codigo, nombre, ruc, rubro, telefono, email, estado, direccion) VALUES
('PROV-0001', 'VetPharma Perú', '20123456789', 'Medicamentos', '511-555-0101', 'contacto@vetpharma.pe', 'Activo', 'Av. Industrial 1234, Lima'),
('PROV-0002', 'Royal Canin Perú', '20134567890', 'Alimentos', '511-555-0102', 'info@royalcanin.pe', 'Activo', 'Jr. Comercio 456, Lima'),
('PROV-0003', 'Zoetis Perú', '20145678901', 'Medicamentos', '511-555-0103', 'peru@zoetis.com', 'Activo', 'Av. Panamericana 789, Lima'),
('PROV-0004', 'Bayer Animal Health', '20156789012', 'Medicamentos', '511-555-0104', 'bayer@animalhealth.com', 'Activo', 'Calle Los Andes 234, Lima'),
('PROV-0005', 'Purina Pro Plan', '20167890123', 'Alimentos', '511-555-0105', 'purina@nestle.com', 'Activo', 'Av. Brasil 567, Lima'),
('PROV-0006', 'Merial Perú', '20178901234', 'Vacunas', '511-555-0106', 'merial@boehringer.com', 'Activo', 'Jr. 28 de Julio 890, Lima'),
('PROV-0007', 'Hill''s Pet Nutrition', '20189012345', 'Alimentos', '511-555-0107', 'hills@colgate.com', 'Activo', 'Av. Benavides 135, Lima'),
('PROV-0008', 'Elanco Animal Health', '20190123456', 'Medicamentos', '511-555-0108', 'elanco@peru.com', 'Activo', 'Calle 10 246, Lima'),
('PROV-0009', 'Virbac Perú', '20201234567', 'Medicamentos', '511-555-0109', 'virbac@peru.com', 'Activo', 'Av. La Marina 579, Lima'),
('PROV-0010', 'PetCare Solutions', '20212345678', 'Accesorios', '511-555-0110', 'petcare@solutions.com', 'Activo', 'Jr. Amazonas 802, Lima'),
('PROV-0011', 'NutriDog Perú', '20223456789', 'Alimentos', '511-555-0111', 'nutridog@peru.com', 'Activo', 'Av. El Sol 913, Lima'),
('PROV-0012', 'VetLab Diagnostics', '20234567890', 'Insumos', '511-555-0112', 'vetlab@diagnostics.com', 'Activo', 'Calle Las Flores 135, Lima'),
('PROV-0013', 'Animal Health International', '20245678901', 'Medicamentos', '511-555-0113', 'ahi@peru.com', 'Activo', 'Av. Principal 321, Lima'),
('PROV-0014', 'Quality Pet Products', '20256789012', 'Accesorios', '511-555-0114', 'quality@pet.com', 'Activo', 'Jr. Los Olivos 456, Lima'),
('PROV-0015', 'BioVet Laboratories', '20267890123', 'Vacunas', '511-555-0115', 'biovet@labs.com', 'Activo', 'Av. Tacna 789, Lima'),
('PROV-0016', 'MediVet Perú', '20278901234', 'Medicamentos', '511-555-0116', 'medivet@peru.com', 'Activo', 'Calle 7 102, Lima');

-- ============================================================
-- 5. TABLA PRODUCTOS (16 productos)
-- ============================================================

INSERT INTO productos (nombre, sku, categoria, tipo, presentacion, unidad, descripcion, proveedor, precio_compra, precio_venta, margen, stock_actual, stock_minimo, stock_maximo, ubicacion, fecha_vencimiento, lote, fabricante, almacenamiento, registro_sanitario, notas) VALUES
('Doxivet 100mg', 'MED-001', 'Medicamentos', 'Medicamento', 'Tableta', 'unidad', 'Antibiótico de amplio espectro', 'VetPharma Perú', 15.50, 25.00, 61.29, 50, 10, 100, 'Farmacia', '2026-12-31', 'L-2024-001', 'VetPharma', 'Temperatura ambiente', 'RS-12345', NULL),
('Amoxicilina 500mg', 'MED-002', 'Medicamentos', 'Medicamento', 'Cápsula', 'unidad', 'Antibiótico penicilínico', 'Zoetis Perú', 8.00, 15.00, 87.50, 80, 20, 150, 'Farmacia', '2025-08-15', 'L-2024-002', 'Zoetis', 'Temperatura ambiente', 'RS-12346', NULL),
('Ivermectina 1%', 'MED-003', 'Medicamentos', 'Medicamento', 'Solución', 'ml', 'Antiparasitario interno y externo', 'Bayer Animal Health', 25.00, 45.00, 80.00, 30, 5, 60, 'Farmacia', '2027-01-20', 'L-2024-003', 'Bayer', 'Protegido de la luz', 'RS-12347', NULL),
('Royal Canin Adult', 'ALI-001', 'Alimentos', 'Alimento', 'Bolsa', 'kg', 'Alimento premium para perros adultos', 'Royal Canin Perú', 45.00, 75.00, 66.67, 25, 10, 50, 'Almacén A', '2025-06-30', 'L-2024-004', 'Royal Canin', 'Lugar fresco y seco', 'RS-12348', NULL),
('Purina Pro Plan', 'ALI-002', 'Alimentos', 'Alimento', 'Bolsa', 'kg', 'Nutrición balanceada para perros', 'Purina Pro Plan', 35.00, 60.00, 71.43, 40, 15, 80, 'Almacén B', '2025-09-15', 'L-2024-005', 'Purina', 'Lugar fresco y seco', 'RS-12349', NULL),
('Hill''s Science Diet', 'ALI-003', 'Alimentos', 'Alimento', 'Lata', 'unidad', 'Alimento húmedo para gatos', 'Hill''s Pet Nutrition', 12.00, 22.00, 83.33, 60, 20, 100, 'Almacén A', '2025-04-20', 'L-2024-006', 'Hill''s', 'Lugar fresco y seco', 'RS-12350', NULL),
('Vacuna Rabia', 'VAC-001', 'Vacunas', 'Insumo', 'Frasco', 'unidad', 'Vacuna antirrábica', 'Merial Perú', 18.00, 35.00, 94.44, 20, 5, 40, 'Farmacia', '2025-03-10', 'L-2024-007', 'Merial', 'Refrigerado (2-8°C)', 'RS-12351', NULL),
('Vacuna Sextuple', 'VAC-002', 'Vacunas', 'Insumo', 'Frasco', 'unidad', 'Vacuna múltiple canina', 'BioVet Laboratories', 22.00, 40.00, 81.82, 15, 5, 30, 'Farmacia', '2025-05-25', 'L-2024-008', 'BioVet', 'Refrigerado (2-8°C)', 'RS-12352', NULL),
('Correa Nylon', 'ACC-001', 'Accesorios', 'Accesorio', 'Unidad', 'unidad', 'Correa resistente para perros medianos', 'PetCare Solutions', 20.00, 35.00, 75.00, 35, 10, 70, 'Vitrina', NULL, NULL, 'PetCare', 'Temperatura ambiente', NULL, NULL),
('Juguete Peluche', 'ACC-002', 'Accesorios', 'Accesorio', 'Unidad', 'unidad', 'Peluche interactivo para mascotas', 'Quality Pet Products', 8.00, 18.00, 125.00, 50, 15, 100, 'Vitrina', NULL, NULL, 'Quality Pet', 'Temperatura ambiente', NULL, NULL),
('Cama Ortopédica', 'ACC-003', 'Accesorios', 'Accesorio', 'Unidad', 'unidad', 'Cama cómoda para perros grandes', 'PetCare Solutions', 80.00, 150.00, 87.50, 10, 3, 20, 'Almacén B', NULL, NULL, 'PetCare', 'Temperatura ambiente', NULL, NULL),
('Champú Antipulgas', 'HIG-001', 'Higiene', 'Insumo', 'Botella', 'ml', 'Champú para control de pulgas', 'Elanco Animal Health', 15.00, 28.00, 86.67, 45, 15, 90, 'Farmacia', '2026-08-01', 'L-2024-009', 'Elanco', 'Temperatura ambiente', 'RS-12353', NULL),
('Cepillo Doble', 'HIG-002', 'Higiene', 'Accesorio', 'Unidad', 'unidad', 'Cepillo para pelaje de mascotas', 'Quality Pet Products', 12.00, 22.00, 83.33, 40, 10, 80, 'Vitrina', NULL, NULL, 'Quality Pet', 'Temperatura ambiente', NULL, NULL),
('Collar Antipulgas', 'HIG-003', 'Higiene', 'Insumo', 'Unidad', 'unidad', 'Collar con protección de 3 meses', 'Virbac Perú', 25.00, 50.00, 100.00, 25, 8, 50, 'Farmacia', '2026-11-15', 'L-2024-010', 'Virbac', 'Temperatura ambiente', 'RS-12354', NULL),
('Suero Fisiológico', 'INS-001', 'Medicamentos', 'Insumo', 'Frasco', 'ml', 'Solución para hidratación', 'VetLab Diagnostics', 5.00, 12.00, 140.00, 100, 30, 200, 'Farmacia', '2026-03-30', 'L-2024-011', 'VetLab', 'Temperatura ambiente', 'RS-12355', NULL),
('Jeringas 5ml', 'INS-002', 'Medicamentos', 'Insumo', 'Caja', 'unidad', 'Jeringas desechables', 'Animal Health International', 8.00, 15.00, 87.50, 70, 20, 140, 'Farmacia', NULL, NULL, 'AHI', 'Temperatura ambiente', NULL, NULL);

-- ============================================================
-- 6. TABLA VACUNAS (16 vacunas)
-- Asociadas a las mascotas
-- ============================================================

INSERT INTO vacunas (mascota_id, vacuna, lote, laboratorio, fecha_aplicacion, proxima_dosis, aplicada_por, via_aplicacion, sitio_aplicacion, estado, observaciones, tipo_recordatorio, dias_antes, notas_adicionales) VALUES
(1, 'Vacuna Sextuple', 'L-2024-007', 'BioVet Laboratories', '2024-01-15', '2025-01-15', 'Dr. Carlos Rodríguez', 'Subcutánea', 'Cuello', 'Aplicada', 'Reacción leve', 'Email', 7, NULL),
(1, 'Antirrábica', 'L-2024-008', 'Merial Perú', '2024-02-20', '2025-02-20', 'Dr. Carlos Rodríguez', 'Subcutánea', 'Muslo', 'Aplicada', NULL, 'Email', 7, NULL),
(2, 'Triple Felina', 'L-2024-009', 'Zoetis Perú', '2024-03-10', '2025-03-10', 'Dra. María González', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(3, 'Vacuna Sextuple', 'L-2024-010', 'BioVet Laboratories', '2024-01-20', '2025-01-20', 'Dr. Juan Pérez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(3, 'Antirrábica', 'L-2024-011', 'Merial Perú', '2024-02-25', '2025-02-25', 'Dr. Juan Pérez', 'Subcutánea', 'Muslo', 'Aplicada', NULL, 'Email', 7, NULL),
(4, 'Triple Felina', 'L-2024-012', 'Zoetis Perú', '2024-04-05', '2025-04-05', 'Dra. Ana Martínez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(5, 'Vacuna Sextuple', 'L-2024-013', 'BioVet Laboratories', '2024-02-15', '2025-02-15', 'Dr. Luis Sánchez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(6, 'Triple Felina', 'L-2024-014', 'Zoetis Perú', '2024-03-20', '2025-03-20', 'Dra. María González', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(7, 'Vacuna Sextuple', 'L-2024-015', 'BioVet Laboratories', '2024-01-25', '2025-01-25', 'Dr. Carlos Rodríguez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(8, 'Triple Felina', 'L-2024-016', 'Zoetis Perú', '2024-04-10', '2025-04-10', 'Dra. Ana Martínez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(9, 'Vacuna Sextuple', 'L-2024-017', 'BioVet Laboratories', '2024-02-28', '2025-02-28', 'Dr. Juan Pérez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(10, 'Triple Felina', 'L-2024-018', 'Zoetis Perú', '2024-03-25', '2025-03-25', 'Dra. María González', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(11, 'Antirrábica', 'L-2024-019', 'Merial Perú', '2024-03-05', '2025-03-05', 'Dr. Luis Sánchez', 'Subcutánea', 'Muslo', 'Aplicada', NULL, 'Email', 7, NULL),
(12, 'Triple Felina', 'L-2024-020', 'Zoetis Perú', '2024-04-15', '2025-04-15', 'Dra. Ana Martínez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(13, 'Vacuna Sextuple', 'L-2024-021', 'BioVet Laboratories', '2024-01-30', '2025-01-30', 'Dr. Carlos Rodríguez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(14, 'Triple Felina', 'L-2024-022', 'Zoetis Perú', '2024-03-15', '2025-03-15', 'Dra. María González', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL);

-- ============================================================
-- 7. TABLA CITAS (16 citas)
-- Asociadas a clientes y mascotas
-- ============================================================

-- Fechas: algunas en el pasado, algunas en el futuro
INSERT INTO citas (cliente_id, mascota_id, tipo_cita, fecha, hora, motivo, duracion, veterinario, consultorio, notas, recordatorio, whatsapp, email) VALUES
(1, 1, 'Consulta General', '2024-06-15', '09:00:00', 'Revisión general de rutina', 30, 'Dr. Carlos Rodríguez', 'Consultorio 1', NULL, true, true, true),
(2, 2, 'Vacunación', '2024-06-18', '10:30:00', 'Aplicación de vacuna triple', 15, 'Dra. María González', 'Consultorio 2', NULL, true, true, true),
(3, 3, 'Control / Revisión', '2024-06-20', '14:00:00', 'Control post-operatorio', 20, 'Dr. Juan Pérez', 'Consultorio 1', 'Revisar suturas', true, true, false),
(4, 4, 'Consulta General', '2024-06-22', '11:00:00', 'Chequeo de rutina', 30, 'Dra. Ana Martínez', 'Consultorio 3', NULL, true, false, true),
(5, 5, 'Desparasitación', '2024-06-25', '15:30:00', 'Desparasitación interna', 15, 'Dr. Luis Sánchez', 'Consultorio 2', NULL, true, true, true),
(6, 6, 'Vacunación', '2024-06-28', '09:30:00', 'Vacuna antirrábica', 20, 'Dra. María González', 'Consultorio 1', NULL, true, true, true),
(7, 7, 'Consulta General', '2024-07-02', '10:00:00', 'Revisión dermatológica', 30, 'Dr. Carlos Rodríguez', 'Consultorio 3', 'Problemas de piel', true, true, false),
(8, 8, 'Control / Revisión', '2024-07-05', '08:30:00', 'Control de peso', 15, 'Dra. Ana Martínez', 'Consultorio 2', NULL, true, false, true),
(9, 9, 'Consulta General', '2024-07-08', '11:30:00', 'Revisión de articulaciones', 30, 'Dr. Juan Pérez', 'Consultorio 1', 'Cojera leve', true, true, true),
(10, 10, 'Vacunación', '2024-07-10', '14:30:00', 'Vacuna anual', 15, 'Dra. María González', 'Consultorio 3', NULL, true, true, true),
(11, 11, 'Desparasitación', '2024-07-12', '09:00:00', 'Desparasitación externa', 20, 'Dr. Luis Sánchez', 'Consultorio 2', NULL, true, true, false),
(12, 12, 'Consulta General', '2024-07-15', '10:30:00', 'Chequeo respiratorio', 30, 'Dr. Carlos Rodríguez', 'Consultorio 1', NULL, true, false, true),
(13, 13, 'Control / Revisión', '2024-07-18', '15:00:00', 'Control nutricional', 20, 'Dra. Ana Martínez', 'Consultorio 3', 'Dieta especial', true, true, true),
(14, 14, 'Vacunación', '2024-07-20', '08:00:00', 'Vacuna triple felina', 15, 'Dra. María González', 'Consultorio 2', NULL, true, true, true),
(15, 15, 'Consulta General', '2024-07-22', '11:00:00', 'Revisión general', 30, 'Dr. Juan Pérez', 'Consultorio 1', NULL, true, true, false),
(16, 16, 'Control / Revisión', '2024-07-25', '14:30:00', 'Control de crecimiento', 15, 'Dr. Luis Sánchez', 'Consultorio 3', NULL, true, false, true);

-- ============================================================
-- 8. TABLA FACTURAS (16 facturas)
-- ============================================================

INSERT INTO facturas (numero, fecha, cliente, telefono, mascota, raza, foto, total, estado, metodo_pago) VALUES
('F-000001', '2024-06-15', 'Carlos Mendoza Quispe', '987654321', 'Max', 'Golden Retriever', NULL, 150.00, 'Pagada', 'Tarjeta de Crédito'),
('F-000002', '2024-06-18', 'María Flores Sánchez', '987654322', 'Luna', 'Siamés', NULL, 85.00, 'Pagada', 'Efectivo'),
('F-000003', '2024-06-20', 'Juan Ramírez Torres', '987654323', 'Rocky', 'Pastor Alemán', NULL, 200.00, 'Pagada', 'Yape'),
('F-000004', '2024-06-22', 'Ana Gutiérrez López', '987654324', 'Simba', 'Persa', NULL, 120.00, 'Pendiente', 'Transferencia'),
('F-000005', '2024-06-25', 'Luis Castro Mendez', '987654325', 'Coco', 'Bulldog Francés', NULL, 95.00, 'Pagada', 'Efectivo'),
('F-000006', '2024-06-28', 'Sofía Díaz Ramos', '987654326', 'Nala', 'Maine Coon', NULL, 180.00, 'Pagada', 'Tarjeta de Débito'),
('F-000007', '2024-07-02', 'Pedro Ortiz Vega', '987654327', 'Thor', 'Husky Siberiano', NULL, 250.00, 'Pagada', 'Yape'),
('F-000008', '2024-07-05', 'Carmen Rojas Silva', '987654328', 'Mia', 'Ragdoll', NULL, 110.00, 'Pendiente', 'Efectivo'),
('F-000009', '2024-07-08', 'Diego Morales Cordero', '987654329', 'Bruno', 'Labrador', NULL, 175.00, 'Pagada', 'Tarjeta de Crédito'),
('F-000010', '2024-07-10', 'Patricia Navarro Fierro', '987654330', 'Bella', 'Bengalí', NULL, 135.00, 'Pagada', 'Transferencia'),
('F-000011', '2024-07-12', 'Roberto Herrera Castillo', '987654331', 'Zeus', 'Doberman', NULL, 220.00, 'Anulada', 'Efectivo'),
('F-000012', '2024-07-15', 'Isabel Soto Benavides', '987654332', 'Lola', 'Sphynx', NULL, 90.00, 'Pagada', 'Yape'),
('F-000013', '2024-07-18', 'Miguel Álvarez Ríos', '987654333', 'Rex', 'Boxer', NULL, 160.00, 'Pagada', 'Tarjeta de Débito'),
('F-000014', '2024-07-20', 'Elena Romero Campos', '987654334', 'Cleo', 'Azul Ruso', NULL, 105.00, 'Pendiente', 'Efectivo'),
('F-000015', '2024-07-22', 'Fernando Vargas Medina', '987654335', 'Duke', 'Rottweiler', NULL, 280.00, 'Pagada', 'Transferencia'),
('F-000016', '2024-07-25', 'Lucía Mendoza Ortiz', '987654336', 'Oreo', 'Cabeza de León', NULL, 75.00, 'Pagada', 'Yape');

-- ============================================================
-- 9. TABLA AUDITORÍA (16 registros)
-- ============================================================

INSERT INTO auditoria (fecha_hora, usuario, accion, modulo, descripcion, ip_dispositivo, navegador, severidad, estado) VALUES
('2024-06-15 08:55:00', 'alexmurga', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Alex Murga inició sesión', '192.168.1.100', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-06-15 09:05:00', 'alexmurga', 'CREAR', 'CLIENTES', 'Creó cliente Carlos Mendoza Quispe', '192.168.1.100', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-06-15 09:10:00', 'alexmurga', 'CREAR', 'MASCOTAS', 'Creó mascota Max', '192.168.1.100', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-06-18 10:25:00', 'veterinario1', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Dr. Carlos Rodríguez inició sesión', '192.168.1.101', 'Firefox 115.0', 'INFO', 'EXITOSO'),
('2024-06-18 10:35:00', 'veterinario1', 'CREAR', 'CITAS', 'Programó cita para Luna', '192.168.1.101', 'Firefox 115.0', 'INFO', 'EXITOSO'),
('2024-06-20 14:05:00', 'veterinario2', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Dra. María González inició sesión', '192.168.1.102', 'Safari 17.0', 'INFO', 'EXITOSO'),
('2024-06-20 14:15:00', 'veterinario2', 'CREAR', 'VACUNAS', 'Registró vacuna para Rocky', '192.168.1.102', 'Safari 17.0', 'INFO', 'EXITOSO'),
('2024-06-22 11:00:00', 'asistente1', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Laura García inició sesión', '192.168.1.103', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-06-22 11:10:00', 'asistente1', 'CREAR', 'FACTURAS', 'Generó factura F-000004', '192.168.1.103', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-06-25 15:25:00', 'veterinario3', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Dr. Juan Pérez inició sesión', '192.168.1.104', 'Edge 120.0', 'INFO', 'EXITOSO'),
('2024-06-25 15:35:00', 'veterinario3', 'EDITAR', 'MASCOTAS', 'Actualizó datos de Coco', '192.168.1.104', 'Edge 120.0', 'INFO', 'EXITOSO'),
('2024-06-28 09:30:00', 'recepcionista1', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Mónica Díaz inició sesión', '192.168.1.105', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-06-28 09:40:00', 'recepcionista1', 'CREAR', 'CITAS', 'Programó cita para Nala', '192.168.1.105', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-07-02 10:00:00', 'alexmurga', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Alex Murga inició sesión', '192.168.1.100', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-07-02 10:15:00', 'alexmurga', 'EDITAR', 'CLIENTES', 'Actualizó cliente Pedro Ortiz Vega', '192.168.1.100', 'Chrome 120.0', 'INFO', 'EXITOSO'),
('2024-07-05 08:30:00', 'asistente2', 'LOGIN', 'AUTENTICACIÓN', 'Usuario Pedro López inició sesión', '192.168.1.106', 'Firefox 115.0', 'INFO', 'EXITOSO');

-- ============================================================
-- 10. TABLA EMPRESA (1 registro)
-- ============================================================

INSERT INTO empresa (ruc, razon_social, nombre_comercial, direccion, email, telefono, departamento, provincia, distrito, ubigeo, logo_url, certificado_path, certificado_password, estado) VALUES
('20600085510', 'VetCare Clínica Veterinarias SAC', 'VetCare Clínica Veterinaria', 'Av. Principal 1234, San Miguel, Lima', 'contacto@vetcare.pe', '511-555-9999', 'Lima', 'Lima', 'San Miguel', '150101', 'https://vetcare.pe/logo.png', NULL, NULL, 'Activo');

-- ============================================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- ============================================================

-- SELECT 'USUARIOS' as tabla, COUNT(*) as total FROM usuarios
-- UNION ALL
-- SELECT 'CLIENTES', COUNT(*) FROM clientes
-- UNION ALL
-- SELECT 'MASCOTAS', COUNT(*) FROM mascotas
-- UNION ALL
-- SELECT 'PRODUCTOS', COUNT(*) FROM productos
-- UNION ALL
-- SELECT 'VACUNAS', COUNT(*) FROM vacunas
-- UNION ALL
-- SELECT 'CITAS', COUNT(*) FROM citas
-- UNION ALL
-- SELECT 'FACTURAS', COUNT(*) FROM facturas
-- UNION ALL
-- SELECT 'PROVEEDORES', COUNT(*) FROM proveedores
-- UNION ALL
-- SELECT 'AUDITORIA', COUNT(*) FROM auditoria
-- UNION ALL
-- SELECT 'EMPRESA', COUNT(*) FROM empresa;
