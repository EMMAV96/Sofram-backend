package com.sofram.personal.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "asignacion_turno")
public class AsignacionTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @Column(name = "fecha_desde", nullable = false)
    private LocalDate fechaDesde;

    @Column(name = "fecha_hasta")
    private LocalDate fechaHasta;

    @Column(name = "motivo_cambio", length = 255)
    private String motivoCambio;

    protected AsignacionTurno() {
    }

    public AsignacionTurno(Empleado empleado, Turno turno, LocalDate fechaDesde, String motivoCambio) {
        this.empleado = empleado;
        this.turno = turno;
        this.fechaDesde = fechaDesde;
        this.motivoCambio = motivoCambio;
    }

    public Long getId() {
        return id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Turno getTurno() {
        return turno;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public String getMotivoCambio() {
        return motivoCambio;
    }

    public void cerrar(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
