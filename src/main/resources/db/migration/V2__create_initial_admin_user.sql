INSERT INTO cargo (nombre, sector, especialidad)
SELECT 'Administrador del sistema', 'Administracion', 'SOFRAM'
WHERE NOT EXISTS (
    SELECT 1 FROM empleado WHERE dni = '00000000'
);

INSERT INTO empleado (
    cargo_id,
    apellido,
    nombre,
    dni,
    fecha_nacimiento,
    direccion,
    telefono,
    email
)
SELECT
    c.id,
    'Sistema',
    'Administrador',
    '00000000',
    '1990-01-01',
    NULL,
    NULL,
    'admin@sofram.local'
FROM cargo c
WHERE c.nombre = 'Administrador del sistema'
  AND NOT EXISTS (
      SELECT 1 FROM empleado WHERE dni = '00000000'
  )
LIMIT 1;

INSERT INTO usuario (empleado_id, rol_id, username, password_hash, activo)
SELECT
    e.id,
    r.id,
    'admin',
    '$2a$10$24yGlBPv6xLD7D3WifniXOd.CQlJIP7srnpSXzqBeqWj8WangYlOK',
    TRUE
FROM empleado e
JOIN rol r ON r.nombre = 'ADMINISTRADOR'
WHERE e.dni = '00000000'
  AND NOT EXISTS (
      SELECT 1 FROM usuario WHERE username = 'admin'
  );
