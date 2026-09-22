CREATE TABLE diagnostico (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

                             detalle_historia_clinica_id BIGINT NOT NULL,

                             descripcion VARCHAR(2000) NOT NULL,

                             CONSTRAINT fk_diagnostico_detalle_historia
                                 FOREIGN KEY (detalle_historia_clinica_id)
                                     REFERENCES detalle_historia_clinica(id)
);

CREATE INDEX idx_diagnostico_detalle_historia
    ON diagnostico(detalle_historia_clinica_id);