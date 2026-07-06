const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 8080;

// Middleware
app.use(cors());
app.use(express.json());

// Usuarios de prueba
const users = [
  { id: 1, username: 'admin', password: 'admin123', nombre: 'Administrador', rol: 'Administrador' },
  { id: 2, username: 'veterinario', password: 'vet123', nombre: 'Dr. Veterinario', rol: 'Veterinario' },
  { id: 3, username: 'recepcionista', password: 'recep123', nombre: 'Recepcionista', rol: 'Recepcionista' },
  { id: 4, username: 'asistente', password: 'asis123', nombre: 'Asistente', rol: 'Asistente' },
  { id: 5, username: 'Alex Murga', password: 'AlexMurga.20*', nombre: 'Alex Murga', rol: 'Administrador' }
];

// ============================================
// RUTAS DE AUTENTICACIÓN
// ============================================

// Login
app.post('/api/auth/login', (req, res) => {
  const { username, password } = req.body;
  
  console.log('Intento de login:', username);
  
  const user = users.find(u => u.username === username && u.password === password);
  
  if (!user) {
    return res.status(401).json({ 
      success: false, 
      message: 'Credenciales inválidas' 
    });
  }
  
  // Generar token simple (en producción usa JWT)
  const token = `token_${user.id}_${Date.now()}`;
  
  res.json({
    success: true,
    token,
    user: {
      id: user.id,
      nombre: user.nombre,
      username: user.username,
      rol: user.rol
    }
  });
});

// Verificar token (para rutas protegidas)
app.get('/api/auth/verify', (req, res) => {
  const token = req.headers.authorization?.split(' ')[1];
  
  if (!token) {
    return res.status(401).json({ message: 'No autorizado' });
  }
  
  res.json({ valid: true });
});

// ============================================
// RUTAS PARA CLIENTES (Datos de prueba)
// ============================================

const clientes = [
  { id: 1, nombre: 'Carlos Alberto Ramírez', apellidos: 'Ramírez Torres', dni: '45678912', telefono: '987654321', whatsapp: '987654321', email: 'carlos.ramirez@gmail.com', fechaNacimiento: '1985-03-15', edad: 39, genero: 'Masculino', estadoCivil: 'Casado/a', direccion: 'Av. Los Olivos 123', departamento: 'Lima', provincia: 'Lima', distrito: 'San Isidro', referencia: 'Cerca al parque', notas: '', estado: 'Activo' },
  { id: 2, nombre: 'María Fernanda López', apellidos: 'López García', dni: '78945612', telefono: '912345678', whatsapp: '912345678', email: 'maria.lopez@hotmail.com', fechaNacimiento: '1990-07-22', edad: 34, genero: 'Femenino', estadoCivil: 'Soltero/a', direccion: 'Jr. Las Flores 456', departamento: 'Lima', provincia: 'Lima', distrito: 'Miraflores', referencia: 'Altura cuadra 5', notas: '', estado: 'Activo' },
  { id: 3, nombre: 'Juan Diego Torres', apellidos: 'Torres Rivas', dni: '32165498', telefono: '923456789', whatsapp: '', email: 'juan.torres@gmail.com', fechaNacimiento: '1982-11-08', edad: 42, genero: 'Masculino', estadoCivil: 'Divorciado/a', direccion: 'Calle Real 789', departamento: 'Junín', provincia: 'Huancayo', distrito: 'Huancayo', referencia: 'Frente al mercado', notas: '', estado: 'Activo' }
];

app.get('/api/clientes', (req, res) => {
  res.json(clientes);
});

app.post('/api/clientes', (req, res) => {
  var nuevo = req.body;
  nuevo.id = clientes.length + 1;
  nuevo.estado = nuevo.estado || 'Activo';
  clientes.push(nuevo);
  res.status(201).json(nuevo);
});

app.put('/api/clientes/:id', (req, res) => {
  var id = parseInt(req.params.id);
  var idx = clientes.findIndex(c => c.id === id);
  if (idx === -1) return res.status(404).json({ message: 'Cliente no encontrado' });
  clientes[idx] = Object.assign(clientes[idx], req.body, { id });
  res.json(clientes[idx]);
});

app.delete('/api/clientes/:id', (req, res) => {
  var id = parseInt(req.params.id);
  var idx = clientes.findIndex(c => c.id === id);
  if (idx === -1) return res.status(404).json({ message: 'Cliente no encontrado' });
  clientes.splice(idx, 1);
  res.status(204).send();
});

// ============================================
// RUTAS PARA MASCOTAS (Datos de prueba)
// ============================================

const mascotas = [
  { id: 1, nombre: 'Max', especie: 'Perro', raza: 'Golden Retriever', edad: '3 años', peso: 28.5, cliente_id: 1, estado: 'Activo' },
  { id: 2, nombre: 'Luna', especie: 'Gato', raza: 'Siamés', edad: '2 años', peso: 4.2, cliente_id: 2, estado: 'Activo' },
  { id: 3, nombre: 'Rocky', especie: 'Perro', raza: 'Labrador', edad: '5 años', peso: 32.0, cliente_id: 1, estado: 'Activo' }
];

app.get('/api/mascotas', (req, res) => {
  res.json(mascotas);
});

// ============================================
// RUTAS PARA PRODUCTOS (Datos de prueba)
// ============================================

const productos = [
  { id: 1, nombre: 'Antipulgas Frontline', sku: 'FRONT-001', tipo: 'Medicamentos', stockActual: 50, stockMinimo: 10, precioVenta: 45.00, estado: 'Activo' },
  { id: 2, nombre: 'Croquetas Premium 20kg', sku: 'CROQ-001', tipo: 'Alimentos', stockActual: 15, stockMinimo: 5, precioVenta: 120.00, estado: 'Activo' },
  { id: 3, nombre: 'Collar Antipulgas', sku: 'COLL-001', tipo: 'Accesorios', stockActual: 8, stockMinimo: 10, precioVenta: 25.00, estado: 'Bajo' }
];

app.get('/api/productos', (req, res) => {
  res.json(productos);
});

// ============================================
// RUTAS PARA FACTURAS (Datos de prueba)
// ============================================

const facturas = [
  { id: 1, numero: 'F-000001', cliente: 'Juan Pérez', fecha: '2025-07-01', total: 120.00, estado: 'Pagada', metodoPago: 'Efectivo' },
  { id: 2, numero: 'F-000002', cliente: 'María López', fecha: '2025-07-02', total: 45.00, estado: 'Pendiente', metodoPago: 'Tarjeta' }
];

app.get('/api/facturas', (req, res) => {
  res.json(facturas);
});

// ============================================
// INICIAR SERVIDOR
// ============================================

app.listen(PORT, () => {
  console.log('========================================');
  console.log('🚀 SERVIDOR BACKEND INICIADO');
  console.log(`📍 URL: http://localhost:${PORT}/api`);
  console.log('========================================');
  console.log('');
  console.log('👤 CREDENCIALES DE PRUEBA:');
  console.log('  Usuario: admin / Contraseña: admin123');
  console.log('  Usuario: veterinario / Contraseña: vet123');
  console.log('  Usuario: recepcionista / Contraseña: recep123');
  console.log('  Usuario: asistente / Contraseña: asis123');
  console.log('');
  console.log('🔗 RUTAS DISPONIBLES:');
  console.log('  POST /api/auth/login');
  console.log('  GET  /api/clientes');
  console.log('  GET  /api/mascotas');
  console.log('  GET  /api/productos');
  console.log('  GET  /api/facturas');
  console.log('========================================');
});