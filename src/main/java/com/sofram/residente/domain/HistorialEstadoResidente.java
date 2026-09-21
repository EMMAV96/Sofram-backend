package com.sofram.residente.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "historial_estado_residente")
public class HistorialEstadoResidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "residente_id",
            nullable = false
    )
    private Residente residente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "estado_id",
            nullable = false
    )
    private EstadoResidente estado;

    @Column(nullable = false)
    private LocalDate fechaCambio;

    @Column(length = 255)
    private String observacion;

    public HistorialEstadoResidente() {
    }

    public Long getId() {
        return id;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    public EstadoResidente getEstado() {
        return estado;
    }

    public void setEstado(EstadoResidente estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}