-- SOLO DESARROLLO LOCAL
-- Elimina datos operativos de SOFRAM.
-- NO ejecutar en producción.

USE sofram;

-- Guardar las referencias mínimas necesarias para que admin siga autenticando.
SET @admin_usuario_id := (
    SELECT id
    FROM usuario
    WHERE username = 'admin'
);

SET @admin_empleado_id := (
    SELECT empleado_id
    FROM usuario
    WHERE username = 'admin'
);

SET @admin_cargo_id := (
    SELECT e.cargo_id
    FROM empleado e
    WHERE e.id = @admin_empleado_id
);

SET @admin_rol_id := (
    SELECT rol_id
    FROM usuario
    WHERE username = 'admin'
);

-- Fallar si falta la cuenta administrativa base o sus relaciones obligatorias.
DELIMITER //

CREATE PROCEDURE assert_admin_base_for_local_reset()
BEGIN
    IF @admin_usuario_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No existe usuario admin. Limpieza cancelada.';
    END IF;

    IF @admin_empleado_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario admin no tiene empleado asociado. Limpieza cancelada.';
    END IF;

    IF @admin_cargo_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El empleado admin no tiene cargo asociado. Limpieza cancelada.';
    END IF;

    IF @admin_rol_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario admin no tiene rol asociado. Limpieza cancelada.';
    END IF;
END//

DELIMITER ;

CALL assert_admin_base_for_local_reset();
DROP PROCEDURE assert_admin_base_for_local_reset;

START TRANSACTION;

-- Auditoría y dependientes de actividades.
DELETE FROM auditoria;
DELETE FROM participacion_actividad;
DELETE FROM actividad;
DELETE FROM detalle_calendario;
DELETE FROM calendario;

-- Gestión médica y dependientes de detalle/evolución.
DELETE FROM medicacion;
DELETE FROM tratamiento;
DELETE FROM diagnostico;
DELETE FROM evaluacion;
DELETE FROM atencion_medica;

-- Historia clínica.
DELETE FROM detalle_historia_clinica;
DELETE FROM historia_clinica;

-- Residentes y sus historiales.
DELETE FROM historial_deterioro_residente;
DELETE FROM historial_estado_residente;
DELETE FROM residente;
DELETE FROM habitacion;

-- Turnos del personal.
DELETE FROM asignacion_turno;
DELETE FROM turno;

-- Usuarios, empleados y cargos de prueba. Se conserva admin y sus relaciones.
DELETE FROM usuario
WHERE id <> @admin_usuario_id;

DELETE FROM empleado
WHERE id <> @admin_empleado_id;

DELETE FROM cargo
WHERE id <> @admin_cargo_id;

COMMIT;

-- Reiniciar AUTO_INCREMENT en tablas que quedaron vacías.
ALTER TABLE auditoria AUTO_INCREMENT = 1;
ALTER TABLE participacion_actividad AUTO_INCREMENT = 1;
ALTER TABLE actividad AUTO_INCREMENT = 1;
ALTER TABLE detalle_calendario AUTO_INCREMENT = 1;
ALTER TABLE calendario AUTO_INCREMENT = 1;
ALTER TABLE medicacion AUTO_INCREMENT = 1;
ALTER TABLE tratamiento AUTO_INCREMENT = 1;
ALTER TABLE diagnostico AUTO_INCREMENT = 1;
ALTER TABLE evaluacion AUTO_INCREMENT = 1;
ALTER TABLE atencion_medica AUTO_INCREMENT = 1;
ALTER TABLE detalle_historia_clinica AUTO_INCREMENT = 1;
ALTER TABLE historia_clinica AUTO_INCREMENT = 1;
ALTER TABLE historial_deterioro_residente AUTO_INCREMENT = 1;
ALTER TABLE historial_estado_residente AUTO_INCREMENT = 1;
ALTER TABLE residente AUTO_INCREMENT = 1;
ALTER TABLE asignacion_turno AUTO_INCREMENT = 1;
ALTER TABLE turno AUTO_INCREMENT = 1;
ALTER TABLE habitacion AUTO_INCREMENT = 1;

-- Reiniciar tablas preservadas al próximo ID seguro, sin colisionar con admin.
SET @next_usuario_id := (
    SELECT COALESCE(MAX(id), 0) + 1
    FROM usuario
);
SET @sql := CONCAT('ALTER TABLE usuario AUTO_INCREMENT = ', @next_usuario_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @next_empleado_id := (
    SELECT COALESCE(MAX(id), 0) + 1
    FROM empleado
);
SET @sql := CONCAT('ALTER TABLE empleado AUTO_INCREMENT = ', @next_empleado_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @next_cargo_id := (
    SELECT COALESCE(MAX(id), 0) + 1
    FROM cargo
);
SET @sql := CONCAT('ALTER TABLE cargo AUTO_INCREMENT = ', @next_cargo_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Validaciones esperadas después de la limpieza.
SELECT version, success
FROM flyway_schema_history
ORDER BY installed_rank DESC
LIMIT 1;

SELECT u.id AS usuario_id,
       u.username,
       r.nombre AS rol,
       u.empleado_id,
       e.nombre AS empleado_nombre,
       e.apellido AS empleado_apellido,
       c.nombre AS cargo
FROM usuario u
JOIN rol r ON r.id = u.rol_id
JOIN empleado e ON e.id = u.empleado_id
JOIN cargo c ON c.id = e.cargo_id
WHERE u.username = 'admin';

SELECT 'actividad' AS tabla, COUNT(*) AS total FROM actividad
UNION ALL SELECT 'asignacion_turno', COUNT(*) FROM asignacion_turno
UNION ALL SELECT 'atencion_medica', COUNT(*) FROM atencion_medica
UNION ALL SELECT 'auditoria', COUNT(*) FROM auditoria
UNION ALL SELECT 'calendario', COUNT(*) FROM calendario
UNION ALL SELECT 'detalle_calendario', COUNT(*) FROM detalle_calendario
UNION ALL SELECT 'detalle_historia_clinica', COUNT(*) FROM detalle_historia_clinica
UNION ALL SELECT 'diagnostico', COUNT(*) FROM diagnostico
UNION ALL SELECT 'evaluacion', COUNT(*) FROM evaluacion
UNION ALL SELECT 'habitacion', COUNT(*) FROM habitacion
UNION ALL SELECT 'historia_clinica', COUNT(*) FROM historia_clinica
UNION ALL SELECT 'historial_deterioro_residente', COUNT(*) FROM historial_deterioro_residente
UNION ALL SELECT 'historial_estado_residente', COUNT(*) FROM historial_estado_residente
UNION ALL SELECT 'medicacion', COUNT(*) FROM medicacion
UNION ALL SELECT 'participacion_actividad', COUNT(*) FROM participacion_actividad
UNION ALL SELECT 'residente', COUNT(*) FROM residente
UNION ALL SELECT 'tratamiento', COUNT(*) FROM tratamiento
UNION ALL SELECT 'turno', COUNT(*) FROM turno
UNION ALL SELECT 'usuarios_no_admin', COUNT(*) FROM usuario WHERE username <> 'admin'
UNION ALL SELECT 'empleados_no_admin', COUNT(*) FROM empleado WHERE id <> @admin_empleado_id
UNION ALL SELECT 'cargos_no_admin', COUNT(*) FROM cargo WHERE id <> @admin_cargo_id;
