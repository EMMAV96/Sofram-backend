CREATE TABLE tratamiento (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

                             detalle_historia_clinica_id BIGINT NOT NULL,

                             nombre VARCHAR(150) NOT NULL,

                             descripcion VARCHAR(2000),

                             CONSTRAINT fk_tratamiento_detalle_historia
                                 FOREIGN KEY (detalle_historia_clinica_id)
                                     REFERENCES detalle_historia_clinica(id)
);

CREATE INDEX idx_tratamiento_detalle_historia
    ON tratamiento(detalle_historia_clinica_id);