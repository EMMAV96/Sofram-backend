CREATE TABLE calendario (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            nombre VARCHAR(150) NOT NULL,
                            periodo VARCHAR(100) NOT NULL,
                            anio INT NOT NULL,
                            estado VARCHAR(50) NOT NULL
);

CREATE TABLE detalle_calendario (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    calendario_id BIGINT NOT NULL,
                                    fecha DATE NOT NULL,
                                    hora_inicio TIME NOT NULL,
                                    hora_fin TIME NOT NULL,
                                    estado VARCHAR(50) NOT NULL,

                                    CONSTRAINT fk_detalle_calendario_calendario
                                        FOREIGN KEY (calendario_id)
                                            REFERENCES calendario(id)
);

CREATE INDEX idx_detalle_calendario_calendario
    ON detalle_calendario(calendario_id);

CREATE INDEX idx_detalle_calendario_fecha
    ON detalle_calendario(fecha);