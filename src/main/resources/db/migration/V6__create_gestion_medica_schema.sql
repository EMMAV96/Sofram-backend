CREATE TABLE atencion_medica (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                 residente_id BIGINT NOT NULL,
                                 empleado_id BIGINT NOT NULL,

                                 fecha DATE NOT NULL,
                                 motivo VARCHAR(500),
                                 tipo_intervencion VARCHAR(100),
                                 observaciones VARCHAR(1000),

                                 CONSTRAINT fk_atencion_medica_residente
                                     FOREIGN KEY (residente_id)
                                         REFERENCES residente(id),

                                 CONSTRAINT fk_atencion_medica_empleado
                                     FOREIGN KEY (empleado_id)
                                         REFERENCES empleado(id)
);

CREATE INDEX idx_atencion_medica_residente
    ON atencion_medica(residente_id);

CREATE INDEX idx_atencion_medica_empleado
    ON atencion_medica(empleado_id);

CREATE TABLE evaluacion (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            atencion_medica_id BIGINT NOT NULL UNIQUE,
                            detalle_historia_clinica_id BIGINT NOT NULL,

                            tipo_evaluacion VARCHAR(100) NOT NULL,
                            descripcion VARCHAR(2000) NOT NULL,
                            plan_intervencion VARCHAR(2000),

                            CONSTRAINT fk_evaluacion_atencion_medica
                                FOREIGN KEY (atencion_medica_id)
                                    REFERENCES atencion_medica(id),

                            CONSTRAINT fk_evaluacion_detalle_historia
                                FOREIGN KEY (detalle_historia_clinica_id)
                                    REFERENCES detalle_historia_clinica(id)
);

CREATE INDEX idx_evaluacion_detalle_historia
    ON evaluacion(detalle_historia_clinica_id);