CREATE TABLE participacion_actividad (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                         actividad_id BIGINT NOT NULL,
                                         residente_id BIGINT NOT NULL,

                                         fecha DATE NOT NULL,
                                         asistencia BOOLEAN NOT NULL,
                                         estado VARCHAR(50) NOT NULL,
                                         observaciones VARCHAR(1000),

                                         CONSTRAINT fk_participacion_actividad
                                             FOREIGN KEY (actividad_id)
                                                 REFERENCES actividad(id),

                                         CONSTRAINT fk_participacion_residente
                                             FOREIGN KEY (residente_id)
                                                 REFERENCES residente(id),

                                         CONSTRAINT uk_participacion_actividad_residente
                                             UNIQUE (actividad_id, residente_id)
);

CREATE INDEX idx_participacion_actividad
    ON participacion_actividad(actividad_id);

CREATE INDEX idx_participacion_residente
    ON participacion_actividad(residente_id);