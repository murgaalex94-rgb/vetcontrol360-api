# Instrucciones para Ejecutar Scripts SQL en Neon

## Archivos Generados

He creado dos archivos SQL en el directorio `backend/`:

1. **schema.sql** - Contiene todas las sentencias `CREATE TABLE` para crear la estructura de la base de datos
2. **data.sql** - Contiene datos de prueba para poblar las tablas

## Entidades Identificadas

Se encontraron las siguientes entidades JPA en el backend:

- **Usuario** (usuarios) - Sistema de autenticación
- **Cliente** (clientes) - Información de clientes
- **Mascota** (mascotas) - Mascotas de los clientes (relacionada con Cliente)
- **Producto** (productos) - Inventario de productos
- **Vacuna** (vacunas) - Registro de vacunas (relacionada con Mascota)
- **Cita** (citas) - Citas médicas (relacionada con Cliente y Mascota)
- **Factura** (facturas) - Facturación
- **Auditoria** (auditoria) - Registro de acciones del sistema

## Cómo Ejecutar los Scripts en Neon

### Opción 1: Usando el Editor SQL de Neon (Recomendado)

1. **Iniciar sesión en Neon**
   - Ve a [https://neon.tech](https://neon.tech)
   - Inicia sesión con tu cuenta

2. **Seleccionar tu proyecto**
   - En el dashboard, selecciona el proyecto de VetControl 360
   - Si no tienes un proyecto, crea uno nuevo

3. **Abrir el Editor SQL**
   - En el panel izquierdo, haz clic en "SQL Editor"
   - Se abrirá un editor de SQL donde puedes ejecutar consultas

4. **Ejecutar schema.sql**
   - Copia todo el contenido del archivo `backend/schema.sql`
   - Pégalo en el editor SQL de Neon
   - Haz clic en el botón "Run" o presiona `Ctrl+Enter`
   - Verifica que no haya errores en la consola

5. **Ejecutar data.sql**
   - Limpia el editor SQL
   - Copia todo el contenido del archivo `backend/data.sql`
   - Pégalo en el editor SQL de Neon
   - Haz clic en el botón "Run" o presiona `Ctrl+Enter`
   - Verifica que los datos se hayan insertado correctamente

6. **Verificar la creación de tablas**
   - En el panel izquierdo, haz clic en "Tables"
   - Deberías ver las siguientes tablas:
     - usuarios
     - clientes
     - mascotas
     - productos
     - vacunas
     - citas
     - facturas
     - auditoria

### Opción 2: Usando psql (Línea de Comandos)

Si prefieres usar la línea de comandos:

1. **Obtener la cadena de conexión**
   - En Neon, ve a tu proyecto
   - Haz clic en "Connection Details"
   - Copia la cadena de conexión (Connection String)

2. **Ejecutar los scripts**
   ```bash
   # Reemplaza con tu cadena de conexión real
   psql "postgresql://usuario:contraseña@ep-host-neon/dbname?sslmode=require" -f backend/schema.sql
   psql "postgresql://usuario:contraseña@ep-host-neon/dbname?sslmode=require" -f backend/data.sql
   ```

### Opción 3: Usando pgAdmin

1. **Descargar e instalar pgAdmin** si no lo tienes
2. **Conectar a tu base de datos Neon**
   - Crea una nueva conexión usando los detalles de Neon
3. **Ejecutar los scripts**
   - Abre el "Query Tool"
   - Carga y ejecuta `schema.sql`
   - Luego carga y ejecuta `data.sql`

## Verificación de Datos

Después de ejecutar los scripts, puedes verificar que los datos se insertaron correctamente ejecutando estas consultas en el editor SQL de Neon:

```sql
-- Ver usuarios
SELECT * FROM usuarios;

-- Ver clientes
SELECT * FROM clientes;

-- Ver mascotas con sus clientes
SELECT m.nombre AS mascota, m.especie, c.nombre AS cliente 
FROM mascotas m 
JOIN clientes c ON m.cliente_id = c.id;

-- Ver productos
SELECT * FROM productos;

-- Ver vacunas con mascotas
SELECT v.vacuna, v.fecha_aplicacion, m.nombre AS mascota 
FROM vacunas v 
JOIN mascotas m ON v.mascota_id = m.id;

-- Ver citas
SELECT c.fecha, c.hora, cli.nombre AS cliente, m.nombre AS mascota 
FROM citas c 
JOIN clientes cli ON c.cliente_id = cli.id 
JOIN mascotas m ON c.mascota_id = m.id;

-- Ver facturas
SELECT * FROM facturas;

-- Ver auditoría
SELECT * FROM auditoria ORDER BY fecha_hora DESC;
```

## Notas Importantes

1. **Contraseñas**: Los usuarios de prueba tienen el password hash correspondiente a "password123" (para propósitos de desarrollo)
2. **Relaciones**: Las claves foráneas están configuradas con `ON DELETE CASCADE`, lo que significa que si eliminas un cliente, se eliminarán sus mascotas, citas y vacunas relacionadas
3. **Índices**: Se han creado índices para mejorar el rendimiento en consultas frecuentes
4. **Tipos de datos**: Todos los tipos de datos son compatibles con PostgreSQL

## Solución de Problemas

### Error: "relation already exists"
- Esto significa que las tablas ya existen. Puedes eliminarlas y recrearlas:
  ```sql
  DROP TABLE IF EXISTS auditoria, facturas, citas, vacunas, productos, mascotas, clientes, usuarios CASCADE;
  ```
  Luego vuelve a ejecutar `schema.sql`

### Error: "foreign key violation"
- Asegúrate de ejecutar `schema.sql` antes de `data.sql`
- Verifica que los datos en `data.sql` respeten las relaciones (por ejemplo, cada mascota debe tener un cliente_id válido)

### Error de conexión
- Verifica que tu cadena de conexión sea correcta
- Asegúrate de que SSL esté habilitado (sslmode=require)
- Verifica que tu usuario tenga los permisos necesarios

## Próximos Pasos

1. Configura tu backend Spring Boot para usar la base de datos Neon
2. Actualiza el archivo `application.properties` o `application.yml` con las credenciales de Neon
3. Ejecuta el backend y verifica que se conecte correctamente a la base de datos
4. Prueba la aplicación con los datos de prueba insertados
