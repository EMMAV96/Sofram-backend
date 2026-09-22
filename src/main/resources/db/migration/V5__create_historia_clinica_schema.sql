CREATE TABLE historia_clinica (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                  residente_id BIGINT NOT NULL UNIQUE,

                                  fecha_creacion DATE NOT NULL,
                                  observaciones VARCHAR(1000),

                                  CONSTRAINT fk_historia_clinica_residente
                                      FOREIGN KEY (residente_id)
                                          REFERENCES residente(id)
);

CREATE TABLE detalle_historia_clinica (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                          historia_clinica_id BIGINT NOT NULL,

                                          fecha DATE NOT NULL,
                                          observaciones VARCHAR(1000),

                                          CONSTRAINT fk_detalle_historia_clinica
                                              FOREIGN KEY (historia_clinica_id)
                                                  REFERENCES historia_clinica(id)
);