CREATE TABLE turno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE asignacion_turno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empleado_id BIGINT NOT NULL,
    turno_id BIGINT NOT NULL,
    fecha_desde DATE NOT NULL,
    fecha_hasta DATE NULL,
    motivo_cambio VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_asignacion_turno_empleado FOREIGN KEY (empleado_id) REFERENCES empleado(id),
    CONSTRAINT fk_asignacion_turno_turno FOREIGN KEY (turno_id) REFERENCES turno(id)
);

CREATE INDEX idx_asigturno_empleado_fecha ON asignacion_turno(empleado_id, fecha_desde);

INSERT INTO turno (descripcion, hora_inicio, hora_fin) VALUES
    ('MANANA', '06:00:00', '14:00:00'),
    ('TARDE', '14:00:00', '22:00:00'),
    ('NOCHE', '22:00:00', '06:00:00');
