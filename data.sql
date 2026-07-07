-- Data SQL para VetControl 360 - Datos de prueba
-- Base de datos PostgreSQL (Neon)

-- Insertar usuarios de prueba
INSERT INTO usuarios (usuario, password_hash, nombre_completo, activo, id_rol) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador Sistema', true, 1),
('veterinario1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Carlos Ramírez', true, 2),
('veterinario2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dra. María García', true, 2),
('recepcionista', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ana Martínez', true, 3);

-- Insertar clientes de prueba
INSERT INTO clientes (nombre, apellidos, dni, telefono, whatsapp, email, fecha_nacimiento, edad, genero, estado_civil, direccion, departamento, provincia, distrito, referencia, notas, estado) VALUES
('Juan', 'Pérez López', '12345678', '987654321', '987654321', 'juan.perez@email.com', '1985-03-15', 39, 'Masculino', 'Casado', 'Av. Principal 123', 'Lima', 'Lima', 'Miraflores', 'Cerca del parque', 'Cliente frecuente', 'Activo'),
('María', 'González Rodríguez', '87654321', '987654322', '987654322', 'maria.gonzalez@email.com', '1990-07-22', 34, 'Femenino', 'Soltera', 'Jr. Secundaria 456', 'Lima', 'Lima', 'San Borja', 'Frente al colegio', NULL, 'Activo'),
('Carlos', 'Rodríguez Martínez', '54321678', '987654323', '987654323', 'carlos.rodriguez@email.com', '1978-11-08', 46, 'Masculino', 'Divorciado', 'Calle Los Olivos 789', 'Lima', 'Lima', 'Los Olivos', 'Al lado del mercado', 'Tiene varias mascotas', 'Activo'),
('Ana', 'Fernández Sánchez', '32165487', '987654324', '987654324', 'ana.fernandez@email.com', '1995-05-30', 29, 'Femenino', 'Casada', 'Av. Brasil 321', 'Lima', 'Lima', 'Surco', 'Cerca del centro comercial', NULL, 'Activo'),
('Roberto', 'Díaz Torres', '65432187', '987654325', '987654325', 'roberto.diaz@email.com', '1982-09-12', 42, 'Masculino', 'Casado', 'Psj. Las Flores 654', 'Lima', 'Lima', 'La Molina', 'Detrás del hospital', 'Cliente nuevo', 'Activo');

-- Insertar mascotas de prueba (relacionadas con clientes)
INSERT INTO mascotas (nombre, especie, raza, sexo, fecha_nacimiento, color, microchip, peso, tipo_pelaje, tamano, esterilizado, notas, estado, foto_url, cliente_id) VALUES
('Max', 'Perro', 'Golden Retriever', 'Macho', '2020-05-10', 'Dorado', 'MC001234567890', 32.5, 'Largo', 'Grande', true, 'Muy activo y amigable', 'Activo', NULL, 1),
('Luna', 'Gato', 'Siamés', 'Hembra', '2021-08-15', 'Crema y gris', 'MC002345678901', 4.2, 'Corto', 'Pequeño', true, 'Un poco tímida', 'Activo', NULL, 2),
('Rocky', 'Perro', 'Pastor Alemán', 'Macho', '2019-03-20', 'Negro y marrón', 'MC003456789012', 38.0, 'Corto', 'Grande', true, 'Excelente guardián', 'Activo', NULL, 3),
('Coco', 'Perro', 'Labrador', 'Hembra', '2022-01-05', 'Chocolate', 'MC004567890123', 28.5, 'Corto', 'Mediano', false, 'Muy juguetona', 'Activo', NULL, 4),
('Simba', 'Gato', 'Persa', 'Macho', '2020-11-25', 'Naranja', 'MC005678901234', 5.8, 'Largo', 'Mediano', true, 'Tranquilo y cariñoso', 'Activo', NULL, 5);

-- Insertar productos de prueba
INSERT INTO productos (nombre, sku, categoria, tipo, presentacion, unidad, descripcion, proveedor, precio_compra, precio_venta, margen, stock_actual, stock_minimo, stock_maximo, ubicacion, fecha_vencimiento, lote, fabricante, almacenamiento, registro_sanitario, notas, imagen) VALUES
('Doxivet 100 mg', 'PROD-001', 'Antibiótico', 'Medicamento', 'Tabletas', 'Caja 20', 'Antibiótico de amplio espectro para perros', 'Laboratorios VetPharma', 45.00, 65.00, 20.00, 150, 20, 200, 'Estante A1', '2025-12-31', 'LOT-2024-001', 'VetPharma S.A.', 'Ambiente seco', 'REG-12345', NULL, NULL),
('Antiparasitario Spot-On', 'PROD-002', 'Antiparasitario', 'Externo', 'Pipetas', 'Caja 3', 'Antiparasitario externo para perros y gatos', 'PetCare Solutions', 35.00, 55.00, 20.00, 80, 15, 100, 'Estante B2', '2026-06-30', 'LOT-2024-002', 'PetCare Inc.', 'Lugar fresco', 'REG-67890', NULL, NULL),
('Vitaminas Premium', 'PROD-003', 'Suplemento', 'Vitaminas', 'Tabletas', 'Frasco 60', 'Multivitamínico para mascotas adultas', 'NutriPet Labs', 28.00, 42.00, 14.00, 200, 30, 250, 'Estante C3', '2027-03-15', 'LOT-2024-003', 'NutriPet Corp.', 'Ambiente seco', 'REG-24680', NULL, NULL);

-- Insertar vacunas de prueba (relacionadas con mascotas)
INSERT INTO vacunas (mascota_id, vacuna, lote, laboratorio, fecha_aplicacion, proxima_dosis, aplicada_por, via_aplicacion, sitio_aplicacion, estado, observaciones, tipo_recordatorio, dias_antes, notas_adicionales) VALUES
(1, 'Rabia', 'VAC-2024-001', 'Laboratorios BioVet', '2024-01-15', '2025-01-15', 'Dr. Carlos Ramírez', 'Subcutánea', 'Cuello', 'Aplicada', 'Reacción normal', 'Email', 7, NULL),
(1, 'Séxtuple', 'VAC-2024-002', 'Laboratorios BioVet', '2024-02-20', '2025-02-20', 'Dr. Carlos Ramírez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(2, 'Triple Felina', 'VAC-2024-003', 'Laboratorios CatVet', '2024-03-10', '2025-03-10', 'Dra. María García', 'Subcutánea', 'Espalda', 'Aplicada', NULL, 'WhatsApp', 5, NULL),
(3, 'Rabia', 'VAC-2024-004', 'Laboratorios BioVet', '2024-01-20', '2025-01-20', 'Dr. Carlos Ramírez', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'Email', 7, NULL),
(4, 'Séxtuple', 'VAC-2024-005', 'Laboratorios BioVet', '2024-04-05', '2025-04-05', 'Dra. María García', 'Subcutánea', 'Cuello', 'Aplicada', NULL, 'WhatsApp', 7, NULL);

-- Insertar citas de prueba (relacionadas con clientes y mascotas)
INSERT INTO citas (cliente_id, mascota_id, tipo_cita, fecha, hora, motivo, duracion, veterinario, consultorio, notas, recordatorio, whatsapp, email) VALUES
(1, 1, 'Consulta General', '2024-07-15', '09:30:00', 'Revisión anual de rutina', 30, 'Dr. Carlos Ramírez', 'Consultorio 1', 'Mascota en buen estado de salud', true, true, true),
(2, 2, 'Vacunación', '2024-07-16', '10:00:00', 'Aplicación de vacuna anual', 15, 'Dra. María García', 'Consultorio 2', NULL, true, false, true),
(3, 3, 'Control / Revisión', '2024-07-17', '11:30:00', 'Seguimiento post-operatorio', 20, 'Dr. Carlos Ramírez', 'Consultorio 1', 'Cicatrización normal', true, true, false),
(4, 4, 'Consulta General', '2024-07-18', '14:00:00', 'Chequeo de rutina', 30, 'Dra. María García', 'Consultorio 2', NULL, true, true, true),
(5, 5, 'Desparasitación', '2024-07-19', '15:30:00', 'Desparasitación interna', 15, 'Dr. Carlos Ramírez', 'Consultorio 1', NULL, false, true, false);

-- Insertar facturas de prueba
INSERT INTO facturas (numero, fecha, cliente, telefono, mascota, raza, foto, total, estado, metodo_pago) VALUES
('F-000001', '2024-07-15', 'Juan Pérez López', '987654321', 'Max', 'Golden Retriever', NULL, 85.00, 'Pagada', 'Efectivo'),
('F-000002', '2024-07-16', 'María González Rodríguez', '987654322', 'Luna', 'Siamés', NULL, 55.00, 'Pagada', 'Tarjeta'),
('F-000003', '2024-07-17', 'Carlos Rodríguez Martínez', '987654323', 'Rocky', 'Pastor Alemán', NULL, 120.00, 'Pendiente', 'Transferencia');

-- Insertar registros de auditoría de prueba
INSERT INTO auditoria (fecha_hora, usuario, accion, modulo, descripcion, ip_dispositivo, navegador, severidad, estado) VALUES
('2024-07-10 08:30:00', 'admin', 'LOGIN', 'Autenticación', 'Usuario admin inició sesión', '192.168.1.100', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-10 09:15:00', 'veterinario1', 'CREAR', 'Citas', 'Cita creada para cliente Juan Pérez', '192.168.1.101', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-10 10:00:00', 'veterinario2', 'ACTUALIZAR', 'Vacunas', 'Vacuna aplicada a mascota Luna', '192.168.1.102', 'Firefox 115.0', 'INFO', 'Exitoso'),
('2024-07-10 11:30:00', 'recepcionista', 'CREAR', 'Facturas', 'Factura F-000001 generada', '192.168.1.103', 'Chrome 120.0', 'INFO', 'Exitoso'),
('2024-07-10 14:00:00', 'admin', 'ELIMINAR', 'Clientes', 'Intento de eliminar cliente con mascotas', '192.168.1.100', 'Chrome 120.0', 'WARNING', 'Fallido'),
('2024-07-10 15:45:00', 'veterinario1', 'CREAR', 'Mascotas', 'Nueva mascota registrada: Coco', '192.168.1.101', 'Chrome 120.0', 'INFO', 'Exitoso');
