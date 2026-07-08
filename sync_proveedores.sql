-- Script SQL para sincronizar proveedores desde la tabla productos
-- Este script inserta proveedores que existen en productos pero no en proveedores

-- Insertar proveedores únicos de productos que no existen en la tabla proveedores
-- Genera RUC ficticio y asigna estado Activo por defecto
INSERT INTO proveedores (codigo, nombre, ruc, rubro, telefono, email, estado, direccion)
SELECT 
    'PROV-' || LPAD(ROW_NUMBER() OVER (ORDER BY DISTINCT proveedor)::TEXT, 4, '0') as codigo,
    DISTINCT proveedor as nombre,
    '20' || LPAD(FLOOR(RANDOM() * 100000000)::TEXT, 8, '0') as ruc,  -- RUC ficticio de 11 dígitos
    'Farmacia Veterinaria' as rubro,
    NULL as telefono,
    NULL as email,
    'Activo' as estado,
    NULL as direccion
FROM productos
WHERE proveedor IS NOT NULL 
  AND proveedor != ''
  AND LOWER(TRIM(proveedor)) NOT IN (
    SELECT LOWER(TRIM(nombre)) FROM proveedores
  );

-- Verificar cuántos proveedores fueron insertados
-- (Descomentar para ver el resultado)
-- SELECT COUNT(*) as proveedores_insertados FROM proveedores WHERE estado = 'Activo' AND rubro = 'Farmacia Veterinaria';
