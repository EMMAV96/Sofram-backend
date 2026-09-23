CREATE TABLE actividad (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,

                           detalle_calendario_id BIGINT NOT NULL,
                           empleado_id BIGINT NOT NULL,

                           nombre VARCHAR(150) NOT NULL,
                           descripcion VARCHAR(2000),
                           tipo VARCHAR(100) NOT NULL,
                           duracion INT NOT NULL,
                           cupo_maximo INT NOT NULL,
                           estado VARCHAR(50) NOT NULL,

                           CONSTRAINT fk_actividad_detalle_calendario
                               FOREIGN KEY (detalle_calendario_id)
                                   REFERENCES detalle_calendario(id),

                           CONSTRAINT fk_actividad_empleado
                               FOREIGN KEY (empleado_id)
                                   REFERENCES empleado(id)
);

CREATE INDEX idx_actividad_detalle_calendario
    ON actividad(detalle_calendario_id);

CREATE INDEX idx_actividad_empleado
    ON actividad(empleado_id);