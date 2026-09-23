package com.sofram.auditoria.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String rol;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(nullable = false, length = 50)
    private String modulo;

    @Column(nullable = false, length = 100)
    private String entidad;

    @Column(name = "entidad_id")
    private Long entidadId;

    @Column(length = 1000)
    private String detalle;

    @Column(
            name = "fecha_hora",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime fechaHora;

    protected Auditoria() {
    }

    public Auditoria(
            Long usuarioId,
            String username,
            String rol,
            String accion,
            String modulo,
            String entidad,
            Long entidadId,
            String detalle
    ) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.rol = rol;
        this.accion = accion;
        this.modulo = modulo;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalle = detalle;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public String getAccion() {
        return accion;
    }

    public String getModulo() {
        return modulo;
    }

    public String getEntidad() {
        return entidad;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public String getDetalle() {
        return detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}