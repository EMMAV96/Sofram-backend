ALTER TABLE detalle_historia_clinica
    ADD COLUMN profesional_id BIGINT NULL;

ALTER TABLE detalle_historia_clinica
    ADD CONSTRAINT fk_detalle_historia_clinica_profesional
        FOREIGN KEY (profesional_id)
            REFERENCES empleado(id);

CREATE INDEX idx_detalle_historia_clinica_profesional
    ON detalle_historia_clinica(profesional_id);
