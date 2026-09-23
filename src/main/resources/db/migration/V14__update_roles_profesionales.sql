-- Actualización de roles profesionales de SOFRAM

-- Reemplazar CUIDADOR por LIC_ENFERMERIA
UPDATE rol
SET nombre = 'LIC_ENFERMERIA',
    descripcion = 'Licenciado/a en Enfermería'
WHERE nombre = 'CUIDADOR';

-- Reemplazar TALLERISTA por TERAPISTA_OCUPACIONAL
UPDATE rol
SET nombre = 'TERAPISTA_OCUPACIONAL',
    descripcion = 'Licenciado/a en Terapia Ocupacional'
WHERE nombre = 'TALLERISTA';

-- Agregar rol ENFERMERO
INSERT INTO rol (nombre, descripcion, created_at, updated_at)
VALUES (
           'ENFERMERO',
           'Personal de enfermería',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

-- Agregar rol PSICOLOGO
INSERT INTO rol (nombre, descripcion, created_at, updated_at)
VALUES (
           'PSICOLOGO',
           'Licenciado/a en Psicología',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );