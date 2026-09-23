package com.sofram.actividad.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "detalle_calendario")
public class DetalleCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendario_id", nullable = false)
    private Calendario calendario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false, length = 50)
    private String estado;

    protected DetalleCalendario() {
    }

    public DetalleCalendario(
            Calendario calendario,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            String estado
    ) {
        this.calendario = calendario;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public String getEstado() {
        return estado;
    }
}