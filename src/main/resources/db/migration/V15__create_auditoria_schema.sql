CREATE TABLE auditoria (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           usuario_id BIGINT NOT NULL,
                           username VARCHAR(50) NOT NULL,
                           rol VARCHAR(50) NOT NULL,
                           accion VARCHAR(50) NOT NULL,
                           modulo VARCHAR(50) NOT NULL,
                           entidad VARCHAR(100) NOT NULL,
                           entidad_id BIGINT NULL,
                           detalle VARCHAR(1000) NULL,
                           fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_auditoria_usuario
                               FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE INDEX idx_auditoria_usuario
    ON auditoria(usuario_id);

CREATE INDEX idx_auditoria_fecha
    ON auditoria(fecha_hora);

CREATE INDEX idx_auditoria_entidad
    ON auditoria(entidad, entidad_id);