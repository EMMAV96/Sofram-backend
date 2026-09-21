CREATE TABLE habitacion (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            numero VARCHAR(20) NOT NULL UNIQUE,
                            capacidad INT NOT NULL,
                            tipo VARCHAR(50),
                            estado VARCHAR(30) NOT NULL DEFAULT 'disponible'
);

CREATE TABLE estado_residente (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE deterioro_residente (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     nombre VARCHAR(50) NOT NULL UNIQUE,
                                     descripcion VARCHAR(255)
);

CREATE TABLE residente (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,

                           habitacion_id BIGINT NOT NULL,

                           apellido VARCHAR(100) NOT NULL,
                           nombre VARCHAR(100) NOT NULL,
                           dni VARCHAR(20) NOT NULL UNIQUE,
                           fecha_nacimiento DATE NOT NULL,

                           direccion VARCHAR(255),
                           telefono VARCHAR(30),
                           email VARCHAR(150),
                           telefono_emergencia VARCHAR(30),
                           familiar_a_cargo VARCHAR(150),

                           fecha_ingreso DATE NOT NULL,
                           fecha_egreso DATE,

                           obra_social VARCHAR(100),

                           CONSTRAINT fk_residente_habitacion
                               FOREIGN KEY (habitacion_id)
                                   REFERENCES habitacion(id)
);

CREATE TABLE historial_estado_residente (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                            residente_id BIGINT NOT NULL,
                                            estado_id BIGINT NOT NULL,

                                            fecha_cambio DATE NOT NULL,
                                            observacion VARCHAR(255),

                                            CONSTRAINT fk_historial_estado_residente
                                                FOREIGN KEY (residente_id)
                                                    REFERENCES residente(id),

                                            CONSTRAINT fk_historial_estado
                                                FOREIGN KEY (estado_id)
                                                    REFERENCES estado_residente(id)
);

CREATE TABLE historial_deterioro_residente (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                               residente_id BIGINT NOT NULL,
                                               deterioro_id BIGINT NOT NULL,

                                               fecha_cambio DATE NOT NULL,
                                               observacion VARCHAR(255),

                                               CONSTRAINT fk_historial_deterioro_residente
                                                   FOREIGN KEY (residente_id)
                                                       REFERENCES residente(id),

                                               CONSTRAINT fk_historial_deterioro
                                                   FOREIGN KEY (deterioro_id)
                                                       REFERENCES deterioro_residente(id)
);