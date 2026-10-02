INSERT INTO estado_residente (nombre)
SELECT 'ACTIVO'
WHERE NOT EXISTS (
    SELECT 1
    FROM estado_residente
    WHERE nombre = 'ACTIVO'
);

INSERT INTO estado_residente (nombre)
SELECT 'EGRESADO'
WHERE NOT EXISTS (
    SELECT 1
    FROM estado_residente
    WHERE nombre = 'EGRESADO'
);
