CREATE TABLE medicacion (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            detalle_historia_clinica_id BIGINT NOT NULL,

                            nombre VARCHAR(150) NOT NULL,
                            dosis VARCHAR(100) NOT NULL,
                            frecuencia VARCHAR(150) NOT NULL,

                            CONSTRAINT fk_medicacion_detalle_historia
                                FOREIGN KEY (detalle_historia_clinica_id)
                                    REFERENCES detalle_historia_clinica(id)
);

CREATE INDEX idx_medicacion_detalle_historia
    ON medicacion(detalle_historia_clinica_id);