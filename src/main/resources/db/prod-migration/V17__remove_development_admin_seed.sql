DELETE FROM usuario
WHERE username = 'admin'
  AND password_hash = '$2a$10$24yGlBPv6xLD7D3WifniXOd.CQlJIP7srnpSXzqBeqWj8WangYlOK'
  AND empleado_id IN (
      SELECT id
      FROM empleado
      WHERE dni = '00000000'
        AND apellido = 'Sistema'
        AND nombre = 'Administrador'
  );

DELETE FROM empleado
WHERE dni = '00000000'
  AND apellido = 'Sistema'
  AND nombre = 'Administrador'
  AND NOT EXISTS (
      SELECT 1
      FROM usuario
      WHERE usuario.empleado_id = empleado.id
  );

DELETE FROM cargo
WHERE nombre = 'Administrador del sistema'
  AND sector = 'Administracion'
  AND especialidad = 'SOFRAM'
  AND NOT EXISTS (
      SELECT 1
      FROM empleado
      WHERE empleado.cargo_id = cargo.id
  );
