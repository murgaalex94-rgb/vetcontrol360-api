-- Schema SQL para VetControl 360 - Base de datos PostgreSQL (Neon)
-- Generado a partir de las entidades JPA del backend Spring Boot

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    usuario VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255),
    activo BOOLEAN,
    id_rol INTEGER
);

-- Tabla: clientes
CREATE TABLE IF NOT EXISTS clientes (
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
CREATE TABLE IF NOT EXISTS mascotas (
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
    cliente_id BIGINT,
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

-- Tabla: productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    sku VARCHAR(100),
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
CREATE TABLE IF NOT EXISTS vacunas (
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
CREATE TABLE IF NOT EXISTS citas (
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
CREATE TABLE IF NOT EXISTS facturas (
    id BIGSERIAL PRIMARY KEY,
    numero VARCHAR(100),
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
CREATE TABLE IF NOT EXISTS auditoria (
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

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_clientes_dni ON clientes(dni);
CREATE INDEX IF NOT EXISTS idx_clientes_estado ON clientes(estado);
CREATE INDEX IF NOT EXISTS idx_mascotas_cliente ON mascotas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_mascotas_estado ON mascotas(estado);
CREATE INDEX IF NOT EXISTS idx_productos_sku ON productos(sku);
CREATE INDEX IF NOT EXISTS idx_productos_categoria ON productos(categoria);
CREATE INDEX IF NOT EXISTS idx_vacunas_mascota ON vacunas(mascota_id);
CREATE INDEX IF NOT EXISTS idx_vacunas_fecha ON vacunas(fecha_aplicacion);
CREATE INDEX IF NOT EXISTS idx_citas_cliente ON citas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_citas_mascota ON citas(mascota_id);
CREATE INDEX IF NOT EXISTS idx_citas_fecha ON citas(fecha);
CREATE INDEX IF NOT EXISTS idx_facturas_numero ON facturas(numero);
CREATE INDEX IF NOT EXISTS idx_auditoria_fecha ON auditoria(fecha_hora);
CREATE INDEX IF NOT EXISTS idx_auditoria_usuario ON auditoria(usuario);
